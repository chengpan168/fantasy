package com.fantasy.common;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class StringToDateConverter implements Converter<String, Date> {

    private Logger logger   = LoggerFactory.getLogger(StringToDateConverter.class);

    String[]       patterns = { "", "" };

    @Override
    public Date convert(String source) {
        try {
            return DateUtils.parseDate(source, patterns);
        } catch (ParseException e) {
            logger.warn("", e);
        }
        return null;
    }

}
