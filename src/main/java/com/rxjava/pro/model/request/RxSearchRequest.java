package com.rxjava.pro.model.request;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RxSearchRequest implements Serializable {

	private Map<String, List<String>> indices;
	private String keyword;

}
