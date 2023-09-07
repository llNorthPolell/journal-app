package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.Journal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface JournalRepository extends CrudRepository<Journal, Integer> {
    @Query("select j.journalRef from Journal j where j.author=:author")
    List<UUID> findJournalRefsByAuthor(@Param("author") String authorId);

    List<Journal> findJournalsByAuthor(@Param("author") String authorId);

    Journal findByJournalRef(@Param("journalRef") UUID journalRef);

    @Query("select j.id from Journal j where j.journalRef=:journalRef")
    Integer findJournalId(@Param("journalRef") UUID journalRef);

    @Query("select j.journalRef from Journal j where j.id=:journalId")
    UUID findJournalRef(@Param("journalId") int journalId);
}
