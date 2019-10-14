package io.crf.cattlelog.aminranch.service.impl;

import io.crf.cattlelog.aminranch.service.ConsultantService;
import io.crf.cattlelog.aminranch.domain.Consultant;
import io.crf.cattlelog.aminranch.repository.ConsultantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Consultant}.
 */
@Service
@Transactional
public class ConsultantServiceImpl implements ConsultantService {

    private final Logger log = LoggerFactory.getLogger(ConsultantServiceImpl.class);

    private final ConsultantRepository consultantRepository;

    public ConsultantServiceImpl(ConsultantRepository consultantRepository) {
        this.consultantRepository = consultantRepository;
    }

    /**
     * Save a consultant.
     *
     * @param consultant the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Consultant save(Consultant consultant) {
        log.debug("Request to save Consultant : {}", consultant);
        return consultantRepository.save(consultant);
    }

    /**
     * Get all the consultants.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Consultant> findAll() {
        log.debug("Request to get all Consultants");
//        return consultantRepository.findAllWithEagerRelationships();
        return consultantRepository.findAll();
    }

    /**
     * Get all the consultants with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Consultant> findAllWithEagerRelationships(Pageable pageable) {
//        return consultantRepository.findAllWithEagerRelationships(pageable);
    	return consultantRepository.findAll(pageable);
    }
    

    /**
     * Get one consultant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
     @Override
    @Transactional(readOnly = true)
    public Optional<Consultant> findOne(Long id) {
        log.debug("Request to get Consultant : {}", id);
//        return consultantRepository.findOneWithEagerRelationships(id);
        return consultantRepository.findById(id);
    }
     
     @Override
     @Transactional(readOnly = true)
     public Optional<Consultant> findOneByUserId(Long userId) {
         log.debug("Request to get Consultant : {}", userId);
//         return consultantRepository.findOneByUserIdWithEagerRelationships(userId.intValue());
         return consultantRepository.findOneByUserId(userId);
     }

    /**
     * Delete the consultant by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Consultant : {}", id);
        consultantRepository.deleteById(id);
    }
}
