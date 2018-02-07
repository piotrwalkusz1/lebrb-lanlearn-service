package com.piotrwalkusz.lebrb.lanlearnservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrwalkusz.lebrb.lanlearn.Language;
import com.piotrwalkusz.lebrb.lanlearn.TranslationDictionary;
import com.piotrwalkusz.lebrb.lanlearnservice.DictionaryManager;
import com.piotrwalkusz.lebrb.lanlearnservice.Swagger2SpringBoot;
import com.piotrwalkusz.lebrb.lanlearnservice.model.ErrorMessage;
import com.piotrwalkusz.lebrb.lanlearnservice.model.RowWordsCounterResult;
import com.piotrwalkusz.lebrb.lanlearnservice.security.JWTUtil;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class WordsCounterApiControllerTest {

    @SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
    @ComponentScan(basePackageClasses = Swagger2SpringBoot.class,
            excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = Swagger2SpringBoot.class)})
    static class TestConfig {

        @Bean
        public JWTUtil jwtUtil() {
            Clock clock = Clock.fixed(Instant.ofEpochSecond(1000000000), ZoneId.systemDefault());
            String secret = "secret123";
            return new JWTUtil(secret, clock);
        }

        @Bean
        public DictionaryManager dictionaryManager() {
            DictionaryManager dictionaryManager = Mockito.mock(DictionaryManager.class);
            TranslationDictionary dictionary = TranslationDictionary.Companion.createFromReader(new StringReader(
                    "german;english\n" +
                            "hund;Hund;dog\n" +
                            "katze;Katze;cat\n"));
            given(dictionaryManager.getDictionary(Language.GERMAN, Language.ENGLISH)).willReturn(dictionary);

            return dictionaryManager;
        }
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper jsonMapper;

    private MockMvc mockMvc;



    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void successfulRequest() throws Exception {
        /*
         *    {
         *      "alg": "HS256",
         *      "typ": "JWT"
         *    }
         *    {
         *      "exp": 1000000005,
         *      "fileSize": 16383
         *    }
         */
        String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjEwMDAwMDAwMDUsImZpbGVTaXplIjoxNjM4M30.IMJeIIPQSknjY2UW3kI5tU_2JEFpZZtwrx3CLJsaGms";
        Path path = webApplicationContext.getResource("classpath:short example.pdf").getFile().toPath();
        List<RowWordsCounterResult> expectedResponse = new ArrayList<RowWordsCounterResult>();
        expectedResponse.add(new RowWordsCounterResult().ori("Hund").tra("dog").num(1));
        expectedResponse.add(new RowWordsCounterResult().ori("Katze").tra("cat").num(2));

        mockMvc.perform(multipart("/words-counter")
                .file("file", Files.readAllBytes(path))
                .param("from", "de")
                .param("to", "en")
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().is(200))
                .andExpect(content().json(jsonMapper.writeValueAsString(expectedResponse)));
    }

    // The file "short example.pdf" has 16383B size. A token permits process only a file with 16382B size.
    // Note that token doesn't permit to process files with smaller size as well. The "fileSize" parameter
    // has to be identical to actual file size.
    @Test
    public void fileSizesMustMatch() throws Exception {
        /*
         *    {
         *      "alg": "HS256",
         *      "typ": "JWT"
         *    }
         *    {
         *      "exp": 1000000005,
         *      "fileSize": 16382
         *    }
         */
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjEwMDAwMDAwMDUsImZpbGVTaXplIjoxNjM4Mn0.FyZRAPw-N_S_awz_tF5Q1j3eWzhrTQ5_A2DhBewSu8o";
        Path path = webApplicationContext.getResource("classpath:short example.pdf").getFile().toPath();
        ErrorMessage expectedResponse = new ErrorMessage("The token enables processing only file with size 16382, actual size of sent file is 16383");

        mockMvc.perform(multipart("/words-counter")
                .file("file", Files.readAllBytes(path))
                .param("from", "de")
                .param("to", "e")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is(401))
                .andExpect(content().json(jsonMapper.writeValueAsString(expectedResponse)));
    }
}