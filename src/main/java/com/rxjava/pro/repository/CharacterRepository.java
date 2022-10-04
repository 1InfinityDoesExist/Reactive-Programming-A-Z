package com.rxjava.pro.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rxjava.pro.entity.Result;

@Repository
public interface CharacterRepository extends MongoRepository<Result, String> {

	Result findResultById(int id);

}
