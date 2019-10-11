package io.crf.cattlelog.aminranch.service;

import io.crf.cattlelog.aminranch.domain.Ranch;
import io.crf.cattlelog.aminranch.service.dto.RanchAccessDTO;
import io.crf.cattlelog.aminranch.service.dto.RanchWithAccessDTO;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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
     * 
     * @param id
     * @return
     */
    List<Ranch> findAllByRancherId(Long id);
    
    /**
     * 
     * @param id
     * @return
     */
    List<Ranch> findAllByUserId(Long id);
    
    /**
     * 
     * @param id
     * @return
     */
    List<RanchWithAccessDTO> findAllForConsultantWithAccessByUserId(Long id);
    
    List<RanchWithAccessDTO> findAllForRancherWithAccessByUserId(Long id);


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
    
    void registerAccess(@Valid RanchAccessDTO requestAccess);
    
    void removeAccess(@Valid RanchAccessDTO requestAccess);

	boolean hasAccess(@Valid RanchAccessDTO requestAccess);

	
}
