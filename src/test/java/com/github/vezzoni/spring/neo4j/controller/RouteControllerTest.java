package com.github.vezzoni.spring.neo4j.controller;

import com.github.vezzoni.spring.neo4j.exceptions.DataSourceException;
import com.github.vezzoni.spring.neo4j.resource.Route;
import com.github.vezzoni.spring.neo4j.service.StationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RouteControllerTest {

    @Mock
    private StationService service;

    @InjectMocks
    private final RouteController controller = new RouteController();

    @Test
    public void testWithoutQueryString() {

        doThrow(IllegalArgumentException.class)
                .when(service)
                .hasDirectRoute(anyInt(), anyInt());

        ResponseEntity responseEntity = controller.checkRoute(null, null);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(400 == responseEntity.getStatusCode().value());

        verify(service, times(1)).hasDirectRoute(anyInt(), anyInt());
        verify(service, times(1)).hasDirectRoute(null, null);
    }

    @Test
    public void testWithDataSourceException() {

        Integer sourceId = 5;
        Integer targetId = 0;

        doThrow(DataSourceException.class)
                .when(service)
                .hasDirectRoute(anyInt(), anyInt());

        ResponseEntity responseEntity = controller.checkRoute(sourceId, targetId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertTrue(500 == responseEntity.getStatusCode().value());

        verify(service, times(1)).hasDirectRoute(anyInt(), anyInt());
        verify(service, times(1)).hasDirectRoute(sourceId, targetId);
    }

    @Test
    public void testWithoutDirectRoute() throws IOException {

        Integer sourceId = 5;
        Integer targetId = 0;
        when(service.hasDirectRoute(sourceId, targetId)).thenReturn(Boolean.FALSE);

        ResponseEntity<Route> responseEntity = controller.checkRoute(sourceId, targetId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(200 == responseEntity.getStatusCode().value());

        Route route = responseEntity.getBody();

        assertEquals(sourceId, route.getDepartureId());
        assertEquals(targetId, route.getArrivalId());
        assertFalse(route.getDirectBusRoute());

        verify(service, times(1)).hasDirectRoute(anyInt(), anyInt());
        verify(service, times(1)).hasDirectRoute(sourceId, targetId);
    }

    @Test
    public void testWithDirectRoute() throws IOException {

        Integer sourceId = 3;
        Integer targetId = 6;
        when(service.hasDirectRoute(sourceId, targetId)).thenReturn(Boolean.TRUE);

        ResponseEntity<Route> responseEntity = controller.checkRoute(sourceId, targetId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(200 == responseEntity.getStatusCode().value());

        Route route = responseEntity.getBody();

        assertEquals(sourceId, route.getDepartureId());
        assertEquals(targetId, route.getArrivalId());
        assertTrue(route.getDirectBusRoute());

        verify(service, times(1)).hasDirectRoute(anyInt(), anyInt());
        verify(service, times(1)).hasDirectRoute(sourceId, targetId);
    }

}
