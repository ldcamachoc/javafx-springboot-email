package com.lakesidess.util;

import com.poiji.config.Casting;
import com.poiji.option.PoijiOptions;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class ExcelCasting implements Casting{

	@Override
	public Object castValue(Class<?> fieldType, String value, int row, int column, PoijiOptions options) {
		
		if(fieldType.getName().contains("String")){
			return new SimpleStringProperty(value);
		}
		
		if(fieldType.getName().contains("Long")) {
			return new SimpleLongProperty(Long.valueOf(value));
		}
		
		if(fieldType.getName().contains("int")) {
			return Integer.valueOf(value);
		}
		
		return "";
	}

}
