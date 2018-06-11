package itmo.apkvt.ruchyov.smartboard.repository;

import itmo.apkvt.ruchyov.smartboard.entity.Entry;
import itmo.apkvt.ruchyov.smartboard.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    @Transactional
    @Query("select e from Entry e where e.project =:projectEntry")
    List<Entry> findProjectEntries(@Param("projectEntry") Project project);

    @Transactional
    @Modifying
    @Query("delete from Entry e where e.project =:projectEntry")
    void deleteProjectEntries(@Param("projectEntry") Project project);
}


