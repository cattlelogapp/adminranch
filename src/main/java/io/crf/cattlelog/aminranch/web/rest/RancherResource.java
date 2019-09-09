package io.crf.cattlelog.aminranch.web.rest;

import io.crf.cattlelog.aminranch.domain.Rancher;
import io.crf.cattlelog.aminranch.service.RancherService;
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
 * REST controller for managing {@link io.crf.cattlelog.aminranch.domain.Rancher}.
 */
@RestController
@RequestMapping("/api")
public class RancherResource {

    private final Logger log = LoggerFactory.getLogger(RancherResource.class);

    private static final String ENTITY_NAME = "adminranchRancher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RancherService rancherService;

    public RancherResource(RancherService rancherService) {
        this.rancherService = rancherService;
    }

    /**
     * {@code POST  /ranchers} : Create a new rancher.
     *
     * @param rancher the rancher to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rancher, or with status {@code 400 (Bad Request)} if the rancher has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ranchers")
    public ResponseEntity<Rancher> createRancher(@Valid @RequestBody Rancher rancher) throws URISyntaxException {
        log.debug("REST request to save Rancher : {}", rancher);
        if (rancher.getId() != null) {
            throw new BadRequestAlertException("A new rancher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rancher result = rancherService.save(rancher);
        return ResponseEntity.created(new URI("/api/ranchers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ranchers} : Updates an existing rancher.
     *
     * @param rancher the rancher to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rancher,
     * or with status {@code 400 (Bad Request)} if the rancher is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rancher couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ranchers")
    public ResponseEntity<Rancher> updateRancher(@Valid @RequestBody Rancher rancher) throws URISyntaxException {
        log.debug("REST request to update Rancher : {}", rancher);
        if (rancher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Rancher result = rancherService.save(rancher);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rancher.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ranchers} : get all the ranchers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ranchers in body.
     */
    @GetMapping("/ranchers")
    public List<Rancher> getAllRanchers() {
        log.debug("REST request to get all Ranchers");
        return rancherService.findAll();
    }

    /**
     * {@code GET  /ranchers/:id} : get the "id" rancher.
     *
     * @param id the id of the rancher to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rancher, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ranchers/{id}")
    public ResponseEntity<Rancher> getRancher(@PathVariable Long id) {
        log.debug("REST request to get Rancher : {}", id);
        Optional<Rancher> rancher = rancherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rancher);
    }

    /**
     * {@code DELETE  /ranchers/:id} : delete the "id" rancher.
     *
     * @param id the id of the rancher to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ranchers/{id}")
    public ResponseEntity<Void> deleteRancher(@PathVariable Long id) {
        log.debug("REST request to delete Rancher : {}", id);
        rancherService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
