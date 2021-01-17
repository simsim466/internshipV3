package com.space.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Это все полнейший ебаный пиздец, я только учусь. Здесь были геттеры/сеттеры. Теперь их нет, это очень хорошо.
    //Проблемы с: датой - полная пизда, сохранением рейтинга - высчитывает правильно но несохраняет
    //дату кое где игнорил - надо вернуть
}
