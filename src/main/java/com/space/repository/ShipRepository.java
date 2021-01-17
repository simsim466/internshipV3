package com.space.repository;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/*
отвечает за взаимодействие с БД
name=String//
planet=String//
shipType=ShipType
after=Long//
before=Long//
isUsed=Boolean
minSpeed=Double//
maxSpeed=Double//
minCrewSize=Integer//
maxCrewSize=Integer//
minRating=Double//
maxRating=Double//
order=ShipOrder
pageNumber=Integer
pageSize=Integer
 */
@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {
    //единый метод запрос с shipType и isUsed null
    List<Ship> findShipsByNameContainingAndPlanetContainingAndProdDateBetweenAndSpeedBetweenAndCrewSizeBetweenAndRatingBetween(
            String name, String planet, Date after, Date before, Double minSpeed, Double maxSpeed,
            Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, Pageable pageable);
    //единый метод запрос с shipType null
    List<Ship> findShipsByNameContainingAndPlanetContainingAndProdDateBetweenAndSpeedBetweenAndCrewSizeBetweenAndRatingBetweenAndIsUsed(
            String name, String planet, Date after, Date before, Double minSpeed, Double maxSpeed,
            Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, Boolean isUsed, Pageable pageable);
    //единый метод запрос с isUsed null
    List<Ship> findShipsByNameContainingAndPlanetContainingAndProdDateBetweenAndSpeedBetweenAndCrewSizeBetweenAndRatingBetweenAndShipType(
            String name, String planet, Date after, Date before, Double minSpeed, Double maxSpeed,
            Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, ShipType shipType, Pageable pageable);
    //единый метод запрос
    List<Ship> findShipsByNameContainingAndPlanetContainingAndProdDateBetweenAndSpeedBetweenAndCrewSizeBetweenAndRatingBetweenAndIsUsedAndShipType(
            String name, String planet, Date after, Date before, Double minSpeed, Double maxSpeed,
            Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, Boolean isUsed, ShipType shipType, Pageable pageable);

    //дублируем все эти запросы без пэйджинга
    //единый метод запрос с shipType и isUsed null
    List<Ship> findShipsByNameContainingAndPlanetContainingAndProdDateBetweenAndSpeedBetweenAndCrewSizeBetweenAndRatingBetween(
            String name, String planet, Date after, Date before, Double minSpeed, Double maxSpeed,
            Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating);
    //единый метод запрос с shipType null
    List<Ship> findShipsByNameContainingAndPlanetContainingAndProdDateBetweenAndSpeedBetweenAndCrewSizeBetweenAndRatingBetweenAndIsUsed(
            String name, String planet, Date after, Date before, Double minSpeed, Double maxSpeed,
            Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, Boolean isUsed);
    //единый метод запрос с isUsed null
    List<Ship> findShipsByNameContainingAndPlanetContainingAndProdDateBetweenAndSpeedBetweenAndCrewSizeBetweenAndRatingBetweenAndShipType(
            String name, String planet, Date after, Date before, Double minSpeed, Double maxSpeed,
            Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, ShipType shipType);
    //единый метод запрос
    List<Ship> findShipsByNameContainingAndPlanetContainingAndProdDateBetweenAndSpeedBetweenAndCrewSizeBetweenAndRatingBetweenAndIsUsedAndShipType(
            String name, String planet, Date after, Date before, Double minSpeed, Double maxSpeed,
            Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, Boolean isUsed, ShipType shipType);
}
