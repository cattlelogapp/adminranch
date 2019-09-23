package io.crf.cattlelog.aminranch.service.impl;

import io.crf.cattlelog.aminranch.service.RancherService;
import io.crf.cattlelog.aminranch.domain.Rancher;
import io.crf.cattlelog.aminranch.repository.RancherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Rancher}.
 */
@Service
@Transactional
public class RancherServiceImpl implements RancherService {

    private final Logger log = LoggerFactory.getLogger(RancherServiceImpl.class);

    private final RancherRepository rancherRepository;

    public RancherServiceImpl(RancherRepository rancherRepository) {
        this.rancherRepository = rancherRepository;
    }

    /**
     * Save a rancher.
     *
     * @param rancher the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Rancher save(Rancher rancher) {
        log.debug("Request to save Rancher : {}", rancher);
        return rancherRepository.save(rancher);
    }

    /**
     * Get all the ranchers.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Rancher> findAll() {
        log.debug("Request to get all Ranchers");
        return rancherRepository.findAll();
    }


    /**
     * Get one rancher by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Rancher> findOne(Long id) {
        log.debug("Request to get Rancher : {}", id);
        return rancherRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Rancher> findOneByUserId(Long userId) {
        log.debug("Request to get Rancher by user id: {}", userId);
        return rancherRepository.findByUserId(userId.intValue());
    }

    /**
     * Delete the rancher by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rancher : {}", id);
        rancherRepository.deleteById(id);
    }
}
