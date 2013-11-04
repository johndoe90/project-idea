package com.phillip.idea.domain.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DtoListConverter {

	private static DtoListConverter converter;
	
	private DtoListConverter(){}
	
	public static DtoListConverter getInstance(){
		if(converter == null){
			converter = new DtoListConverter();
		}
		
		return converter;
	}
	
	@SuppressWarnings("unchecked")
	public <T, U> List<T> convert(List<U> input, Class<T> targetClass){
		if(input == null || input.isEmpty())
			return Collections.emptyList();
		
		List<T> result = new ArrayList<T>();
		Class<U> sourceClass = (Class<U>) input.iterator().next().getClass();
		for(U current : input){
			try {
				result.add((T) Class.forName(targetClass.getName()).getConstructor(sourceClass).newInstance(current));
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		return result;
	}
}
