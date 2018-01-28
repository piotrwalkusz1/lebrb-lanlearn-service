package com.piotrwalkusz.lebrb.lanlearnservice.model;

import com.fasterxml.jackson.annotation.JsonValue;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets language
 */
public enum Language {

    PL("pl") {
        @Override
        public com.piotrwalkusz.lebrb.lanlearn.Language toLanLearnLanguage() {
            return com.piotrwalkusz.lebrb.lanlearn.Language.POLISH;
        }
    },

    EN("en") {
        @Override
        public com.piotrwalkusz.lebrb.lanlearn.Language toLanLearnLanguage() {
            return com.piotrwalkusz.lebrb.lanlearn.Language.ENGLISH;
        }
    },

    DE("de") {
        @Override
        public com.piotrwalkusz.lebrb.lanlearn.Language toLanLearnLanguage() {
            return com.piotrwalkusz.lebrb.lanlearn.Language.GERMAN;
        }
    };

    private String value;

    Language(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static Language fromValue(String text) {
        for (Language b : Language.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

    public static Language fromLanLearnLanguage(com.piotrwalkusz.lebrb.lanlearn.Language language) {
        for (Language lan : Language.values()) {
            if (lan.toLanLearnLanguage() == language) {
                return lan;
            }
        }

        return null;
    }

    public abstract com.piotrwalkusz.lebrb.lanlearn.Language toLanLearnLanguage();
}

