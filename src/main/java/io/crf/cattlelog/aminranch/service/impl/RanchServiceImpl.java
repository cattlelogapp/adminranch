package io.crf.cattlelog.aminranch.service.impl;

import io.crf.cattlelog.aminranch.service.RanchService;
import io.crf.cattlelog.aminranch.service.dto.RanchAccessDTO;
import io.crf.cattlelog.aminranch.service.dto.RanchWithAccessDTO;
import io.crf.cattlelog.aminranch.domain.Ranch;
import io.crf.cattlelog.aminranch.repository.RanchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

/**
 * Service Implementation for managing {@link Ranch}.
 */
@Service
@Transactional
public class RanchServiceImpl implements RanchService {

    private final Logger log = LoggerFactory.getLogger(RanchServiceImpl.class);

    private final RanchRepository ranchRepository;

    public RanchServiceImpl(RanchRepository ranchRepository) {
        this.ranchRepository = ranchRepository;
    }

    /**
     * Save a ranch.
     *
     * @param ranch the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Ranch save(Ranch ranch) {
        log.debug("Request to save Ranch : {}", ranch);
        return ranchRepository.save(ranch);
    }

    /**
     * Get all the ranches.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ranch> findAll() {
        log.debug("Request to get all Ranches");
        return ranchRepository.findAll();
    }
    
    /**
     * 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ranch> findAllByRancherId(Long id) {
        log.debug("Request to get all Ranches by Rancher Id");
        return ranchRepository.findAllByRancherId(id);
    }
    
    /**
     * 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ranch> findAllByUserId(Long id) {
        log.debug("Request to get all Ranches by User Id");
        return ranchRepository.findAllByUserId(id.intValue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RanchWithAccessDTO> findAllForConsultantWithAccessByUserId(Long id) {
        log.debug("Request to get all Ranches by User Id for a consultant");
        return ranchRepository.findAllForConsultantWithAccessByUserId(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RanchWithAccessDTO> findAllForRancherWithAccessByUserId(Long id) {
        log.debug("Request to get all Ranches by User Id for a rancher");
        return ranchRepository.findAllForRancherWithAccessByUserId(id);
    }

    /**
     * Get one ranch by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Ranch> findOne(Long id) {
        log.debug("Request to get Ranch : {}", id);
        return ranchRepository.findById(id);
    }
    

    /**
     * Delete the ranch by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ranch : {}", id);
        ranchRepository.deleteById(id);
    }

	@Override
	public void registerAccess(@Valid RanchAccessDTO requestAccess) {
		ranchRepository.registerAccess(requestAccess.getRanchId(), requestAccess.getConsultantId());
		
	}

	

	@Override
	public void removeAccess(@Valid RanchAccessDTO requestAccess) {
		Optional<Ranch> ranch = ranchRepository.findById(Long.valueOf(requestAccess.getRanchId()));
		if (ranch != null) {
			ranchRepository.removeAccess(requestAccess.getRanchId(), requestAccess.getConsultantId());
		}
		
	}

	@Override
	public boolean hasAccess(@Valid RanchAccessDTO requestAccess) {
		Object access = ranchRepository.findAccess(requestAccess.getRanchId(), requestAccess.getConsultantId());
		if (access != null) {
			return true;
		}
		;

		return false;
	}
}
