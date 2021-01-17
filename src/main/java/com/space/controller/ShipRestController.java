package com.space.controller;


import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class ShipRestController {

    @Autowired
    private final ShipService shipService;

    public ShipRestController(ShipService shipService) {
        this.shipService = shipService;
    }

    //удаление готово
    @DeleteMapping(value = "/ships/{id}")
    public ResponseEntity<Ship> deleteShip(@PathVariable Long id)  {
        if ( id == null || id <= 0 )
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean isDeleted = shipService.deleteShip(id);

        return !isDeleted ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(HttpStatus.OK);
    }

    //обновление готово - может быть проблема с датой и вообще хз про requestBody
    @PostMapping(value = "/ships/{id}")
    public ResponseEntity<Ship> updateShip(@PathVariable Long id, @RequestBody(required = false) Ship ship )  {
        if ( id == null || id <= 0 )
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship updatedShip = shipService.update(id, ship);

        return updatedShip != null ? new ResponseEntity<>(updatedShip, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    //достать корабль по id готово
    @GetMapping(value = "/ships/{id}")
    public ResponseEntity<Ship> getShipById(@PathVariable Long id)  {
        if ( id == null || id <= 0 )
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship ship = shipService.getShipById(id);

        return ship == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(ship, HttpStatus.OK);
    }

    //сохранить корабль - см. обновление
    @PostMapping(value = "/ships")
    public ResponseEntity<Ship> createShip(@RequestBody(required = false) Ship ship)  {
        if ( ship == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship shipCreated = shipService.create(ship);

        return shipCreated == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(shipCreated, HttpStatus.OK);
    }

    //получить список пользователям согласно фильтрам
    /*
    after=Long
before=Long
isUsed=Boolean
minSpeed=Double
maxSpeed=Double
minCrewSize=Integer
maxCrewSize=Integer
minRating=Double
maxRating=Double
     */
    @GetMapping("/ships")
    public ResponseEntity<List<Ship>> getShipsList( @RequestParam(required = false) String name, @RequestParam(required = false) String planet,
                                             @RequestParam(required = false) ShipType shipType, @RequestParam(required = false) Long after,
                                             @RequestParam(required = false) Long before, @RequestParam(required = false) Boolean isUsed,
                                             @RequestParam(required = false) Double minSpeed, @RequestParam(required = false) Double maxSpeed,
                                             @RequestParam(required = false) Integer minCrewSize, @RequestParam(required = false) Integer maxCrewSize,
                                             @RequestParam(required = false) Double minRating, @RequestParam(required = false) Double maxRating,
                                             @RequestParam(required = false, defaultValue = "ID") ShipOrder order,
                                             @RequestParam(required = false, defaultValue =  "0") Integer pageNumber,
                                             @RequestParam(required = false, defaultValue =  "3") Integer pageSize )  {
        List<Ship> getShips = shipService.getSatisfiedShips(name, planet, after, before, shipType, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating, isUsed, order, pageNumber, pageSize);

        return new ResponseEntity<>(getShips, HttpStatus.OK);
    }

    @GetMapping("/ships/count")
    public ResponseEntity<Integer> getShipNumber( @RequestParam(required = false) String name, @RequestParam(required = false) String planet,
                                                  @RequestParam(required = false) ShipType shipType, @RequestParam(required = false) Long after,
                                                  @RequestParam(required = false) Long before, @RequestParam(required = false) Boolean isUsed,
                                                  @RequestParam(required = false) Double minSpeed, @RequestParam(required = false) Double maxSpeed,
                                                  @RequestParam(required = false) Integer minCrewSize, @RequestParam(required = false) Integer maxCrewSize,
                                                  @RequestParam(required = false) Double minRating, @RequestParam(required = false) Double maxRating )  {

        List<Ship> getShips = shipService.getSatisfiedShips(name, planet, after, before, shipType, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating, isUsed, null, null, null);

        return new ResponseEntity<>(getShips.size(), HttpStatus.OK);
    }

}

