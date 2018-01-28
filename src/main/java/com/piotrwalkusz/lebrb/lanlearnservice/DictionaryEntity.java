package com.piotrwalkusz.lebrb.lanlearnservice;

import org.springframework.data.annotation.Id;

public class DictionaryEntity {

   @Id
   private String id;

   private String rawDictionary;

   public DictionaryEntity(String id, String rawDictionary) {
      this.id = id;
      this.rawDictionary = rawDictionary;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getRawDictionary() {
      return rawDictionary;
   }

   public void setRawDictionary(String rawDictionary) {
      this.rawDictionary = rawDictionary;
   }
}
