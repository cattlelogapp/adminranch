package io.crf.cattlelog.aminranch.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Consultant.
 */
@Entity
@Table(name = "consultant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Consultant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "licence")
    private String licence;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "consultant_ranch",
               joinColumns = @JoinColumn(name = "consultant_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "ranch_id", referencedColumnName = "id"))
    private Set<Ranch> ranches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Consultant userId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLicence() {
        return licence;
    }

    public Consultant licence(String licence) {
        this.licence = licence;
        return this;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public Set<Ranch> getRanches() {
        return ranches;
    }

    public Consultant ranches(Set<Ranch> ranches) {
        this.ranches = ranches;
        return this;
    }

    public Consultant addRanch(Ranch ranch) {
        this.ranches.add(ranch);
        ranch.getConsultants().add(this);
        return this;
    }

    public Consultant removeRanch(Ranch ranch) {
        this.ranches.remove(ranch);
        ranch.getConsultants().remove(this);
        return this;
    }

    public void setRanches(Set<Ranch> ranches) {
        this.ranches = ranches;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Consultant)) {
            return false;
        }
        return id != null && id.equals(((Consultant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Consultant{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", licence='" + getLicence() + "'" +
            "}";
    }
}
