package io.crf.cattlelog.aminranch.service;

import io.crf.cattlelog.aminranch.domain.Consultant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Consultant}.
 */
public interface ConsultantService {

    /**
     * Save a consultant.
     *
     * @param consultant the entity to save.
     * @return the persisted entity.
     */
    Consultant save(Consultant consultant);

    /**
     * Get all the consultants.
     *
     * @return the list of entities.
     */
    List<Consultant> findAll();

    /**
     * Get all the consultants with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<Consultant> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" consultant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Consultant> findOne(Long id);

    /**
     * Delete the "id" consultant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
