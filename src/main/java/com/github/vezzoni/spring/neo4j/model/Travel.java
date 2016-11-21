package com.github.vezzoni.spring.neo4j.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type="TRAVELS")
public class Travel {

    @GraphId
    private Long id;

    private Integer route;
    @StartNode
    private Station startStation;
    @EndNode
    private Station endStation;

    public Travel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRoute() {
        return route;
    }

    public void setRoute(Integer route) {
        this.route = route;
    }

    public Station getStartStation() {
        return startStation;
    }

    public void setStartStation(Station startStation) {
        this.startStation = startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public void setEndStation(Station endStation) {
        this.endStation = endStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Travel travel = (Travel) o;

        return id != null ? id.equals(travel.id) : travel.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Travel{" +
                "id=" + id +
                ", route=" + route +
                ", startStation=" + startStation.getStationId() +
                ", endStation=" + endStation.getStationId() +
                '}';
    }
}
