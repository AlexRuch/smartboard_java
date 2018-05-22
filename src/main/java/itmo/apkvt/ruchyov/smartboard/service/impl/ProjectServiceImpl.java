package itmo.apkvt.ruchyov.smartboard.service.impl;

import itmo.apkvt.ruchyov.smartboard.entity.Entry;
import itmo.apkvt.ruchyov.smartboard.entity.Project;
import itmo.apkvt.ruchyov.smartboard.repository.EntryRepository;
import itmo.apkvt.ruchyov.smartboard.repository.ProjectRepository;
import itmo.apkvt.ruchyov.smartboard.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final EntryRepository entryRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, EntryRepository entryRepository) {
        this.projectRepository = projectRepository;
        this.entryRepository = entryRepository;
    }

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

    @Override
    public void deleteProject(long projectId) {
        projectRepository.deleteById(projectId);
    }

    @Override
    public Optional<Project> getProject(long projectId) {
        return projectRepository.findById(projectId);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public void makeProjectEnabled(long projectId) {
        if (projectRepository.findEnabled() != null) {
            projectRepository.findEnabled().setEnabled(false);
        }
        projectRepository.getOne(projectId).setEnabled(true);

        projectRepository.flush();
    }

    @Override
    public void updateProject(String projectName, long projectId) {
        Objects.requireNonNull(projectRepository.findById(projectId).orElse(null)).setProjectName(projectName);
        projectRepository.flush();
    }

    @Override
    public void addEntryToProject(Entry entry, long projectId) {

    }

    @Override
    public Project updateEntryPosition(final long projectId, final long entryId, final String changeType) {
        if (changeType.equals("UP")) {
            Objects.requireNonNull(entryRepository.findProjectEntries(projectId).stream()
                    .filter(e -> e.getEntryId() == entryId)
                    .findAny()
                    .orElse(null))
                    .setEntryPosition(entryId + 1);
            Objects.requireNonNull(entryRepository.findProjectEntries(projectId).stream()
                    .filter(e -> e.getEntryId() == entryId + 1)
                    .findAny()
                    .orElse(null))
                    .setEntryPosition(entryId);
            entryRepository.flush();
        } else if (changeType.equals("DOWN")) {
            Objects.requireNonNull(entryRepository.findProjectEntries(projectId).stream()
                    .filter(e -> e.getEntryId() == entryId)
                    .findAny()
                    .orElse(null))
                    .setEntryPosition(entryId - 1);
            Objects.requireNonNull(entryRepository.findProjectEntries(projectId).stream()
                    .filter(e -> e.getEntryId() == entryId - 1)
                    .findAny()
                    .orElse(null))
                    .setEntryPosition(entryId);
            entryRepository.flush();
        }

        return projectRepository.findById(projectId).orElse(null);
    }
}
