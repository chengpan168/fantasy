package com.shining3d.common;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class StringToDateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
		return DateUtils.format(source);
	}

}
