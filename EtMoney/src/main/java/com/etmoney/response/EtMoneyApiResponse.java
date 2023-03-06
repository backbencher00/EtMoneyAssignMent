package com.etmoney.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EtMoneyApiResponse {
 
	private boolean success = true;

	private String message;

	private String code;

	private Object data;

}
