package kz.saya.sbase.Entity;

import kz.saya.sbase.Entity.NotPersistent.Langs;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class MappedLocalizedClass extends MappedSuperClass {
    // Will have all localization fields for all languages

    @Column
    public String langValue1;

    @Column
    public String langValue2;

    @Column
    public String description1;

    @Column
    public String description2;

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
