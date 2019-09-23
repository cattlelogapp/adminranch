package io.crf.cattlelog.aminranch.service;

import io.crf.cattlelog.aminranch.domain.Rancher;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Rancher}.
 */
public interface RancherService {

    /**
     * Save a rancher.
     *
     * @param rancher the entity to save.
     * @return the persisted entity.
     */
    Rancher save(Rancher rancher);

    /**
     * Get all the ranchers.
     *
     * @return the list of entities.
     */
    List<Rancher> findAll();


    /**
     * Get the "id" rancher.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Rancher> findOne(Long id);

    /**
     * 
     * @param userId
     * @return
     */
    Optional<Rancher> findOneByUserId(Long userId);

    /**
     * Delete the "id" rancher.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    
}
