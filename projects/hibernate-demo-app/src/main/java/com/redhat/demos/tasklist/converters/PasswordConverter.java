package com.redhat.demos.tasklist.converters;

import java.util.Base64;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PasswordConverter implements AttributeConverter<String, String>{

	@Override
	public String convertToDatabaseColumn(String arg0) {
		if(arg0!=null) {
			return Base64.getEncoder().encodeToString(arg0.getBytes());
		} else {
			return null;
		}
	}

	@Override
	public String convertToEntityAttribute(String arg0) {
		if(arg0!=null) {
			return new String(Base64.getDecoder().decode(arg0));
		} else {
			return null;
		}
	}
	
	
}
