package com.netflix.testingdemo.lolomo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowsRepository extends CrudRepository<ShowEntity, Long> {
    @Query(value = "SELECT * from shows where ?1 = any(categories)", nativeQuery = true)
    List<ShowEntity> findByCategory(String category);
}
