package com.github.vezzoni.spring.neo4j.helper;

import com.github.vezzoni.spring.neo4j.model.Station;
import com.github.vezzoni.spring.neo4j.model.Travel;
import com.github.vezzoni.spring.neo4j.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;

@Component
public class DataLoader {

    @Autowired
    private StationService service;

    public void load(BufferedReader reader) throws IOException {

        if (reader == null) {
            throw new IllegalArgumentException();
        }

        String line = reader.readLine();
        Integer maxRoutes = Integer.valueOf(line);

        for (int routeIndex = 0; routeIndex < maxRoutes; routeIndex++) {

            line = reader.readLine();

            loadBusRoute(line);
        }

    }

    private void loadBusRoute(String line) {

        String[] lineValues = line.split(" ");

        Integer route = Integer.valueOf(lineValues[0]);
        Integer stationId;
        Station station;
        Station nextStation = null;
        Travel travel;

        for (int j = lineValues.length - 1; j > 0 ; j--) {

            stationId = Integer.valueOf(lineValues[j]);

            station = getStation(stationId);

            station.getRoutes().add(route);
            if (nextStation != null) {
                travel = new Travel();
                travel.setRoute(route);
                travel.setStartStation(station);
                travel.setEndStation(nextStation);
                station.getTravels().add(travel);
            }

            nextStation = service.save(station);
        }
    }

    private Station getStation(Integer stationId) {

        Station station = service.findByStationId(stationId);
        if (station == null) {
            station = new Station();
            station.setStationId(stationId);
        }

        return station;
    }

}
