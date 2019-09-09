package io.crf.cattlelog.aminranch.web.rest;

import io.crf.cattlelog.aminranch.AdminranchApp;
import io.crf.cattlelog.aminranch.domain.Ranch;
import io.crf.cattlelog.aminranch.repository.RanchRepository;
import io.crf.cattlelog.aminranch.service.RanchService;
import io.crf.cattlelog.aminranch.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static io.crf.cattlelog.aminranch.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link RanchResource} REST controller.
 */
@SpringBootTest(classes = AdminranchApp.class)
public class RanchResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    @Autowired
    private RanchRepository ranchRepository;

    @Autowired
    private RanchService ranchService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restRanchMockMvc;

    private Ranch ranch;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RanchResource ranchResource = new RanchResource(ranchService);
        this.restRanchMockMvc = MockMvcBuilders.standaloneSetup(ranchResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ranch createEntity(EntityManager em) {
        Ranch ranch = new Ranch()
            .name(DEFAULT_NAME)
            .location(DEFAULT_LOCATION);
        return ranch;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ranch createUpdatedEntity(EntityManager em) {
        Ranch ranch = new Ranch()
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION);
        return ranch;
    }

    @BeforeEach
    public void initTest() {
        ranch = createEntity(em);
    }

    @Test
    @Transactional
    public void createRanch() throws Exception {
        int databaseSizeBeforeCreate = ranchRepository.findAll().size();

        // Create the Ranch
        restRanchMockMvc.perform(post("/api/ranches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ranch)))
            .andExpect(status().isCreated());

        // Validate the Ranch in the database
        List<Ranch> ranchList = ranchRepository.findAll();
        assertThat(ranchList).hasSize(databaseSizeBeforeCreate + 1);
        Ranch testRanch = ranchList.get(ranchList.size() - 1);
        assertThat(testRanch.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRanch.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    public void createRanchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ranchRepository.findAll().size();

        // Create the Ranch with an existing ID
        ranch.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRanchMockMvc.perform(post("/api/ranches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ranch)))
            .andExpect(status().isBadRequest());

        // Validate the Ranch in the database
        List<Ranch> ranchList = ranchRepository.findAll();
        assertThat(ranchList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ranchRepository.findAll().size();
        // set the field null
        ranch.setName(null);

        // Create the Ranch, which fails.

        restRanchMockMvc.perform(post("/api/ranches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ranch)))
            .andExpect(status().isBadRequest());

        List<Ranch> ranchList = ranchRepository.findAll();
        assertThat(ranchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRanches() throws Exception {
        // Initialize the database
        ranchRepository.saveAndFlush(ranch);

        // Get all the ranchList
        restRanchMockMvc.perform(get("/api/ranches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ranch.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())));
    }
    
    @Test
    @Transactional
    public void getRanch() throws Exception {
        // Initialize the database
        ranchRepository.saveAndFlush(ranch);

        // Get the ranch
        restRanchMockMvc.perform(get("/api/ranches/{id}", ranch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ranch.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRanch() throws Exception {
        // Get the ranch
        restRanchMockMvc.perform(get("/api/ranches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRanch() throws Exception {
        // Initialize the database
        ranchService.save(ranch);

        int databaseSizeBeforeUpdate = ranchRepository.findAll().size();

        // Update the ranch
        Ranch updatedRanch = ranchRepository.findById(ranch.getId()).get();
        // Disconnect from session so that the updates on updatedRanch are not directly saved in db
        em.detach(updatedRanch);
        updatedRanch
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION);

        restRanchMockMvc.perform(put("/api/ranches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRanch)))
            .andExpect(status().isOk());

        // Validate the Ranch in the database
        List<Ranch> ranchList = ranchRepository.findAll();
        assertThat(ranchList).hasSize(databaseSizeBeforeUpdate);
        Ranch testRanch = ranchList.get(ranchList.size() - 1);
        assertThat(testRanch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRanch.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void updateNonExistingRanch() throws Exception {
        int databaseSizeBeforeUpdate = ranchRepository.findAll().size();

        // Create the Ranch

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRanchMockMvc.perform(put("/api/ranches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ranch)))
            .andExpect(status().isBadRequest());

        // Validate the Ranch in the database
        List<Ranch> ranchList = ranchRepository.findAll();
        assertThat(ranchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRanch() throws Exception {
        // Initialize the database
        ranchService.save(ranch);

        int databaseSizeBeforeDelete = ranchRepository.findAll().size();

        // Delete the ranch
        restRanchMockMvc.perform(delete("/api/ranches/{id}", ranch.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ranch> ranchList = ranchRepository.findAll();
        assertThat(ranchList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ranch.class);
        Ranch ranch1 = new Ranch();
        ranch1.setId(1L);
        Ranch ranch2 = new Ranch();
        ranch2.setId(ranch1.getId());
        assertThat(ranch1).isEqualTo(ranch2);
        ranch2.setId(2L);
        assertThat(ranch1).isNotEqualTo(ranch2);
        ranch1.setId(null);
        assertThat(ranch1).isNotEqualTo(ranch2);
    }
}
