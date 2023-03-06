package com.etmoney.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.etmoney.request.VenueSearchRequest;
import com.etmoney.response.EtMoneyApiResponse;
import com.etmoney.response.VenueSearchResponse;
import com.etmoney.service.EtMoneyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
 /**
 * 
 * @author sourabh
 *
 */
@RestController
public class EtMoneyController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EtMoneyController.class);

		
	@Autowired
	EtMoneyService service;
	
	@GetMapping("/venue")
	public ResponseEntity<EtMoneyApiResponse> getVenues(@RequestBody VenueSearchRequest request) throws JsonMappingException, JsonProcessingException{
		LOGGER.info("requestBody : {}",  request.toString());
		return ResponseEntity.ok(service.getVenues(request));
	}
}
