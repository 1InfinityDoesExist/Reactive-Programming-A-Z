package com.rxjava.pro.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import com.rxjava.pro.model.request.RxSearchRequest;
import com.rxjava.pro.model.response.RxSearchResponse;
import com.rxjava.pro.service.RxSearchService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RxSearchServiceImpl implements RxSearchService {

	@Autowired
	private RestHighLevelClient esClient;

	private static final String REGX_FORMAT = "*%s*";

	private static final ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * 
	 */
	@Override
	public RxSearchResponse searchUsingPost(RxSearchRequest rxSearchRequest, Pageable pageable)
			throws IOException, ElasticsearchParseException, ParseException {
		log.info("-----RxSearchServiceImpl Class, searchUsingPost method : {}", rxSearchRequest);

		String keyword = rxSearchRequest.getKeyword();
		if (!ObjectUtils.isEmpty(keyword)) {
			keyword = keyword.replaceAll(" ", "*");
		}

		HashSet<String> indexes = new HashSet<>();

		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//		rxSearchRequest.getIndices().entrySet().stream().forEach(entry -> {
//			indexes.add(entry.getKey());
//			buildQuery(queryBuilder, keyword, entry);
//		});

		for (Entry<String, List<String>> entry : rxSearchRequest.getIndices().entrySet()) {
			indexes.add(entry.getKey());
			buildQuery(queryBuilder, keyword, entry);
		}

		log.info("-------Final Query : {}", queryBuilder);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(queryBuilder)
				.from(pageable.getPageNumber() * pageable.getPageSize()).size(pageable.getPageSize()).explain(true);

		Optional.ofNullable(pageable.getSort()).map(Sort::iterator)
				.ifPresent(iterator -> iterator.forEachRemaining(order -> searchSourceBuilder
						.sort(order.getProperty() + ".keyword", Optional.ofNullable(order.getDirection())
								.map(Direction::toString).map(SortOrder::fromString).orElse(SortOrder.ASC))));

		SearchRequest searchRequest = new SearchRequest().indices(Iterables.toArray(indexes, String.class))
				.source(searchSourceBuilder);

		SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

		SearchHits searchHit = searchResponse.getHits();

		SearchHit[] searchHits = searchHit.getHits();

		List<JSONObject> searchRes = Arrays.stream(searchHits).map(document -> {

			JSONObject response = null;
			try {
				response = (JSONObject) new JSONParser().parse(document.getSourceAsString());

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
		}).filter(Objects::nonNull).collect(Collectors.toList());

		return RxSearchResponse.builder().pageNumber(pageable.getPageNumber()).pageSize(pageable.getPageSize())
				.totalRecords(searchHits.length).response(searchRes).build();
	}

	private void buildQuery(BoolQueryBuilder queryBuilder, String keyword, Entry<String, List<String>> entry)
			throws ElasticsearchParseException, IOException, ParseException {

		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.typeQuery(entry.getKey()));

		if (CollectionUtils.isEmpty(entry.getValue())) {
			entry.setValue(getAllMappings(entry.getKey()));
		}
		BoolQueryBuilder fieldQuery = QueryBuilders.boolQuery();
		entry.getValue().stream().forEach(field -> {
			fieldQuery.should(QueryBuilders.wildcardQuery(field + ".keyword", String.format(REGX_FORMAT, keyword))
					.caseInsensitive(true));
		});
		boolQuery.must(fieldQuery);

		queryBuilder.should(boolQuery);
	}

	/**
	 * 
	 * @param index
	 * @return
	 * @throws IOException
	 * @throws ElasticsearchParseException
	 * @throws ParseException
	 */
	private List<String> getAllMappings(String index) throws IOException, ElasticsearchParseException, ParseException {
		log.info("----Retrieving all the mapping data. for index : {}", index);
		List<String> att = new ArrayList<>();
		GetMappingsRequest request = new GetMappingsRequest();
		request.indices(index);
		request.setMasterTimeout(TimeValue.timeValueMinutes(1));
		// sync call
		GetMappingsResponse syncMappingResponse = esClient.indices().getMapping(request, RequestOptions.DEFAULT);
		JSONObject entityJsonObject = (JSONObject) ((JSONObject) new JSONParser()
				.parse(new ObjectMapper().writeValueAsString(syncMappingResponse.mappings().get(index).sourceAsMap())))
				.get("properties");
		log.info("-----Object : {}", entityJsonObject);
		for (Object obj : entityJsonObject.keySet()) {
			String prop = (String) obj;
			log.info("---Prop : {}", prop);
			att.add(prop);
		}
		log.info("----Attribute on which searcing will be called : {}", att.size());
		return att;
	}
}
