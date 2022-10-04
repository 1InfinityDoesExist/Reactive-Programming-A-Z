package com.rxjava.pro.model.request;

import java.io.Serializable;

import lombok.Data;

@Data
public class MarvelCharacterObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private String characterName;
	private String resource;
}
