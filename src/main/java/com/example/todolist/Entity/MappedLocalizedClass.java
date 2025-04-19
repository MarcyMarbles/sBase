package com.example.todolist.Entity;

import com.example.todolist.Entity.NotPersistent.Langs;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class MappedLocalizedClass extends MappedSuperClass {
    // Will have all localization fields for all languages
    public String langValue1; // ru - Задание
    public String langValue2; // en - Task
    public String description1; // ru - Задание
    public String description2; // en - Task

    public String getLangValue(Langs langs) // Based on the language of the user
    {
        return switch (langs) {
            case RU -> langValue1;
            case EN -> langValue2;
            default -> throw new IllegalStateException("Unexpected value: " + langs);
        };
    }

    public String getDescription(Langs langs) // Based on the language of the user
    {
        return switch (langs) {
            case RU -> description1;
            case EN -> description2;
            default -> throw new IllegalStateException("Unexpected value: " + langs);
        };
    }

    public void setLangValue(Langs langs, String value) {
        switch (langs) {
            case RU -> langValue1 = value;
            case EN -> langValue2 = value;
            default -> throw new IllegalStateException("Unexpected value: " + langs);
        }
    }

    public void setDescription(Langs langs, String value) {
        switch (langs) {
            case RU -> description1 = value;
            case EN -> description2 = value;
            default -> throw new IllegalStateException("Unexpected value: " + langs);
        }
    }
}
