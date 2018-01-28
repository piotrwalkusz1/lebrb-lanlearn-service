package com.piotrwalkusz.lebrb.lanlearnservice;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DictionaryRepository extends MongoRepository<DictionaryEntity, String> {

}