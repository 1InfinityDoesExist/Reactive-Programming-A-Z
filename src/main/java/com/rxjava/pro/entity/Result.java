package com.rxjava.pro.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rxjava.pro.model.Comics;
import com.rxjava.pro.model.Events;
import com.rxjava.pro.model.Series;
import com.rxjava.pro.model.Stories;
import com.rxjava.pro.model.Thumbnail;
import com.rxjava.pro.model.Url;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "characters")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String defaultId;
	public int id;
	public String name;
	public String description;
	public Date modified;
	public String resourceURI;
	public ArrayList<Url> urls;
	public Thumbnail thumbnail;
	public Comics comics;
	public Stories stories;
	public Events events;
	public Series series;
}
