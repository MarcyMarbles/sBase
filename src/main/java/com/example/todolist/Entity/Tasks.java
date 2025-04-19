package com.example.todolist.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Tasks extends MappedLocalizedClass {
    private String name;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Priority priority;

    private Date deadline;

    @ManyToOne
    @JsonIgnore
    private Person user;

    @ManyToMany(mappedBy = "tasks")
    List<Person> assignees = new ArrayList<>();

    public boolean isExpired() {
        return deadline != null && deadline.before(new Date());
    }

    public boolean isCompleted() {
        return this.getStart_date_ts().isAfter(OffsetDateTime.now()) && this.getEnd_date_ts().isBefore(OffsetDateTime.now());
    }


}
