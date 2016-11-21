package com.github.vezzoni.spring.neo4j.service;

import com.github.vezzoni.spring.neo4j.exceptions.DataSourceException;
import com.github.vezzoni.spring.neo4j.model.Station;

public interface StationService {

    Station findByStationId(Integer stationId) throws IllegalArgumentException, DataSourceException;

    boolean hasDirectRoute(Integer sourceId, Integer targetId) throws IllegalArgumentException, DataSourceException;

    Station save(Station station) throws IllegalArgumentException, DataSourceException;

}
