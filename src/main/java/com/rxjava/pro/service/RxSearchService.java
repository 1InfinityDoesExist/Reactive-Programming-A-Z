package com.rxjava.pro.service;

import java.io.IOException;

import org.elasticsearch.ElasticsearchParseException;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rxjava.pro.model.request.RxSearchRequest;
import com.rxjava.pro.model.response.RxSearchResponse;

@Service
public interface RxSearchService {

	RxSearchResponse searchUsingPost(RxSearchRequest rxSearchRequest, Pageable pageable)
			throws IOException, ElasticsearchParseException, ParseException;

}
