package org.teamchallenge.bookshop.enums;


import lombok.Getter;

@Getter
public enum Category {
    ARCHITECTURE("Архітектура"),
    BUSINESS_MARKETING("Бізнес та маркетинг"),
    DESIGN("Дизайн"),
    CULINARY("Кулінарія"),
    CINEMATOGRAPHY("Кінематографія"),
    ART("Мистецтво"),
    IT_PROGRAMMING("ІТ та програмування"),
    FASHION_AND_BEAUTY("Мода та краса"),
    PHILOSOPHY("Філософія"),
    PSYCHOLOGY("Психологія"),
    COMICS("Комікси"),
    LIFESTYLE("Стиль життя"),
    UKRAINIAN_AUTHORS("Українські автори");

    private final String name;

    Category(String name) {
        this.name = name;
    }

}
