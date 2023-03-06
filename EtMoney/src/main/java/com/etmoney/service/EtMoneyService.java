package com.etmoney.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.validation.ValidationException;

import org.apache.coyote.http11.Http11AprProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etmoney.client.VenueSearchClient;
import com.etmoney.client.response.HotelCategory;
import com.etmoney.client.response.Stat;
import com.etmoney.client.response.Venue;
import com.etmoney.client.response.VenueSearchClientResponse;
import com.etmoney.request.SortingProvision;
import com.etmoney.request.VenueSearchRequest;
import com.etmoney.response.EtMoneyApiResponse;
import com.etmoney.response.VenueResponse;
import com.etmoney.response.VenueSearchResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EtMoneyService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EtMoneyService.class);

	@Autowired
	VenueSearchClient client;
	
	@Autowired
	ObjectMapper mapper;
		
	public EtMoneyApiResponse getVenues(VenueSearchRequest request) throws JsonMappingException, JsonProcessingException {
		validateRequest(request);
		String venueLists = client.getVenueDetails();
 		VenueSearchClientResponse venueSearchResponse =  mapper.readValue(venueLists, VenueSearchClientResponse.class);
 		if(null == venueSearchResponse || null == venueSearchResponse.getResponse() || null== venueSearchResponse.getResponse().getVenues()) {
 			throw new ValidationException("No data Found while making the client call... !");
 		}
 		List<Venue> venues  = venueSearchResponse.getResponse().getVenues();
 		LOGGER.info("client response venues : {}", venues);
 		List<VenueResponse> venueResponseList = new ArrayList<>();
 		if(null == request.getFilter().getCategory() ||  request.getFilter().getCategory().isEmpty()){
  			applyVerifiedFilter(request, venues, venueResponseList);
  			
 		}else {
  			List<Venue>filteredVenues = applyCategoryFilter(request, venues, venueResponseList);
  			applyVerifiedFilter(request, filteredVenues, venueResponseList);
 		}
 		
	 	   sortAccordingSortingProvision(request, venueResponseList); 
	 	   VenueSearchResponse response = new VenueSearchResponse();
	 	   response.setVenues(venueResponseList);
		   LOGGER.info("response : {}", response);
 		   return buildEtMoneyApiResponse(venueResponseList, response);
	}

	private EtMoneyApiResponse buildEtMoneyApiResponse(List<VenueResponse> venueResponseList,
			VenueSearchResponse response) {
		EtMoneyApiResponse etMoneyApiResponse = new EtMoneyApiResponse();
 		  etMoneyApiResponse.setSuccess(true);
 		  etMoneyApiResponse.setCode("200");
 		  if(null == venueResponseList || venueResponseList.isEmpty()) {
 	 		  etMoneyApiResponse.setMessage("No Data Found According to your search");
 		  }else {
 	 		  etMoneyApiResponse.setMessage("your search Data is Here");

 		  }
 		  etMoneyApiResponse.setData(response);
		return etMoneyApiResponse;
	}

	private List<Venue> applyCategoryFilter(VenueSearchRequest request, List<Venue> venues,
			List<VenueResponse> venueResponseList) {
		    HashMap<String, String> category = new HashMap<String, String>();
		    for(String it : request.getFilter().getCategory()) {
		    	System.out.println(it);
		    	category.put(it, it);
		    }
	 		LOGGER.info("category Map : {}", category);
		    List<Venue> filteredVenue = new ArrayList<>();
		    for(Venue it : venues) {
		    	List<HotelCategory> hotelCategoryResponse = new ArrayList<>();
		    	for(HotelCategory hotelCategory : it.getCategories()) {
		    		if(category.containsKey(hotelCategory.getName())) {
		    			hotelCategoryResponse.add(hotelCategory);		    			
		    		}
		    	}
		    	if(null !=  hotelCategoryResponse && !hotelCategoryResponse.isEmpty()) {
		    		filteredVenue.add(buildFilteredVanue(it, hotelCategoryResponse));
		    	}
		    }
	 		LOGGER.info("filtered Venue : {}", filteredVenue);
		    return filteredVenue;
	}

	private Venue buildFilteredVanue( Venue it, List<HotelCategory> hotelCategoryResponse) {
		Venue newVenueResponse = new Venue();
		newVenueResponse.setName(it.getName());
		newVenueResponse.setCategories(hotelCategoryResponse);
		newVenueResponse.setVerified(it.getVerified());
		newVenueResponse.setStats(it.getStats());
 		return newVenueResponse;
	}
	private void buildVenueResponse(Venue it, VenueResponse venuresponse) {
		venuresponse.setName(it.getName()); 
		venuresponse.setCategoryNames(buildHotelCategoryName(it));
		venuresponse.setVerified(it.getVerified());
		venuresponse.setStats(buildStats(it));
	}

	private void applyVerifiedFilter(VenueSearchRequest request, List<Venue> venues, List<VenueResponse> venueResponseList) {
		if(null == request.getFilter().getVerified()) {
			for(Venue it : venues) {
		    	buildVenueResponse(venueResponseList, it);
		  }	
		}else if( request.getFilter().getVerified()) {
			for(Venue it : venues) {
		    	if(it.getVerified()) {
 			    	buildVenueResponse(venueResponseList, it);
		    	}
			}	
		}else if(!request.getFilter().getVerified()) {
 			for(Venue it : venues) {
		    	if(!it.getVerified()) {
			    	buildVenueResponse(venueResponseList, it);
		    	}
			}	
		}
	}

	private void buildVenueResponse(List<VenueResponse> venueResponseList, Venue it) {
		VenueResponse venuresponse = new VenueResponse();
		buildVenueResponse(it, venuresponse);
		venueResponseList.add(venuresponse);
	}

	
	private void sortAccordingSortingProvision(VenueSearchRequest request, List<VenueResponse> venueResponseList) {
		if(null != request.getSort() ) {
 	    	Collections.sort(venueResponseList, new Comparator<VenueResponse>() {
 	            @Override
 	            public int compare(VenueResponse p1, VenueResponse p2) {
 	            	 	if(request.getSort().equals(SortingProvision.tipCount)) {
 	            	 		 return  p1.getStats().getTipCount()-p2.getStats().getTipCount();
 	            	 	}else if(request.getSort().equals(SortingProvision.checkinsCount)) {
 	            	 		 return  p1.getStats().getCheckinsCount()-p2.getStats().getCheckinsCount();
 	            	 	}else if(request.getSort().equals(SortingProvision.usersCount)){
 	            	 		 return  p1.getStats().getUsersCount()-p2.getStats().getUsersCount();
 	            	 	}else {
 	            	 		throw new ValidationException("Invalid Request");
 	            	 	}
 	            }
 	        });
 	    }
	}

	private Stat buildStats(Venue it) {
		Stat stats = new Stat();
		stats.setCheckinsCount(it.getStats().getCheckinsCount());
		stats.setTipCount(it.getStats().getTipCount());
		stats.setUsersCount(it.getStats().getUsersCount());
		return stats;
	}

	private List<String> buildHotelCategoryName(Venue it) {
		List<String> categories = new ArrayList<>();
		for(HotelCategory hoteCategory : it.getCategories()) {
			categories.add(hoteCategory.getName());
		}
		return categories;
	}

	private void validateRequest(VenueSearchRequest request) {
		 
		if(null == request || null == request.getFilter()
				|| null == request.getSort()) {
					throw new ValidationException("Request body is null");
		}/*else if(null != request && (null == request.getFilter().getCategory() || null == request.getFilter().getVerified())) {
			throw new ValidationException("Invalid Request");
		}*/
	}  
	 
}
