package com.piotrwalkusz.lebrb.lanlearnservice.api;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrwalkusz.lebrb.lanlearn.LanLearnProcessor;
import com.piotrwalkusz.lebrb.lanlearn.Language;
import com.piotrwalkusz.lebrb.lanlearn.MediaType;
import com.piotrwalkusz.lebrb.lanlearn.TranslationDictionary;
import com.piotrwalkusz.lebrb.lanlearnservice.DictionaryManager;
import com.piotrwalkusz.lebrb.lanlearnservice.ResponseEntityUtil;
import com.piotrwalkusz.lebrb.lanlearnservice.model.RowWordsCounterResult;
import com.piotrwalkusz.lebrb.lanlearnservice.security.JWTUtil;
import io.swagger.annotations.ApiParam;
import kotlin.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-24T23:16:06.014Z")

@Controller
public class WordsCounterApiController implements WordsCounterApi {

    private static final Logger log = LoggerFactory.getLogger(WordsCounterApiController.class);

    @Autowired
    private DictionaryManager dictionaryManager;

    @Autowired
    private JWTUtil jwtUtil;

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final LanLearnProcessor lanLearnProcessor = new LanLearnProcessor();

    @org.springframework.beans.factory.annotation.Autowired
    public WordsCounterApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<?> wordsCounterPost(@ApiParam(value = "file detail") @Valid @RequestPart("file") MultipartFile file,
                                              @ApiParam(value = "The language of source text", required=true, allowableValues="pl, en, de") @RequestParam(value="from", required=true) String from,
                                              @ApiParam(value = "The language to which source text will be translated", required=true, allowableValues="pl, en, de") @RequestParam(value="to", required=true) String to,
                                              @RequestHeader("Authorization") String authorizationHeader) {

        if (!authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntityUtil.unauthorized().message("The \"Authorization\" header must contain value started with \"Bearer \"");
        }
        String token = authorizationHeader.substring(7);
        DecodedJWT decodedToken = jwtUtil.verifyAndDecode(token);
        long fileSize = decodedToken.getClaim("fileSize").asLong();
        if (file.getSize() != fileSize) {
            return ResponseEntityUtil.unauthorized().message("The token enables processing only file with size %d, actual size of sent file is %d", fileSize, file.getSize());
        }

        Language sourceLanguage = com.piotrwalkusz.lebrb.lanlearnservice.model.Language.fromValue(from).toLanLearnLanguage();
        Language destinationLanguage = com.piotrwalkusz.lebrb.lanlearnservice.model.Language.fromValue(to).toLanLearnLanguage();
        TranslationDictionary dictionary = dictionaryManager.getDictionary(sourceLanguage, destinationLanguage);
        if (dictionary == null) {
            return ResponseEntityUtil.badRequest().message("Translation for these languages is not supported");
        }

        try(InputStream inputstream = file.getInputStream()) {
            Map<String, Pair<String, Integer>> result = lanLearnProcessor.countAndTranslateWords(inputstream, MediaType.PDF, dictionary);
            List<RowWordsCounterResult> responseBody = result.entrySet().stream()
                    .map(x -> new RowWordsCounterResult()
                            .ori(x.getKey())
                            .tra(x.getValue().getFirst())
                            .num(x.getValue().getSecond())
                    ).collect(Collectors.toList());
            return new ResponseEntity<List<RowWordsCounterResult>>(responseBody, HttpStatus.OK);
        } catch (IOException ex) {
            return ResponseEntityUtil.internalServerError().message("");
        }
    }
}
