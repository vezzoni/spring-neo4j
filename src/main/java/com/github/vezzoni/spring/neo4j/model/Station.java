package com.github.vezzoni.spring.neo4j.model;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = "Station")
public class Station {

    @GraphId
    private Long id;

    private Integer stationId;
    private Set<Integer> routes;

    @Relationship(type="TRAVELS", direction = Relationship.OUTGOING)
    private Set<Travel> travels;

    public Station() {
        this.routes = new HashSet<>();
        this.travels = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public Set<Integer> getRoutes() {
        return routes;
    }

    public void setRoutes(Set<Integer> routes) {
        this.routes = routes;
    }

    public Set<Travel> getTravels() {
        return travels;
    }

    public void setTravels(Set<Travel> travels) {
        this.travels = travels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Station station = (Station) o;

        return stationId.equals(station.stationId);

    }

    @Override
    public int hashCode() {
        return stationId.hashCode();
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", stationId=" + stationId +
                ", routes=" + (routes == null ? null : Arrays.toString(routes.toArray())) +
                ", travels=" + (travels == null ? null : Arrays.toString(travels.toArray())) +
                '}';
    }
}
