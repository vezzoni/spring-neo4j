package com.github.vezzoni.spring.neo4j;

import com.github.vezzoni.spring.neo4j.helper.DataLoader;
import com.github.vezzoni.spring.neo4j.model.Station;
import com.github.vezzoni.spring.neo4j.model.Travel;
import com.github.vezzoni.spring.neo4j.service.StationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataLoaderTest {

    @Mock
    private StationService service;

    @InjectMocks
    private final DataLoader loader = new DataLoader();

    @Test(expected = IllegalArgumentException.class)
    public void testLoadwithNullArg() throws IOException {

        BufferedReader br = null;

        loader.load(br);

        verify(service, never()).findByStationId(anyInt());
        verify(service, never()).save(any(Station.class));
    }

    @Test
    public void testLoadwithValidFile() throws IOException {

        when(service.findByStationId(anyInt())).thenReturn(null);
        when(service.save(any(Station.class))).thenReturn(null);

        URL file = this.getClass().getClassLoader().getResource("testcase-valid");
        try (FileReader fr = new FileReader(file.getFile()); BufferedReader reader = new BufferedReader(fr)) {
            loader.load(reader);
        }

        verify(service, times(12)).findByStationId(anyInt());
        verify(service, times(12)).save(any(Station.class));
    }

    @Test
    public void testLoadwithValidFileAndExistingStation() throws IOException {

        Station start = new Station();
        Station end = new Station();
        end.setId(62L);
        end.setStationId(2);

        Travel travel = new Travel();
        travel.setId(61L);
        travel.setRoute(0);
        travel.setStartStation(start);
        travel.setEndStation(end);

        Set<Integer> routes = new HashSet<>(Arrays.asList(0));
        Set<Travel> travels = new HashSet<>(Arrays.asList(travel));

        Station station1 = new Station();
        station1.setId(60L);
        station1.setStationId(1);
        station1.setRoutes(routes);
        station1.setTravels(travels);

        Station station2 = new Station();
        station2.setId(60L);
        station2.setStationId(2);

        when(service.findByStationId(anyInt())).thenReturn(null);
        when(service.findByStationId(1)).thenReturn(null, station1);
        when(service.save(any(Station.class))).thenReturn(null);
        when(service.save(station2)).thenReturn(station2);

        URL file = this.getClass().getClassLoader().getResource("testcase-valid");
        try (FileReader fr = new FileReader(file.getFile()); BufferedReader reader = new BufferedReader(fr)) {
            loader.load(reader);
        }

        verify(service, times(12)).findByStationId(anyInt());
        verify(service, times(12)).save(any(Station.class));
    }

}
