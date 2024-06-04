package org.teamchallenge.bookshop.enums;


import lombok.Getter;

@Getter
public enum Category {
    ARCHITECTURE(1, "Архітектура"),
    BUSINESS_MARKETING(2, "Бізнес та маркетинг"),
    DESIGN(3, "Дизайн"),
    CULINARY(4, "Кулінарія"),
    CINEMATOGRAPHY(5, "Кінематографія"),
    ART(6, "Мистецтво"),
    IT_PROGRAMMING(7, "ІТ та програмування"),
    FASHION_AND_BEAUTY(8, "Мода та краса"),
    PHILOSOPHY(9, "Філософія"),
    PSYCHOLOGY(10, "Психологія"),
    COMICS(11, "Комікси"),
    LIFESTYLE(12, "Стиль життя"),
    UKRAINIAN_AUTHORS(13, "Українські автори");

    private final int id;
    private final String name;

    Category(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
