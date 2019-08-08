package com.hcl.parking.entity;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
	SPOT_OWNER, HQ_EMPLOYEES,ADMIN;

	private static Map<String, Role> rolesMap=new HashMap<>();
	static {
		rolesMap.put("Spot owner",SPOT_OWNER);
		rolesMap.put("Hq employees",HQ_EMPLOYEES);
		rolesMap.put("admin",ADMIN);
		
	}
	
	@JsonCreator
    public static Role forValue(String value) {
		if(value==null)
			return null;
        return rolesMap.get(value.toLowerCase());
    }
}
