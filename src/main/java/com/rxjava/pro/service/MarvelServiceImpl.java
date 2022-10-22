package com.rxjava.pro.service;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTimeUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.rxjava.pro.model.Characters;
import com.rxjava.pro.model.Data;
import com.rxjava.pro.repository.CharacterRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import rx.Single;

@Slf4j
@Component
public class MarvelServiceImpl implements MarvelService {

	@Autowired
	private CharacterRepository characterRepository;

	@Autowired
	private WebClient.Builder webClientBuilder;

	@Value("${marvel.private.key}")
	private String privateKey;
	@Value("${marvel.public.key}")
	private String publicKey;
	@Value("${marvel.endpoint.url}")
	private String marvelEndPoint;

	@Override
	public Single<Long> getCharacterIdByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Single<JSONObject> getCharacterDetailsById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * .forEach(marvelHero -> characterRepository.save(marvelHero)
	 * 
	 * @param characterName
	 */
	@Override
	public void persistMarvelCharacterInDB(String characterName, String resource) {

		Long currentTime = DateTimeUtils.currentTimeMillis();
		byte[] hash = DigestUtils.md5(currentTime + privateKey + publicKey);
		final String result = new String(Hex.encodeHex(hash));

		log.info("Thread at the beginning of method : {}", Thread.currentThread().getName());
		Mono<Characters> characters = webClientBuilder.build().get()
				.uri(uriBuilder -> uriBuilder.path(marvelEndPoint).queryParam("ts", currentTime)
						.queryParam("apikey", publicKey).queryParam("hash", result).queryParam("name", characterName)
						.build(resource))
				.retrieve().bodyToMono(Characters.class);

		characters.map(Characters::getData).map(Data::getResults).filter(characterSize -> characterSize.size() > 0)
				.doOnNext(logs -> log.info(" Thread : {}", Thread.currentThread().getName())).subscribe(marvelChar -> {
					marvelChar.parallelStream()
							.filter(hero -> ObjectUtils.isEmpty(characterRepository.findResultById(hero.getId())))
							.forEach(res -> characterRepository.save(res));
				});
		log.info("Thread at end of method: {}", Thread.currentThread().getName());
	}

}
