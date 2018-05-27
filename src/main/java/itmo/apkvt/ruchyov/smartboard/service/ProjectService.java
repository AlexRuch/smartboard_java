package itmo.apkvt.ruchyov.smartboard.service;

import itmo.apkvt.ruchyov.smartboard.entity.Project;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProjectService {

    Project createProject(final String projectName);

    void deleteProject(final long projectId);

    Optional<Project> getProject(final long projectId);

    List<Project> getAllProjects();

    void makeProjectEnabled(final long projectId);

    void updateProject(final String projectName, final long projectId);

    Project updateEntryPosition(final long projectId, final long entryPosition, final String changeType);

    Project addTextEntry(final long projectId, final String entryName, final String entryText);

    Project addImageEntry(final long projectId, final String entryName, final MultipartFile multipartFile) throws IOException;

    Project addTableEntry(final long projectId, final String entryName, final String css, final List<String> tableRows);

    Project deleteEntry(final long entryId);
}
