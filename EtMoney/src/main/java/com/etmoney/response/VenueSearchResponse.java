package com.etmoney.response;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VenueSearchResponse {
			private List<VenueResponse> venues;
			public String toString() {
				String serialized = "";
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					serialized = objectMapper.writeValueAsString(this);
				} catch (JsonProcessingException jpe) {
					jpe.printStackTrace();
				}
				return serialized;
			}
}
