package com.github.vezzoni.spring.neo4j.repository;

import com.github.vezzoni.spring.neo4j.model.Station;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends GraphRepository<Station> {

    Station findByStationId(Integer stationId);

    @Query("match (s {stationId: {from}})-[r:TRAVELS*]->(t {stationId: {to}}) " +
            "where all(rel in r where rel.route in s.routes) " +
            "return distinct s")
    Iterable<Station> findRoutes(@Param("from") Integer from, @Param("to") Integer to);

}
