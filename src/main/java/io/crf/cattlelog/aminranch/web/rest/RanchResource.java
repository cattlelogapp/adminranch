package io.crf.cattlelog.aminranch.web.rest;

import io.crf.cattlelog.aminranch.domain.Ranch;
import io.crf.cattlelog.aminranch.service.RanchService;
import io.crf.cattlelog.aminranch.service.dto.RanchAccessDTO;
import io.crf.cattlelog.aminranch.service.dto.RanchWithAccessDTO;
import io.crf.cattlelog.aminranch.web.rest.errors.BadRequestAlertException;
import io.crf.cattlelog.aminranch.web.rest.errors.ExistingAccessException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.crf.cattlelog.aminranch.domain.Ranch}.
 */
@RestController
@RequestMapping("/api")
public class RanchResource {

    private final Logger log = LoggerFactory.getLogger(RanchResource.class);

    private static final String ENTITY_NAME = "adminranchRanch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RanchService ranchService;

    public RanchResource(RanchService ranchService) {
        this.ranchService = ranchService;
    }

    /**
     * {@code POST  /ranches} : Create a new ranch.
     *
     * @param ranch the ranch to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ranch, or with status {@code 400 (Bad Request)} if the ranch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ranches")
    public ResponseEntity<Ranch> createRanch(@Valid @RequestBody Ranch ranch) throws URISyntaxException {
        log.debug("REST request to save Ranch : {}", ranch);
        if (ranch.getId() != null) {
            throw new BadRequestAlertException("A new ranch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ranch result = ranchService.save(ranch);
        return ResponseEntity.created(new URI("/api/ranches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ranches} : Updates an existing ranch.
     *
     * @param ranch the ranch to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ranch,
     * or with status {@code 400 (Bad Request)} if the ranch is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ranch couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ranches")
    public ResponseEntity<Ranch> updateRanch(@Valid @RequestBody Ranch ranch) throws URISyntaxException {
        log.debug("REST request to update Ranch : {}", ranch);
        if (ranch.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ranch result = ranchService.save(ranch);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ranch.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ranches} : get all the ranches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ranches in body.
     */
    @GetMapping("/ranches")
    public List<Ranch> getAllRanches() {
        log.debug("REST request to get all Ranches");
        return ranchService.findAll();
    }
    
    /**
     * 
     * @return
     */
    @GetMapping("/ranches/rancher/{id}")
    public List<Ranch> getAllRanchesByRancherId(@PathVariable Long id) {
        log.debug("REST request to get all Ranches by Rancher Id");
        return ranchService.findAllByRancherId(id);
    }
    
    @GetMapping("/ranches/user/{id}")
    public List<Ranch> getAllRanchesByUserId(@PathVariable Long id) {
        log.debug("REST request to get all Ranches by User Id");
        return ranchService.findAllByUserId(id);
    }
    
//    @GetMapping("/ranches/access/user/{id}")
//    public List<Ranch> getAllRanchesForAccessByUserId(@PathVariable Long id) {
//        log.debug("REST request to get all Ranches by User Id");
//        return ranchService.findAllForAccessByUserId(id);
//    }
    
    @GetMapping("/ranches/access")
    public List<RanchWithAccessDTO> getAllRanchesForConsultantWithAccess() {
        log.debug("REST request to get all Ranches by with Access");
        return ranchService.findAllForConsultantWithAccess();
    }
        
	@GetMapping("/ranches/access/ranch/{ranchName}")
	public List<RanchWithAccessDTO> getAllRanchesForConsultantWithAccessByRanchName(@PathVariable String ranchName) {
		log.debug("REST request to get all Ranches by User Id with Access");
		return ranchService.findAllForConsultantWithAccessByRanchName(ranchName);
    }
    
    @GetMapping("/ranches/access/rancher/user/{id}")
    public List<RanchWithAccessDTO> getAllRanchesForRancherWithAccessByUserId(@PathVariable Long id) {
        log.debug("REST request to get all Ranches by User Id with Access");
        return ranchService.findAllForRancherWithAccessByUserId(id);
    }
    
    @PostMapping("/ranches/access/request")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void requestAccess(@Valid @RequestBody RanchAccessDTO requestAccess) throws URISyntaxException{
    	if (ranchService.hasAccess(requestAccess)) {
            throw new ExistingAccessException();
        }
        ranchService.registerAccess(requestAccess);
//        mailService.sendActivationEmail(user);
    }
    
    @PostMapping("/ranches/access/grant")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void grantAccess(@Valid @RequestBody RanchAccessDTO grantAccess) throws URISyntaxException{
        ranchService.grantAccess(grantAccess);
//        mailService.sendActivationEmail(user);
    }
    
    /**
     * {@code DELETE  /ranches/:id} : delete the "id" ranch.
     *
     * @param id the id of the ranch to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ranches/access/{ranchId}/{consultantId}")
    public ResponseEntity<Void> deleteRanchAccess(@PathVariable Long ranchId, @PathVariable Long consultantId) {
        log.debug("REST request to delete Access for ranch {} and consultant {}", ranchId, consultantId);
        ranchService.deleteAccess(ranchId, consultantId);
        return ResponseEntity.noContent().build();
    }


    /**
     * {@code GET  /ranches/:id} : get the "id" ranch.
     *
     * @param id the id of the ranch to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ranch, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ranches/{id}")
    public ResponseEntity<Ranch> getRanch(@PathVariable Long id) {
        log.debug("REST request to get Ranch : {}", id);
        Optional<Ranch> ranch = ranchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ranch);
    }

    /**
     * {@code DELETE  /ranches/:id} : delete the "id" ranch.
     *
     * @param id the id of the ranch to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ranches/{id}")
    public ResponseEntity<Void> deleteRanch(@PathVariable Long id) {
        log.debug("REST request to delete Ranch : {}", id);
        ranchService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
