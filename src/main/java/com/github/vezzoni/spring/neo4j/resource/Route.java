package com.github.vezzoni.spring.neo4j.resource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Route {

    private Integer departureId;
    private Integer arrivalId;
    private Boolean directBusRoute;

    public Integer getDepartureId() {
        return departureId;
    }

    public void setDepartureId(Integer departureId) {
        this.departureId = departureId;
    }

    public Integer getArrivalId() {
        return arrivalId;
    }

    public void setArrivalId(Integer arrivalId) {
        this.arrivalId = arrivalId;
    }

    public Boolean getDirectBusRoute() {
        return directBusRoute;
    }

    public void setDirectBusRoute(Boolean directBusRoute) {
        this.directBusRoute = directBusRoute;
    }

}
