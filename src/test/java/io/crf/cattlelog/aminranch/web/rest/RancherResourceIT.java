package io.crf.cattlelog.aminranch.web.rest;

import io.crf.cattlelog.aminranch.AdminranchApp;
import io.crf.cattlelog.aminranch.domain.Rancher;
import io.crf.cattlelog.aminranch.repository.RancherRepository;
import io.crf.cattlelog.aminranch.service.RancherService;
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
 * Integration tests for the {@Link RancherResource} REST controller.
 */
@SpringBootTest(classes = AdminranchApp.class)
public class RancherResourceIT {

    private static final Long DEFAULT_USER_ID = 1l;
    private static final Long UPDATED_USER_ID = 2l;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MARK = "AAAAAAAAAA";
    private static final String UPDATED_MARK = "BBBBBBBBBB";

    @Autowired
    private RancherRepository rancherRepository;

    @Autowired
    private RancherService rancherService;

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

    private MockMvc restRancherMockMvc;

    private Rancher rancher;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RancherResource rancherResource = new RancherResource(rancherService);
        this.restRancherMockMvc = MockMvcBuilders.standaloneSetup(rancherResource)
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
    public static Rancher createEntity(EntityManager em) {
        Rancher rancher = new Rancher()
            .userId(DEFAULT_USER_ID)
            .code(DEFAULT_CODE)
            .shortCode(DEFAULT_SHORT_CODE)
            .mark(DEFAULT_MARK);
        return rancher;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rancher createUpdatedEntity(EntityManager em) {
        Rancher rancher = new Rancher()
            .userId(UPDATED_USER_ID)
            .code(UPDATED_CODE)
            .shortCode(UPDATED_SHORT_CODE)
            .mark(UPDATED_MARK);
        return rancher;
    }

    @BeforeEach
    public void initTest() {
        rancher = createEntity(em);
    }

    @Test
    @Transactional
    public void createRancher() throws Exception {
        int databaseSizeBeforeCreate = rancherRepository.findAll().size();

        // Create the Rancher
        restRancherMockMvc.perform(post("/api/ranchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rancher)))
            .andExpect(status().isCreated());

        // Validate the Rancher in the database
        List<Rancher> rancherList = rancherRepository.findAll();
        assertThat(rancherList).hasSize(databaseSizeBeforeCreate + 1);
        Rancher testRancher = rancherList.get(rancherList.size() - 1);
        assertThat(testRancher.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testRancher.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testRancher.getShortCode()).isEqualTo(DEFAULT_SHORT_CODE);
        assertThat(testRancher.getMark()).isEqualTo(DEFAULT_MARK);
    }

    @Test
    @Transactional
    public void createRancherWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rancherRepository.findAll().size();

        // Create the Rancher with an existing ID
        rancher.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRancherMockMvc.perform(post("/api/ranchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rancher)))
            .andExpect(status().isBadRequest());

        // Validate the Rancher in the database
        List<Rancher> rancherList = rancherRepository.findAll();
        assertThat(rancherList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = rancherRepository.findAll().size();
        // set the field null
        rancher.setUserId(null);

        // Create the Rancher, which fails.

        restRancherMockMvc.perform(post("/api/ranchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rancher)))
            .andExpect(status().isBadRequest());

        List<Rancher> rancherList = rancherRepository.findAll();
        assertThat(rancherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRanchers() throws Exception {
        // Initialize the database
        rancherRepository.saveAndFlush(rancher);

        // Get all the rancherList
        restRancherMockMvc.perform(get("/api/ranchers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rancher.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].shortCode").value(hasItem(DEFAULT_SHORT_CODE.toString())))
            .andExpect(jsonPath("$.[*].mark").value(hasItem(DEFAULT_MARK.toString())));
    }
    
    @Test
    @Transactional
    public void getRancher() throws Exception {
        // Initialize the database
        rancherRepository.saveAndFlush(rancher);

        // Get the rancher
        restRancherMockMvc.perform(get("/api/ranchers/{id}", rancher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rancher.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.shortCode").value(DEFAULT_SHORT_CODE.toString()))
            .andExpect(jsonPath("$.mark").value(DEFAULT_MARK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRancher() throws Exception {
        // Get the rancher
        restRancherMockMvc.perform(get("/api/ranchers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRancher() throws Exception {
        // Initialize the database
        rancherService.save(rancher);

        int databaseSizeBeforeUpdate = rancherRepository.findAll().size();

        // Update the rancher
        Rancher updatedRancher = rancherRepository.findById(rancher.getId()).get();
        // Disconnect from session so that the updates on updatedRancher are not directly saved in db
        em.detach(updatedRancher);
        updatedRancher
            .userId(UPDATED_USER_ID)
            .code(UPDATED_CODE)
            .shortCode(UPDATED_SHORT_CODE)
            .mark(UPDATED_MARK);

        restRancherMockMvc.perform(put("/api/ranchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRancher)))
            .andExpect(status().isOk());

        // Validate the Rancher in the database
        List<Rancher> rancherList = rancherRepository.findAll();
        assertThat(rancherList).hasSize(databaseSizeBeforeUpdate);
        Rancher testRancher = rancherList.get(rancherList.size() - 1);
        assertThat(testRancher.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRancher.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testRancher.getShortCode()).isEqualTo(UPDATED_SHORT_CODE);
        assertThat(testRancher.getMark()).isEqualTo(UPDATED_MARK);
    }

    @Test
    @Transactional
    public void updateNonExistingRancher() throws Exception {
        int databaseSizeBeforeUpdate = rancherRepository.findAll().size();

        // Create the Rancher

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRancherMockMvc.perform(put("/api/ranchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rancher)))
            .andExpect(status().isBadRequest());

        // Validate the Rancher in the database
        List<Rancher> rancherList = rancherRepository.findAll();
        assertThat(rancherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRancher() throws Exception {
        // Initialize the database
        rancherService.save(rancher);

        int databaseSizeBeforeDelete = rancherRepository.findAll().size();

        // Delete the rancher
        restRancherMockMvc.perform(delete("/api/ranchers/{id}", rancher.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rancher> rancherList = rancherRepository.findAll();
        assertThat(rancherList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rancher.class);
        Rancher rancher1 = new Rancher();
        rancher1.setId(1L);
        Rancher rancher2 = new Rancher();
        rancher2.setId(rancher1.getId());
        assertThat(rancher1).isEqualTo(rancher2);
        rancher2.setId(2L);
        assertThat(rancher1).isNotEqualTo(rancher2);
        rancher1.setId(null);
        assertThat(rancher1).isNotEqualTo(rancher2);
    }
}
