package io.crf.cattlelog.aminranch.service;

import io.crf.cattlelog.aminranch.domain.Ranch;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Ranch}.
 */
public interface RanchService {

    /**
     * Save a ranch.
     *
     * @param ranch the entity to save.
     * @return the persisted entity.
     */
    Ranch save(Ranch ranch);

    /**
     * Get all the ranches.
     *
     * @return the list of entities.
     */
    List<Ranch> findAll();


    /**
     * Get the "id" ranch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ranch> findOne(Long id);

    /**
     * Delete the "id" ranch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
