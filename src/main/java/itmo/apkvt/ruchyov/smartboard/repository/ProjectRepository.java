package itmo.apkvt.ruchyov.smartboard.repository;

import itmo.apkvt.ruchyov.smartboard.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("select b from Project b where b.isEnabled = true")
    Project findEnabled();
}
