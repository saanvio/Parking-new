package com.hcl.parking.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SpotOwnerRequestDto implements  Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long ownerId;
	private String fromDate;
	private String toDate;

}
