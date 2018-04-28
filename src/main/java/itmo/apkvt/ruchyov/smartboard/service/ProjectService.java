package itmo.apkvt.ruchyov.smartboard.service;

import itmo.apkvt.ruchyov.smartboard.entity.Entry;
import itmo.apkvt.ruchyov.smartboard.entity.Project;

import java.util.List;

public interface ProjectService {

    void createProject(final String projectName);

    void deleteProject(final long projectId);

    Project getProject(final long projectId);

    List<Project> getAllProjects();

    void makeProgectEnabled(final long projectId);

    void updateProject(final String projectName, final long projectId);

    void addEntryToProject(final Entry entry, final long projectId);
}
