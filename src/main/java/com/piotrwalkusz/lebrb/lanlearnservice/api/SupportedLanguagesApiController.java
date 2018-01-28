package com.piotrwalkusz.lebrb.lanlearnservice.api;

import com.piotrwalkusz.lebrb.lanlearnservice.DictionaryManager;
import com.piotrwalkusz.lebrb.lanlearnservice.ResponseEntityUtil;
import com.piotrwalkusz.lebrb.lanlearnservice.model.Language;
import com.piotrwalkusz.lebrb.lanlearnservice.model.LanguagePair;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.parser.Entity;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-24T23:16:06.014Z")

@Controller
public class SupportedLanguagesApiController implements SupportedLanguagesApi {

    private static final Logger log = LoggerFactory.getLogger(SupportedLanguagesApiController.class);

    @Autowired
    private DictionaryManager dictionaryManager;

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public SupportedLanguagesApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<?> supportedLanguagesGet(@ApiParam(value = "Get only pairs of languages where a source language equals given language ", allowableValues = "pl, en, de") @Valid @RequestParam(value = "from", required = false) String from,
                                                   @ApiParam(value = "Get only pairs of languages where a destination language equals given language ", allowableValues = "pl, en, de") @Valid @RequestParam(value = "to", required = false) String to) {

        Language sourceLanguage;
        Language destinationLanguage;

        try {
            sourceLanguage = from == null ? null : Language.fromValueOrException(from);
        } catch (IllegalArgumentException ex) {
            return ResponseEntityUtil.badRequest().message("Source language \"%s\" cannot be recognized", from);
        }

        try {
            destinationLanguage = to == null ? null : Language.fromValueOrException(to);
        } catch (IllegalArgumentException ex) {
            return ResponseEntityUtil.badRequest().message("Destination language \"%s\" cannot be recognized", to);
        }

        List<LanguagePair> languagePairs = dictionaryManager.getAllSupportedTranslations().stream()
                .map(x -> new LanguagePair(Language.fromLanLearnLanguage(x.getFirst()), Language.fromLanLearnLanguage(x.getSecond())))
                .filter(x -> (sourceLanguage == null || x.getFrom() == sourceLanguage) &&
                             (destinationLanguage == null || x.getTo() == destinationLanguage))
                .collect(Collectors.toList());

        return new ResponseEntity<List<LanguagePair>>(languagePairs, HttpStatus.OK);
    }
}
