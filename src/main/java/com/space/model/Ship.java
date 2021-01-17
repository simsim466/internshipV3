package com.space.model;

import javax.persistence.*;
import java.sql.Date;
import java.text.SimpleDateFormat;

import java.util.GregorianCalendar;

/*
Long id ID корабля
String name Название корабля (до 50 знаков включительно)
String planet Планета пребывания (до 50 знаков включительно)
ShipType shipType Тип корабля
Date prodDate Дата выпуска.
Диапазон значений года 2800..3019 включительно
Boolean isUsed Использованный / новый
Double speed Максимальная скорость корабля. Диапазон значений
0,01..0,99 включительно. Используй математическое
округление до сотых.
Integer crewSize Количество членов экипажа. Диапазон значений
1..9999 включительно.
Double rating Рейтинг корабля. Используй математическое
округление до сотых.

id       BIGINT(20)  NOT NULL AUTO_INCREMENT,
    name     VARCHAR(50) NULL,
    planet   VARCHAR(50) NULL,
    shipType VARCHAR(9)  NULL,
    prodDate date        NULL,
    isUsed   BIT(1)      NULL,
    speed    DOUBLE      NULL,
    crewSi
   ze INT(4)      NULL,
    rating   DOUBLE      NULL,
    PRIMARY KEY (id)

 */

@Entity
@Table(name = "ship")
public class Ship extends BaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "planet")
    private String planet;
    @Column(name = "shipType")
    @Enumerated(value = EnumType.STRING)
    private ShipType shipType;
    @Column(name = "prodDate")
    private Date prodDate;
    @Column(name = "isUsed")
    private Boolean isUsed;
    @Column(name = "speed")
    private Double speed;
    @Column(name = "crewSize")
    private Integer crewSize;
    @Column(name = "rating")
    private Double rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date proDate) {
        this.prodDate = proDate;
    }

    public Boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = roundToTwoDecimalPlaces(speed);
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    //проверяем верна ли дата
    public boolean isProDateCorrect()   {

        return this.prodDate.after(new GregorianCalendar(2800, 0,1).getTime()) &&
                this.prodDate.before(new GregorianCalendar(3020, 0, 1).getTime());
    }
    //получаем год производства - вспомогательный метод для оценки рейтинга
    private int getYear()    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");

        return Integer.parseInt(df.format(getProdDate()));
    }
    //округляем до сотых - вспомогательный метод для оценки рейтинга и скорости
    private double roundToTwoDecimalPlaces(double number)   {
        int n = (int) ( number * 1000 );
        int remainder = n % 10;
        double result = (double)((int) (number * 100)) / 100;
        if ( remainder > 4 )
            result += 0.01;
        return result;
    }
    //устанавливаем рейтинг
    public void calculateRating() {
        int denominator = 3019 - getYear() + 1;
        double k = isUsed ? 0.5 : 1;
        double numerator = 80 * getSpeed() * k;

        this.rating = roundToTwoDecimalPlaces(numerator / denominator);
    }
    //проверяем не Null все поля
    public boolean isEmpty()    {
        return name == null && planet == null && shipType == null && prodDate == null
                && isUsed == null && speed == null && crewSize == null && rating == null;
    }
}
