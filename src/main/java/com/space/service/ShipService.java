package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Pageable;


import java.sql.Date;
import java.util.List;

public interface ShipService {

    Ship getShipById(long id);//5

    //List<Ship> getAllShips();//1

    boolean deleteShip(long id);//4

    Ship create(Ship ship);//2

    Ship update(long id, Ship ship);//3

    List<Ship> getSatisfiedShips(String name, String planet, Long after, Long before, ShipType shipType,
                                 Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                                 Double minRating, Double maxRating, Boolean isUsed,
                                 ShipOrder order, Integer pageNumber, Integer pageSize);//6

    Integer numOfSatisfiedShips(String name, String planet, Long after, Long before, ShipType shipType,
                                Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                                Double minRating, Double maxRating, Boolean isUsed);//7

    //методы на все случаи жизни
    List<Ship> getShipsWithNullShipTypeAndIsUsed(String name, String planet, Date after, Date before,
                                                 Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                                                 Double minRating, Double maxRating, Pageable pageable);

    List<Ship> getShipsWithNullShipType(String name, String planet, Date after, Date before,
                                        Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                                        Double minRating, Double maxRating, Boolean isUsed, Pageable pageable);

    List<Ship> getShipsWithNullIsUsed(String name, String planet, Date after, Date before, Double minSpeed, Double maxSpeed,
                                      Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating,
                                      ShipType shipType, Pageable pageable);

    List<Ship> getShipsWithCompleteRequest(String name, String planet, Date after, Date before,
                                           Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                                           Double minRating, Double maxRating, Boolean isUsed, ShipType shipType,
                                           Pageable pageable);

}
