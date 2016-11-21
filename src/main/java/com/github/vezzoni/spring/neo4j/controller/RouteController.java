package com.github.vezzoni.spring.neo4j.controller;

import com.github.vezzoni.spring.neo4j.exceptions.DataSourceException;
import com.github.vezzoni.spring.neo4j.resource.Route;
import com.github.vezzoni.spring.neo4j.service.StationService;
import org.perf4j.aop.Profiled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController {

    private Logger logger = LoggerFactory.getLogger(RouteController.class);

    @Autowired
    private StationService service;

    @RequestMapping(value = "/routes", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @Profiled(tag = "routes", logFailuresSeparately = true)
    public ResponseEntity checkRoute(
            @RequestParam(value="dep_sid") Integer departureId,
            @RequestParam(value="arr_sid") Integer arrivalId) {

        String uri = String.format("/routes?dep_sid=%s&arr_sid=%s", departureId, arrivalId);
        logger.info("uri: {}", uri);

        HttpStatus responseStatus = HttpStatus.OK;

        Route route = null;

        try {
            boolean hasDirectRoute = service.hasDirectRoute(departureId, arrivalId);

            route = new Route();
            route.setDepartureId(departureId);
            route.setArrivalId(arrivalId);
            route.setDirectBusRoute(hasDirectRoute);

        } catch (IllegalArgumentException e) {
            responseStatus = HttpStatus.BAD_REQUEST;
            logger.warn(e.getMessage());
        } catch (DataSourceException e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            logger.error(e.getMessage(), e);
        }

        ResponseEntity.BodyBuilder builder = ResponseEntity.status(responseStatus);

        logger.info("response: {}", responseStatus.value());
        return (route == null ? builder.build() : builder.body(route));
    }

}
