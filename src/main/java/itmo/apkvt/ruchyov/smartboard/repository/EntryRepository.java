package itmo.apkvt.ruchyov.smartboard.repository;

import itmo.apkvt.ruchyov.smartboard.entity.Entry;
import itmo.apkvt.ruchyov.smartboard.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    @Query("select e from Entry e where e.project_id =:projectId")
    List<Entry> findProjectEntries(@Param("projectId") long projectId);
}
