package com.piotrwalkusz.lebrb.lanlearnservice;

import com.piotrwalkusz.lebrb.lanlearn.Language;
import com.piotrwalkusz.lebrb.lanlearn.TranslationDictionary;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictionaryManager {

    @Autowired
    private DictionaryRepository dictionaryRepository;

    private List<TranslationDictionary> dictionaries;

    @PostConstruct
    private void initialize() {
        updateDictionaries();
    }

    public void updateDictionaries() {
        dictionaries = dictionaryRepository.findAll().stream()
                .map(x -> TranslationDictionary.Companion.createFromReader(new StringReader(x.rawDictionary)))
                .collect(Collectors.toList());
    }

    public TranslationDictionary getDictionary(Language from, Language to) {
        return IterableUtils.find(dictionaries, x -> x.getSourceLanguage() == from && x.getDestinationLanguage() == to);
    }

    public List<Pair<Language, Language>> getAllSupportedTranslations() {
        return dictionaries.stream()
                .map(x -> Pair.of(x.getSourceLanguage(), x.getDestinationLanguage()))
                .collect(Collectors.toList());
    }
}