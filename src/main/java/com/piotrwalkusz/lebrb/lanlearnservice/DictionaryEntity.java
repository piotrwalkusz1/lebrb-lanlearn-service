package com.piotrwalkusz.lebrb.lanlearnservice;

import org.springframework.data.annotation.Id;

public class DictionaryEntity {

   @Id
   public String id;

   public String rawDictionary;
}
