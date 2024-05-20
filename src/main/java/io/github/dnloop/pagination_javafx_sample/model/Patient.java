package io.github.dnloop.pagination_javafx_sample.model;

import jakarta.persistence.*;


@Entity
public class Patient {
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "patient_sequence"
    )
    @SequenceGenerator(name = "patient_sequence",
                       sequenceName = "PATIENT_SEQUENCE",
                       allocationSize = 5
    )
    @Id
    private Integer id;

    @Basic
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
