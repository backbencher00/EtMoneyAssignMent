package com.etmoney.client.response;

import java.util.List;

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
public class Location {
	private String  address;
	private String Shop;  
    private String crossStreet;
	private String lat;
	private String lng;
	private LabeledLatitudeLongitude labeledLatLngs;
	private String postalCode;
	private String cc;
	private String city;
	private String state;
	private String country;
	private List<String> formattedAddress;
	
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
