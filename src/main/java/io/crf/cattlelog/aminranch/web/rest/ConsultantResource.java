package io.crf.cattlelog.aminranch.web.rest;

import io.crf.cattlelog.aminranch.domain.Consultant;
import io.crf.cattlelog.aminranch.service.ConsultantService;
import io.crf.cattlelog.aminranch.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.crf.cattlelog.aminranch.domain.Consultant}.
 */
@RestController
@RequestMapping("/api")
public class ConsultantResource {

    private final Logger log = LoggerFactory.getLogger(ConsultantResource.class);

    private static final String ENTITY_NAME = "adminranchConsultant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsultantService consultantService;

    public ConsultantResource(ConsultantService consultantService) {
        this.consultantService = consultantService;
    }

    /**
     * {@code POST  /consultants} : Create a new consultant.
     *
     * @param consultant the consultant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consultant, or with status {@code 400 (Bad Request)} if the consultant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/consultants")
    public ResponseEntity<Consultant> createConsultant(@Valid @RequestBody Consultant consultant) throws URISyntaxException {
        log.debug("REST request to save Consultant : {}", consultant);
        if (consultant.getId() != null) {
            throw new BadRequestAlertException("A new consultant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Consultant result = consultantService.save(consultant);
        return ResponseEntity.created(new URI("/api/consultants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /consultants} : Updates an existing consultant.
     *
     * @param consultant the consultant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultant,
     * or with status {@code 400 (Bad Request)} if the consultant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consultant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/consultants")
    public ResponseEntity<Consultant> updateConsultant(@Valid @RequestBody Consultant consultant) throws URISyntaxException {
        log.debug("REST request to update Consultant : {}", consultant);
        if (consultant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Consultant result = consultantService.save(consultant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consultant.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /consultants} : get all the consultants.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of consultants in body.
     */
    @GetMapping("/consultants")
    public List<Consultant> getAllConsultants(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Consultants");
        return consultantService.findAll();
    }

    /**
     * {@code GET  /consultants/:id} : get the "id" consultant.
     *
     * @param id the id of the consultant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consultant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/consultants/{id}")
    public ResponseEntity<Consultant> getConsultant(@PathVariable Long id) {
        log.debug("REST request to get Consultant : {}", id);
        Optional<Consultant> consultant = consultantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consultant);
    }

    /**
     * {@code DELETE  /consultants/:id} : delete the "id" consultant.
     *
     * @param id the id of the consultant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/consultants/{id}")
    public ResponseEntity<Void> deleteConsultant(@PathVariable Long id) {
        log.debug("REST request to delete Consultant : {}", id);
        consultantService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
