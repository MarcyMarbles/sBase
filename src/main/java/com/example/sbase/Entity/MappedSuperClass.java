package com.example.sbase.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class MappedSuperClass {
    @Id
    @GeneratedValue
    protected int id;
    protected OffsetDateTime created_ts = OffsetDateTime.now();
    protected OffsetDateTime updated_ts;
    protected OffsetDateTime deleted_ts;
    protected OffsetDateTime start_date_ts = OffsetDateTime.now();
    protected OffsetDateTime end_date_ts = OffsetDateTime.parse("9999-12-31T00:00:00Z");


}
