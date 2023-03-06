package com.etmoney.client.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VenueChain {
		private String id;
		
		public  String toString() {
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
