package itmo.apkvt.ruchyov.smartboard.service;

import itmo.apkvt.ruchyov.smartboard.entity.Entry;
import itmo.apkvt.ruchyov.smartboard.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    void createProject(final String projectName);

    void deleteProject(final long projectId);

    Optional<Project> getProject(final long projectId);

    List<Project> getAllProjects();

    void makeProjectEnabled(final long projectId);

    void updateProject(final String projectName, final long projectId);

    void addEntryToProject(final Entry entry, final long projectId);

    Project updateEntryPosition(final long projectId, final long entryId, final String changeType);
}
