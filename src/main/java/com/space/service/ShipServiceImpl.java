package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class ShipServiceImpl implements ShipService {


    ShipRepository shipRepository;
    /*
    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();//создание объекта класса драйвера для взаимодействия с MySQL
            connection = DriverManager.getConnection("jdbc:mysql://localhost/cosmoport?serverTimezone=Europe/Moscow&useSSL=false", "root", "root");
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException t) {
            t.printStackTrace();
        }
    }*/

    @Autowired
    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    //достать пользователя по id готово
    @Override
    public Ship getShipById(long id) {
        return shipRepository.findById(id).get();
    }

    //Удаление готово
    @Override
    public boolean deleteShip(long id) {
        if ( !shipRepository.existsById(id) )
            return false;

        shipRepository.deleteById(id);
        return true;
    }

    //создание корабля - готово
    @Override
    public Ship create(Ship ship) {
        if ( ship == null || ship.getName() == null || ship.getPlanet() == null ||
                ship.getShipType() == null || ship.getProdDate() == null ||
                ship.getSpeed() == null ||
                ship.getCrewSize() == null )
            return null;

        Ship shipCreated = new Ship();
        //имя
        String name = ship.getName();
        if ( name.isEmpty() || name.length() > 50 )
            return null;
        else shipCreated.setName(name);

        //планета
        String planet = ship.getPlanet();
        if ( planet.isEmpty() || planet.length() > 50 )
            return null;
        else shipCreated.setPlanet(planet);

        //скорость
        double speed = ship.getSpeed();
        if ( speed <= 0 || speed >= 1 )
            return null;
        else shipCreated.setSpeed(speed);

        //размер экипажа
        int crewSize = ship.getCrewSize();
        if ( crewSize < 1 || crewSize > 9999 )
            return null;
        else shipCreated.setCrewSize(crewSize);

        //дата
        if ( !ship.isProDateCorrect() )
            return null;
        else shipCreated.setProdDate(ship.getProdDate());

        //использование
        if ( ship.isUsed() == null)
            shipCreated.setUsed(false);
        else shipCreated.setUsed(ship.isUsed());

        //тип
        shipCreated.setShipType(ship.getShipType());

        shipCreated.calculateRating();

        return shipRepository.save(shipCreated);
    }
    //обновление готово - см.контроллер
    @Override
    public Ship update(long id, Ship ship) {
        Ship shipUpdated = getShipById(id);
        if ( ship != null && !ship.isEmpty() ) {
            if ( shipUpdated != null ) {
                //имя
                if ( ship.getName() != null && shipUpdated.getName() != null )  {
                    if (  ship.getName().length() < 51 && !ship.getName().isEmpty() )
                        shipUpdated.setName(ship.getName());
                    else return null;
                }
                //планета
                if ( ship.getPlanet() != null && shipUpdated.getName() != null )    {
                    if ( ship.getPlanet().length() < 51 && !ship.getPlanet().isEmpty() )
                        shipUpdated.setPlanet(ship.getPlanet());
                    else return null;
                }
                //тип корабля
                if ( ship.getShipType() != null && shipUpdated.getShipType() != null )
                    shipUpdated.setShipType(ship.getShipType());

                //дата
                if ( shipUpdated.getProdDate() != null && ship.getProdDate() != null )  {
                    if ( ship.isProDateCorrect() )
                        shipUpdated.setProdDate(ship.getProdDate());
                    else return null;
                }


                //показатель используемости
                if ( shipUpdated.isUsed() != null  && ship.isUsed() != null )
                    shipUpdated.setUsed(false);

                //максимальная скорость
                if ( shipUpdated.getSpeed() != null && ship.getSpeed() != null )    {
                    double speed = ship.getSpeed();
                    if ( speed >= 0 && speed < 1 )
                        shipUpdated.setSpeed(speed);
                }

                //размер
                if ( shipUpdated.getCrewSize() != null && ship.getCrewSize() != null )  {
                    int crewSize = ship.getCrewSize();
                    if ( crewSize > 0 && crewSize < 10000 )
                        shipUpdated.setCrewSize(crewSize);
                    else return null;
                }

                //высчитываем рейтинг
                shipUpdated.calculateRating();
            }
            else throw new NoSuchElementException();
        }


        return shipUpdated == null ? null : shipRepository.saveAndFlush(shipUpdated);
    }
    //основной метод для получения по фильтрам
    @Override
    public List<Ship> getSatisfiedShips(String name, String planet, Long after, Long before, ShipType shipType,
                                        Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                                        Double minRating, Double maxRating, Boolean isUsed,
                                        ShipOrder order, Integer pageNumber, Integer pageSize) {
        //переприсваивание значений если что то не получено
        if ( name == null )
            name = "";
        if ( planet == null )
            planet = "";

        Date start;
        if ( after == null )
            start = new Date(
                    new GregorianCalendar(2800, 0, 0).getTimeInMillis());
        else start = new Date(after);

        Date finish;
        if ( before == null )
            finish = new Date(
                    new GregorianCalendar(3020, 0, 1).getTimeInMillis());
        else finish = new Date(before);

        if ( minSpeed == null )
            minSpeed = 0.0;

        if ( maxSpeed == null )
            maxSpeed = 1.0;

        if ( minCrewSize == null )
            minCrewSize = 0;

        if ( maxCrewSize == null )
            maxCrewSize = 10000;

        if ( minRating == null )
            minRating = 0.0;

        if ( maxRating == null )
            maxRating = 80.0;


        Pageable pageable = pageNumber != null && pageSize != null && order != null ?
                PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName())) : null;
        List<Ship> shipList;
        if ( isUsed == null && shipType == null )
            shipList = getShipsWithNullShipTypeAndIsUsed(name, planet, start, finish, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating, pageable);
        else if ( isUsed == null )
            shipList = getShipsWithNullIsUsed(name, planet, start, finish, minSpeed, maxSpeed,
                    minCrewSize, maxCrewSize, minRating, maxRating, shipType, pageable);
        else if ( shipType == null )
            shipList = getShipsWithNullShipType(name, planet, start, finish, minSpeed, maxSpeed,
                    minCrewSize, maxCrewSize, minRating, maxRating, isUsed, pageable);
        else shipList = getShipsWithCompleteRequest(name, planet, start, finish, minSpeed, maxSpeed,
                    minCrewSize, maxCrewSize, minRating, maxRating, isUsed, shipType, pageable);

        return shipList;
    }


    @Override
    public List<Ship> getShipsWithNullShipTypeAndIsUsed(String name, String planet, Date after, Date before,
                                                        Double minSpeed, Double maxSpeed, Integer minCrewSize,
                                                        Integer maxCrewSize, Double minRating, Double maxRating,
                                                        Pageable pageable) {
        return pageable != null ? shipRepository.findShipsByNameContainingAndPlanetContainingAndProdDateBetweenAndSpeedBetweenAndCrewSizeBetweenAndRatingBetween(
                name, planet, after, before, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating,
                pageable) : shipRepository.findShipsByNameContainingAndPlanetContainingAndProdDateBetweenAndSpeedBetweenAndCrewSizeBetweenAndRatingBetween(
                name, planet, after, before, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
    }

    @Override
    public List<Ship> getShipsWithNullShipType(String name, String planet, Date after, Date before,
                                               Double minSpeed, Double maxSpeed, Integer minCrewSize,
                                               Integer maxCrewSize, Double minRating, Double maxRating,
                                               Boolean isUsed, Pageable pageable) {
        return pageable != null ? shipRepository.findShipsByNameContainingAndPlanetContainingAndProdDateBetweenAndSpeedBetweenAndCrewSizeBetweenAndRatingBetweenAndIsUsed(
                name, planet, after, before, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating,
                isUsed, pageable) : shipRepository.findShipsByNameContainingAndPlanetContainingAndProdDateBetweenAndSpeedBetweenAndCrewSizeBetweenAndRatingBetweenAndIsUsed(
                name, planet, after, before, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating,
                isUsed);
    }

    @Override
    public List<Ship> getShipsWithNullIsUsed(String name, String planet, Date after, Date before,
                                             Double minSpeed, Double maxSpeed, Integer minCrewSize,
                                             Integer maxCrewSize, Double minRating, Double maxRating,
                                             ShipType shipType, Pageable pageable) {
        return pageable != null ? shipRepository.findShipsByNameContainingAndPlanetContainingAndProdDateBetweenAndSpeedBetweenAndCrewSizeBetweenAndRatingBetweenAndShipType(
                name, planet, after, before, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating,
                shipType, pageable) : shipRepository.findShipsByNameContainingAndPlanetContainingAndProdDateBetweenAndSpeedBetweenAndCrewSizeBetweenAndRatingBetweenAndShipType(
                name, planet, after, before, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating,
                shipType);
    }

    @Override
    public List<Ship> getShipsWithCompleteRequest(String name, String planet, Date after, Date before,
                                                  Double minSpeed, Double maxSpeed, Integer minCrewSize,
                                                  Integer maxCrewSize, Double minRating, Double maxRating,
                                                  Boolean isUsed, ShipType shipType, Pageable pageable) {

        return pageable != null ? shipRepository.findShipsByNameContainingAndPlanetContainingAndProdDateBetweenAndSpeedBetweenAndCrewSizeBetweenAndRatingBetweenAndIsUsedAndShipType(
                name, planet, after, before, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating,
                isUsed, shipType, pageable) : shipRepository.findShipsByNameContainingAndPlanetContainingAndProdDateBetweenAndSpeedBetweenAndCrewSizeBetweenAndRatingBetweenAndIsUsedAndShipType(
                name, planet, after, before, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating,
                isUsed, shipType);
    }






    @Override
    public Integer numOfSatisfiedShips(String name, String planet, Long after, Long before, ShipType shipType,
                                       Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                                       Double minRating, Double maxRating, Boolean isUsed) {
        //переприсваивание значений если что то не получено
        if ( planet == null )
            planet = "";

        if ( name == null )
            name = "";

        Date start;
        if ( after == null )
            start = new Date(
                    new GregorianCalendar(2800, 0, 0).getTimeInMillis());
        else start = new Date(after);

        Date finish;
        if ( before == null )
            finish = new Date(
                    new GregorianCalendar(3020, 0, 1).getTimeInMillis());
        else finish = new Date(before);

        if ( minSpeed == null )
            minSpeed = 0.0;

        if ( maxSpeed == null )
            maxSpeed = 1.0;

        if ( minCrewSize == null )
            minCrewSize = 0;

        if ( maxCrewSize == null )
            maxCrewSize = 10000;

        if ( minRating == null )
            minRating = 0.0;

        if ( maxRating == null )
            maxRating = 80.0;

        List<Ship> shipList;
        if ( isUsed == null && shipType == null )
            shipList = getShipsWithNullShipTypeAndIsUsed(name, planet, start, finish, minSpeed, maxSpeed,
                    minCrewSize, maxCrewSize, minRating, maxRating, null);
        else if ( isUsed == null )
            shipList = getShipsWithNullIsUsed(name, planet, start, finish, minSpeed, maxSpeed,
                    minCrewSize, maxCrewSize, minRating, maxRating, shipType, null);
        else if ( shipType == null )
            shipList = getShipsWithNullShipType(name, planet, start, finish, minSpeed, maxSpeed,
                    minCrewSize, maxCrewSize, minRating, maxRating, isUsed, null);
        else shipList = getShipsWithCompleteRequest(name, planet, start, finish, minSpeed, maxSpeed,
                    minCrewSize, maxCrewSize, minRating, maxRating, isUsed, shipType, null);

        return shipList.size();
    }

}
