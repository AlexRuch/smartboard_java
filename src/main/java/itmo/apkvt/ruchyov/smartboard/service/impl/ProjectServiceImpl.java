package itmo.apkvt.ruchyov.smartboard.service.impl;

import itmo.apkvt.ruchyov.smartboard.entity.Entry;
import itmo.apkvt.ruchyov.smartboard.entity.Project;
import itmo.apkvt.ruchyov.smartboard.entity.TableRow;
import itmo.apkvt.ruchyov.smartboard.repository.EntryRepository;
import itmo.apkvt.ruchyov.smartboard.repository.ProjectRepository;
import itmo.apkvt.ruchyov.smartboard.repository.TableRowRepository;
import itmo.apkvt.ruchyov.smartboard.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final EntryRepository entryRepository;

    private final TableRowRepository tableRowRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, EntryRepository entryRepository, TableRowRepository tableRowRepository) {
        this.projectRepository = projectRepository;
        this.entryRepository = entryRepository;
        this.tableRowRepository = tableRowRepository;
    }

    @Transactional
    @Override
    public Project createProject(String projectName) {
        Project project = new Project();
        project.setProjectName(projectName);
        project.setCreateDate(new Date().getTime());
        project.setUpdateDate(new Date().getTime());
        projectRepository.save(project);
        projectRepository.flush();
        return project;
    }

    @Transactional
    @Override
    public void deleteProject(long projectId) {
        System.out.println("PROJECT ID: " + projectId);
        Project project = projectRepository.getOne(projectId);
        project.getEntryList().forEach(tableRowRepository::deleteByEntry);
        for (Entry entry : project.getEntryList()) {
            if (entry.getContentType().equals("IMAGE")) {
                File image = new File(imageProperties.getProperty("image.fs_path") + entry.getImagePath().replace(imageProperties.getProperty("image.db_path"), ""));
                image.delete();
            }
        }
        entryRepository.deleteProjectEntries(project);
        projectRepository.deleteById(projectId);
    }

    @Transactional
    @Override
    public Optional<Project> getProject(long projectId) {
        return projectRepository.findById(projectId);
    }

    @Transactional
    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Transactional
    @Override
    public Project getEnableProject() {
        return projectRepository.findEnabled();
    }

    @Transactional
    @Override
    public void makeProjectEnabled(long projectId) {
        if (projectRepository.findEnabled() != null) {
            projectRepository.findEnabled().setEnabled(false);
        }
        projectRepository.getOne(projectId).setEnabled(true);

        projectRepository.flush();
    }

    @Transactional
    @Override
    public void updateProject(String projectName, long projectId) {
        Objects.requireNonNull(projectRepository.findById(projectId).orElse(null)).setProjectName(projectName);
        Objects.requireNonNull(projectRepository.findById(projectId).orElse(null)).setUpdateDate(new Date().getTime());
        projectRepository.flush();
    }

    @Transactional
    @Override
    public Project updateEntryPosition(final long projectId, final long entryPosition, final String changeType) {
        Project project = projectRepository.getOne(projectId);
        Long entryId = Objects.requireNonNull(project.getEntryList().stream()
                .filter(e -> e.getEntryPosition() == entryPosition)
                .findAny()
                .orElse(null))
                .getEntryId();
        if (changeType.equals("UP")) {

            Objects.requireNonNull(project.getEntryList().stream()
                    .filter(e -> e.getEntryPosition() == entryPosition - 1)
                    .findAny()
                    .orElse(null))
                    .setEntryPosition(entryPosition);
            Objects.requireNonNull(project.getEntryList().stream()
                    .filter(e -> e.getEntryId() == entryId)
                    .findAny()
                    .orElse(null))
                    .setEntryPosition(entryPosition - 1);

        } else if (changeType.equals("DOWN")) {

            Objects.requireNonNull(project.getEntryList().stream()
                    .filter(e -> e.getEntryPosition() == entryPosition + 1)
                    .findAny()
                    .orElse(null))
                    .setEntryPosition(entryPosition);
            Objects.requireNonNull(project.getEntryList().stream()
                    .filter(e -> e.getEntryId() == entryId)
                    .findAny()
                    .orElse(null))
                    .setEntryPosition(entryPosition + 1);
        }
        entryRepository.flush();
        project.setUpdateDate(new Date().getTime());
        projectRepository.saveAndFlush(project);
        return projectRepository.findById(projectId).orElse(null);
    }

    @Transactional
    @Override
    public Project addTextEntry(long projectId, String entryName, String entryText) {
        Project project = projectRepository.getOne(projectId);

        Entry entry = new Entry(entryName, entryText, "TEXT", project.getEntryList().size() + 1, project);
        entryRepository.saveAndFlush(entry);

        project.getEntryList().add(entry);

        project.setUpdateDate(new Date().getTime());
        projectRepository.saveAndFlush(project);

        return project;
    }

    private Properties imageProperties = new Properties();

    {
        try {
            imageProperties.load(ProjectServiceImpl.class.getResourceAsStream("/image.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    @Override
    public Project addImageEntry(long projectId, String entryName, MultipartFile multipartFile) throws IOException {
        byte[] imageBytes = multipartFile.getBytes();
        String name = String.valueOf(new Date().getTime()) + ".jpg";
        File dir = new File(imageProperties.getProperty("image.fs_path"));
        if (!dir.exists()) {
            dir.mkdir();
        }
        File imageFile = new File(dir.getAbsolutePath() + File.separator + name);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(imageFile));
        bufferedOutputStream.write(imageBytes);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();

        Project project = projectRepository.getOne(projectId);

        Entry entry = new Entry(entryName
                , imageProperties.getProperty("image.db_path") + name
                , "IMAGE"
                , project.getEntryList().size() + 1
                , project);

        project.getEntryList().add(entry);
        project.setUpdateDate(new Date().getTime());
        entryRepository.saveAndFlush(entry);
        projectRepository.saveAndFlush(project);

        return project;
    }

    @Transactional
    @Override
    public Project addTableEntry(long projectId, String entryName, String css, List<String> tableRows) {
        Project project = projectRepository.getOne(projectId);
        Entry entry = new Entry(entryName
                , "TABLE"
                , project.getEntryList().size() + 1
                , css
                , project);
        entryRepository.saveAndFlush(entry);
        project.getEntryList().add(entry);
        projectRepository.saveAndFlush(project);

        for (String rowText : tableRows) {
            TableRow tableRow = new TableRow(rowText, entry);
            tableRowRepository.saveAndFlush(tableRow);
            entry.getTableRowList().add(tableRow);
        }
        entryRepository.saveAndFlush(entry);
        return project;
    }

    @Transactional
    @Override
    public Project deleteEntry(long entryId) {
        Project project = entryRepository.getOne(entryId).getProject();
        long position = entryRepository.getOne(entryId).getEntryPosition();
        Entry entryEntity = entryRepository.getOne(entryId);
        if (entryEntity.getContentType().equals("IMAGE")) {
            File image = new File(imageProperties.getProperty("image.fs_path") + entryEntity.getImagePath().replace(imageProperties.getProperty("image.db_path"), ""));
            image.delete();
        } else if (entryEntity.getContentType().equals("TABLE")) {
            tableRowRepository.deleteByEntry(entryEntity);
        }
        entryRepository.deleteById(entryId);
        for (Entry entry : entryRepository.findProjectEntries(project)) {
            if (entry.getEntryPosition() > position) {
                entry.setEntryPosition(entry.getEntryPosition() - 1);
            }
        }
        entryRepository.flush();
        project.setUpdateDate(new Date().getTime());
        projectRepository.saveAndFlush(project);
        return project;
    }
}
