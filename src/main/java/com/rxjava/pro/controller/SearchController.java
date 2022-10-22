package com.rxjava.pro.controller;

import java.io.IOException;

import org.elasticsearch.ElasticsearchParseException;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rxjava.pro.model.request.RxSearchRequest;
import com.rxjava.pro.model.response.RxSearchResponse;
import com.rxjava.pro.service.RxSearchService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/v1.0")
public class SearchController {

	@Autowired
	private RxSearchService rxSearchService;

	@RequestMapping(value = "/search")
	public ResponseEntity<RxSearchResponse> searchUsingPost(@RequestBody RxSearchRequest rxSearchRequest,
			@PageableDefault(size = 10, page = 0, sort = { "name" }, direction = Sort.Direction.ASC) Pageable pageable)
			throws IOException, ElasticsearchParseException, ParseException {
		log.info("-----SearchController Class, searchUsingPost method with payload as : {}", rxSearchRequest);

		return ResponseEntity.status(HttpStatus.OK).body(rxSearchService.searchUsingPost(rxSearchRequest, pageable));

	}

}
