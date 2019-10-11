package io.crf.cattlelog.aminranch.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Ranch.
 */
@Entity
@Table(name = "ranch")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ranch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location")
    private String location;

    @ManyToOne
    @JsonIgnoreProperties("ranches")
    private Rancher rancher;

//    @ManyToMany(mappedBy = "ranches")
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//    @JsonIgnore
//    private Set<Consultant> consultants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Ranch name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public Ranch location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Rancher getRancher() {
        return rancher;
    }

    public Ranch rancher(Rancher rancher) {
        this.rancher = rancher;
        return this;
    }

    public void setRancher(Rancher rancher) {
        this.rancher = rancher;
    }

//    public Set<Consultant> getConsultants() {
//        return consultants;
//    }
//
//    public Ranch consultants(Set<Consultant> consultants) {
//        this.consultants = consultants;
//        return this;
//    }
//
//    public Ranch addConsultant(Consultant consultant) {
//        this.consultants.add(consultant);
//        consultant.getRanches().add(this);
//        return this;
//    }
//
//    public Ranch removeConsultant(Consultant consultant) {
//        this.consultants.remove(consultant);
//        consultant.getRanches().remove(this);
//        return this;
//    }
//
//    public void setConsultants(Set<Consultant> consultants) {
//        this.consultants = consultants;
//    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ranch)) {
            return false;
        }
        return id != null && id.equals(((Ranch) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Ranch{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
