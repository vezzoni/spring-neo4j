package com.github.vezzoni.spring.neo4j.service;

import com.github.vezzoni.spring.neo4j.exceptions.DataSourceException;
import com.github.vezzoni.spring.neo4j.model.Station;
import com.github.vezzoni.spring.neo4j.model.Travel;
import com.github.vezzoni.spring.neo4j.repository.StationRepository;
import com.github.vezzoni.spring.neo4j.service.impl.StationServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StationServiceTest {

    @Mock
    private StationRepository repository;

    @InjectMocks
    private final StationService service = new StationServiceImpl();

    @Test(expected = IllegalArgumentException.class)
    public void getFindByNullStationId() {

        Integer stationId = null;

        service.findByStationId(stationId);

        verify(repository, never()).findByStationId(anyInt());
    }

    @Test(expected = DataSourceException.class)
    public void getFindByStationIdWithDataException() {

        Integer stationId = 1;

        doThrow(RuntimeException.class).when(repository).findByStationId(stationId);

        service.findByStationId(stationId);

        verify(repository, times(1)).findByStationId(anyInt());
        verify(repository, times(1)).findByStationId(stationId);
    }

    @Test
    public void getFindByValidStationId() {

        Integer stationId = 9;

        Station expectedStation = new Station();
        expectedStation.setId(1L);
        expectedStation.setStationId(stationId);

        when(service.findByStationId(stationId)).thenReturn(expectedStation);

        Station station = service.findByStationId(stationId);

        assertNotNull(station);
        assertEquals(expectedStation, station);
        assertTrue(station.getRoutes().isEmpty());
        assertTrue(station.getTravels().isEmpty());

        verify(repository, times(1)).findByStationId(anyInt());
        verify(repository, times(1)).findByStationId(stationId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIfHasDirectRoutesWithNullArgs() {

        Integer sourceId = null;
        Integer targetId = null;

        service.hasDirectRoute(sourceId, targetId);

        verify(repository, never()).findRoutes(anyInt(), anyInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIfHasDirectRoutesWithNullSourceId() {

        Integer sourceId = null;
        Integer targetId = 9;

        service.hasDirectRoute(sourceId, targetId);

        verify(repository, never()).findRoutes(anyInt(), anyInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIfHasDirectRoutesWithNullTargetId() {

        Integer sourceId = 9;
        Integer targetId = null;

        service.hasDirectRoute(sourceId, targetId);

        verify(repository, never()).findRoutes(anyInt(), anyInt());
    }

    @Test(expected = DataSourceException.class)
    public void testIfHasDirectRoutesWithDataException() {

        Integer sourceId = 3;
        Integer targetId = 6;

        doThrow(RuntimeException.class).when(repository).findRoutes(sourceId, targetId);

        service.hasDirectRoute(sourceId, targetId);

        verify(repository, times(1)).findRoutes(anyInt(), anyInt());
        verify(repository, times(1)).findRoutes(sourceId, targetId);
    }

    @Test
    public void testIfHasDirectRoutesWithoutDirectRoutes() {

        Integer sourceId = 0;
        Integer targetId = 5;

        Iterable<Station> expectedRoutes = Arrays.asList();
        when(repository.findRoutes(sourceId, targetId)).thenReturn(expectedRoutes);

        boolean hasDirectRoute = service.hasDirectRoute(sourceId, targetId);

        assertFalse(hasDirectRoute);

        verify(repository, times(1)).findRoutes(anyInt(), anyInt());
        verify(repository, times(1)).findRoutes(sourceId, targetId);
    }

    @Test
    public void testIfHasDirectRoutesWithDirectRoutes() {

        Integer sourceId = 3;
        Integer targetId = 6;

        Long nodeId = 1L;
        Integer stationId = 9;
        Integer routeId = 1;

        Station station = new Station();
        station.setId(nodeId);
        station.setStationId(stationId);
        station.getRoutes().add(routeId);

        Iterable<Station> expectedRoutes = Arrays.asList(station);
        when(repository.findRoutes(sourceId, targetId)).thenReturn(expectedRoutes);

        boolean hasDirectRoute = service.hasDirectRoute(sourceId, targetId);

        assertTrue(hasDirectRoute);

        verify(repository, times(1)).findRoutes(anyInt(), anyInt());
        verify(repository, times(1)).findRoutes(sourceId, targetId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWithNullArg() {

        Station station = null;

        service.save(station);

        verify(repository, never()).save(any(Station.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWithNullStationId() {

        Integer stationId = null;

        Station station = new Station();
        station.setStationId(stationId);

        service.save(station);

        verify(repository, never()).save(any(Station.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWithInvalidRoutes() {

        Integer stationId = 9;
        Set<Integer> routes = new HashSet<>();

        Station station = new Station();
        station.setStationId(stationId);
        station.setRoutes(routes);

        service.save(station);

        verify(repository, never()).save(any(Station.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWithNullRoutes() {

        Integer stationId = 9;
        Set<Integer> routes = null;

        Station station = new Station();
        station.setStationId(stationId);
        station.setRoutes(routes);

        service.save(station);

        verify(repository, never()).save(any(Station.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWithNullTravels() {

        Integer stationId = 9;
        Set<Integer> routes = new HashSet<>(Arrays.asList(1));
        Set<Travel> travels = null;

        Station station = new Station();
        station.setStationId(stationId);
        station.setRoutes(routes);
        station.setTravels(travels);

        service.save(station);

        verify(repository, never()).save(any(Station.class));
    }

    @Test(expected = DataSourceException.class)
    public void testSaveWithNullDataSourceException() {

        Integer stationId = 9;
        Set<Integer> routes = new HashSet<>(Arrays.asList(1));
        Set<Travel> travels = new HashSet<>();

        Station station = new Station();
        station.setStationId(stationId);
        station.setRoutes(routes);
        station.setTravels(travels);

        doThrow(RuntimeException.class).when(repository).save(station);

        service.save(station);

        verify(repository, times(1)).save(any(Station.class));
        verify(repository, times(1)).save(station);
    }

    @Test
    public void testSaveNewStation() {

        Long nodeId = 1L;
        Integer stationId = 9;
        Integer routeId = 1;

        Station expectedStation = new Station();
        expectedStation.setId(nodeId);
        expectedStation.setStationId(stationId);
        expectedStation.getRoutes().add(routeId);

        Station station = new Station();
        station.setId(null);
        station.setStationId(stationId);
        station.getRoutes().add(routeId);

        when(repository.save(station)).thenReturn(expectedStation);

        Station savedStation = service.save(station);

        assertNotNull(savedStation);
        assertEquals(expectedStation, savedStation);
        assertEquals(nodeId, savedStation.getId());

        verify(repository, times(1)).save(any(Station.class));
        verify(repository, times(1)).save(station);
    }

    @Test
    public void testSaveExistingStation() {

        Long nodeId = 1L;
        Integer stationId = 9;
        Integer route1 = 1;
        Integer route2 = 2;

        Station originalStation = new Station();
        originalStation.setId(nodeId);
        originalStation.setStationId(stationId);
        originalStation.getRoutes().add(route1);

        when(repository.findByStationId(stationId)).thenReturn(originalStation);

        Station station = service.findByStationId(stationId);
        assertTrue(station.getRoutes().size() == 1);

        station.getRoutes().add(route2);

        Station expectedStation = new Station();
        expectedStation.setId(nodeId);
        expectedStation.setStationId(stationId);
        expectedStation.getRoutes().add(route1);
        expectedStation.getRoutes().add(route2);

        when(repository.save(station)).thenReturn(expectedStation);

        Station savedStation = service.save(station);

        assertNotNull(savedStation);
        assertEquals(expectedStation, savedStation);
        assertEquals(nodeId, savedStation.getId());
        assertTrue(expectedStation.getRoutes().size() == savedStation.getRoutes().size());

        verify(repository, times(1)).findByStationId(anyInt());
        verify(repository, times(1)).findByStationId(stationId);
        verify(repository, times(1)).save(any(Station.class));
        verify(repository, times(1)).save(station);
    }

}
