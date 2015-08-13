package br.com.smartsy.fwj.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.smartsy.fwj.util.StringUtil;

public class DateTimeAdapter extends XmlAdapter<String, DateTime> {
	
	private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final DateTimeFormatter fmt = DateTimeFormat.forPattern(PATTERN);

	@Override
	public DateTime unmarshal(String value) throws Exception {
		if(!StringUtil.hasText(value)) return null;
		try{
			return fmt.parseDateTime(value);
		}
		catch(IllegalArgumentException e){
			throw new IllegalArgumentException("Invalid date format: \""+value+"\". The accepted pattern is \""+PATTERN+"\"");
		}
	}

	@Override
	public String marshal(DateTime v) throws Exception {
		if(v == null)
			v = DateTime.now();
		return v.toString(fmt);
	}

}