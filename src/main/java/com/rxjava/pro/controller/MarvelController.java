package com.rxjava.pro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rxjava.pro.model.request.MarvelCharacterObject;

public interface MarvelController {

	@RequestMapping(value = "/character", consumes = { "application/json" }, produces = {
			"application/json" }, method = RequestMethod.POST)
	public ResponseEntity<?> saveMarvelCharacterInDB(@RequestBody MarvelCharacterObject marvelCharacterObject);

}
