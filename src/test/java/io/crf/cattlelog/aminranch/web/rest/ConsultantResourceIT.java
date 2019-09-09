package io.crf.cattlelog.aminranch.web.rest;

import io.crf.cattlelog.aminranch.AdminranchApp;
import io.crf.cattlelog.aminranch.domain.Consultant;
import io.crf.cattlelog.aminranch.repository.ConsultantRepository;
import io.crf.cattlelog.aminranch.service.ConsultantService;
import io.crf.cattlelog.aminranch.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static io.crf.cattlelog.aminranch.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link ConsultantResource} REST controller.
 */
@SpringBootTest(classes = AdminranchApp.class)
public class ConsultantResourceIT {

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final String DEFAULT_LICENCE = "AAAAAAAAAA";
    private static final String UPDATED_LICENCE = "BBBBBBBBBB";

    @Autowired
    private ConsultantRepository consultantRepository;

    @Mock
    private ConsultantRepository consultantRepositoryMock;

    @Mock
    private ConsultantService consultantServiceMock;

    @Autowired
    private ConsultantService consultantService;

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

    private MockMvc restConsultantMockMvc;

    private Consultant consultant;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConsultantResource consultantResource = new ConsultantResource(consultantService);
        this.restConsultantMockMvc = MockMvcBuilders.standaloneSetup(consultantResource)
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
    public static Consultant createEntity(EntityManager em) {
        Consultant consultant = new Consultant()
            .userId(DEFAULT_USER_ID)
            .licence(DEFAULT_LICENCE);
        return consultant;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultant createUpdatedEntity(EntityManager em) {
        Consultant consultant = new Consultant()
            .userId(UPDATED_USER_ID)
            .licence(UPDATED_LICENCE);
        return consultant;
    }

    @BeforeEach
    public void initTest() {
        consultant = createEntity(em);
    }

    @Test
    @Transactional
    public void createConsultant() throws Exception {
        int databaseSizeBeforeCreate = consultantRepository.findAll().size();

        // Create the Consultant
        restConsultantMockMvc.perform(post("/api/consultants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consultant)))
            .andExpect(status().isCreated());

        // Validate the Consultant in the database
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeCreate + 1);
        Consultant testConsultant = consultantList.get(consultantList.size() - 1);
        assertThat(testConsultant.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testConsultant.getLicence()).isEqualTo(DEFAULT_LICENCE);
    }

    @Test
    @Transactional
    public void createConsultantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = consultantRepository.findAll().size();

        // Create the Consultant with an existing ID
        consultant.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsultantMockMvc.perform(post("/api/consultants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consultant)))
            .andExpect(status().isBadRequest());

        // Validate the Consultant in the database
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = consultantRepository.findAll().size();
        // set the field null
        consultant.setUserId(null);

        // Create the Consultant, which fails.

        restConsultantMockMvc.perform(post("/api/consultants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consultant)))
            .andExpect(status().isBadRequest());

        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConsultants() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList
        restConsultantMockMvc.perform(get("/api/consultants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consultant.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].licence").value(hasItem(DEFAULT_LICENCE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllConsultantsWithEagerRelationshipsIsEnabled() throws Exception {
        ConsultantResource consultantResource = new ConsultantResource(consultantServiceMock);
        when(consultantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restConsultantMockMvc = MockMvcBuilders.standaloneSetup(consultantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restConsultantMockMvc.perform(get("/api/consultants?eagerload=true"))
        .andExpect(status().isOk());

        verify(consultantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllConsultantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ConsultantResource consultantResource = new ConsultantResource(consultantServiceMock);
            when(consultantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restConsultantMockMvc = MockMvcBuilders.standaloneSetup(consultantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restConsultantMockMvc.perform(get("/api/consultants?eagerload=true"))
        .andExpect(status().isOk());

            verify(consultantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getConsultant() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get the consultant
        restConsultantMockMvc.perform(get("/api/consultants/{id}", consultant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(consultant.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.licence").value(DEFAULT_LICENCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConsultant() throws Exception {
        // Get the consultant
        restConsultantMockMvc.perform(get("/api/consultants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConsultant() throws Exception {
        // Initialize the database
        consultantService.save(consultant);

        int databaseSizeBeforeUpdate = consultantRepository.findAll().size();

        // Update the consultant
        Consultant updatedConsultant = consultantRepository.findById(consultant.getId()).get();
        // Disconnect from session so that the updates on updatedConsultant are not directly saved in db
        em.detach(updatedConsultant);
        updatedConsultant
            .userId(UPDATED_USER_ID)
            .licence(UPDATED_LICENCE);

        restConsultantMockMvc.perform(put("/api/consultants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConsultant)))
            .andExpect(status().isOk());

        // Validate the Consultant in the database
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeUpdate);
        Consultant testConsultant = consultantList.get(consultantList.size() - 1);
        assertThat(testConsultant.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testConsultant.getLicence()).isEqualTo(UPDATED_LICENCE);
    }

    @Test
    @Transactional
    public void updateNonExistingConsultant() throws Exception {
        int databaseSizeBeforeUpdate = consultantRepository.findAll().size();

        // Create the Consultant

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultantMockMvc.perform(put("/api/consultants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consultant)))
            .andExpect(status().isBadRequest());

        // Validate the Consultant in the database
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConsultant() throws Exception {
        // Initialize the database
        consultantService.save(consultant);

        int databaseSizeBeforeDelete = consultantRepository.findAll().size();

        // Delete the consultant
        restConsultantMockMvc.perform(delete("/api/consultants/{id}", consultant.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Consultant.class);
        Consultant consultant1 = new Consultant();
        consultant1.setId(1L);
        Consultant consultant2 = new Consultant();
        consultant2.setId(consultant1.getId());
        assertThat(consultant1).isEqualTo(consultant2);
        consultant2.setId(2L);
        assertThat(consultant1).isNotEqualTo(consultant2);
        consultant1.setId(null);
        assertThat(consultant1).isNotEqualTo(consultant2);
    }
}
