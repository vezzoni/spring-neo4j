package com.github.vezzoni.spring.neo4j.service.impl;

import com.github.vezzoni.spring.neo4j.exceptions.DataSourceException;
import com.github.vezzoni.spring.neo4j.model.Station;
import com.github.vezzoni.spring.neo4j.repository.StationRepository;
import com.github.vezzoni.spring.neo4j.service.StationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationServiceImpl implements StationService {

    private Logger logger = LoggerFactory.getLogger(StationService.class);

    @Autowired
    private StationRepository repository;

    @Override
    public Station findByStationId(Integer stationId) {

        logger.debug("stationId: {}", stationId);

        if (stationId == null) {
            throw new IllegalArgumentException();
        }

        try {
            return repository.findByStationId(stationId);
        } catch (RuntimeException e) {
            throw new DataSourceException(e);
        }
    }

    @Override
    public boolean hasDirectRoute(Integer sourceId, Integer targetId) {

        logger.debug("sourceId: {}", sourceId);
        logger.debug("targetId: {}", targetId);

        if (sourceId == null || targetId == null) {
            throw new IllegalArgumentException();
        }

        Iterable<Station> routes;
        try {
            routes = repository.findRoutes(sourceId, targetId);
        } catch (RuntimeException e) {
            throw new DataSourceException(e);
        }

        return !((List) routes).isEmpty();
    }

    @Override
    public Station save(Station station) {

        logger.debug("station: {}", station);

        validateStation(station);

        try {
            return repository.save(station);
        } catch (RuntimeException e) {
            throw new DataSourceException(e);
        }
    }

    private void validateStation(Station station) {

        boolean isValid = station != null
                && station.getStationId() != null
                && station.getRoutes() != null
                && !station.getRoutes().isEmpty()
                && station.getTravels() != null;

        if (!isValid){
            throw new IllegalArgumentException();
        }
    }
}
