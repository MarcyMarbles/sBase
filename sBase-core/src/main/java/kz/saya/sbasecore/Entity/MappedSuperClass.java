package kz.saya.sbasecore.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class MappedSuperClass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    protected UUID id;
    protected OffsetDateTime created_ts = OffsetDateTime.now();
    protected OffsetDateTime updated_ts;
    protected OffsetDateTime deleted_ts;
    protected OffsetDateTime start_date_ts = OffsetDateTime.now();
    protected OffsetDateTime end_date_ts = OffsetDateTime.parse("9999-12-31T00:00:00Z");
}
