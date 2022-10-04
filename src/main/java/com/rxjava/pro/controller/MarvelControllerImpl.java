package com.rxjava.pro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rxjava.pro.model.request.MarvelCharacterObject;
import com.rxjava.pro.service.MarvelServiceImpl;

@RestController
@RequestMapping("/marvel")
public class MarvelControllerImpl implements MarvelController {

	@Autowired
	private MarvelServiceImpl marvelService;

	@Override
	public ResponseEntity<?> saveMarvelCharacterInDB(MarvelCharacterObject marvelCharacterObject) {

		marvelService.persistMarvelCharacterInDB(marvelCharacterObject.getCharacterName(),
				marvelCharacterObject.getResource());
		return ResponseEntity.status(HttpStatus.OK).body(new ModelMap().addAttribute("msg", "Success"));
	}
}
