package com.rxjava.pro.service;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import rx.Single;

@Service
public interface MarvelService {
	public Single<Long> getCharacterIdByName(String name);

	public Single<JSONObject> getCharacterDetailsById(long id);

	public void persistMarvelCharacterInDB(String characterName, String resource);

}
