package com.piotrwalkusz.lebrb.lanlearnservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrwalkusz.lebrb.lanlearnservice.DictionaryEntity;
import com.piotrwalkusz.lebrb.lanlearnservice.DictionaryRepository;
import com.piotrwalkusz.lebrb.lanlearnservice.Swagger2SpringBoot;
import com.piotrwalkusz.lebrb.lanlearnservice.model.ErrorMessage;
import com.piotrwalkusz.lebrb.lanlearnservice.model.Language;
import com.piotrwalkusz.lebrb.lanlearnservice.model.LanguagePair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class SupportedLanguagesApiControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper jsonMapper;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
    @ComponentScan(basePackageClasses = Swagger2SpringBoot.class,
            excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = Swagger2SpringBoot.class)})
    public static class TestConfiguration {

        @Bean
        public DictionaryRepository dictionaryRepository() {
            DictionaryRepository repo = Mockito.mock(DictionaryRepository.class);
            given(repo.findAll()).willReturn(Arrays.asList(
                    new DictionaryEntity("0", "german;english\n"),
                    new DictionaryEntity("1", "german;polish\n"),
                    new DictionaryEntity("2", "polish;english\n")));

            return repo;
        }
    }

    @Test
    public void getAllSupportedLanguages() throws Exception {
        List<LanguagePair> expectedResponse = Arrays.asList(
                new LanguagePair(Language.DE, Language.EN),
                new LanguagePair(Language.DE, Language.PL),
                new LanguagePair(Language.PL, Language.EN)
        );

        mockMvc.perform(get("/supported-languages"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    public void filterBySourceLanguage() throws Exception {
        List<LanguagePair> expectedResponse = Arrays.asList(
                new LanguagePair(Language.DE, Language.EN),
                new LanguagePair(Language.DE, Language.PL)
        );

        mockMvc.perform(get("/supported-languages")
                .param("from", "de"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    public void filterByDestinationLanguage() throws Exception {
        List<LanguagePair> expectedResponse = Arrays.asList(
                new LanguagePair(Language.DE, Language.EN),
                new LanguagePair(Language.PL, Language.EN)
        );

        mockMvc.perform(get("/supported-languages")
                .param("to", "en"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    public void checkIfSpecificTranslationExists() throws Exception {
        List<LanguagePair> expectedResponse = Arrays.asList(
                new LanguagePair(Language.DE, Language.EN)
        );

        mockMvc.perform(get("/supported-languages")
                .param("from", "de")
                .param("to", "en"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    public void errorMessageIfInvalidSourceLanguage() throws Exception {
        ErrorMessage errorMessage = new ErrorMessage("Source language \"xx\" cannot be recognized");

        mockMvc.perform(get("/supported-languages")
                .param("from", "xx"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonMapper.writeValueAsString(errorMessage)));
    }

    @Test
    public void errorMessageIfInvalidDestinationLanguage() throws Exception {
        ErrorMessage errorMessage = new ErrorMessage("Destination language \"xx\" cannot be recognized");

        mockMvc.perform(get("/supported-languages")
                .param("to", "xx"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonMapper.writeValueAsString(errorMessage)));
    }
}