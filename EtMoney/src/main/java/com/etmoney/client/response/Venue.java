package com.etmoney.client.response;

import java.sql.Date;
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
public class Venue {
	private String id;
	private String name;
	private Contact contact;
	private List<HotelCategory> categories;
	private Boolean verified;
	private Stat stats;
	private String url; 
    private Boolean allowMenuUrlEdit;
    private BeenHere beenHere;
    private Date createdAt;
    private HereNow hereNow;
    private String referralId;
    private List<VenueChain> venueChains;
    private Boolean hasPerk;
    
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
