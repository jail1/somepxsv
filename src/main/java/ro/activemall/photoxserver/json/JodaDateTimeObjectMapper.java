package ro.activemall.photoxserver.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class JodaDateTimeObjectMapper extends ObjectMapper {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JodaDateTimeObjectMapper() {
		super();
		registerModule(new JodaModule());
	}
}
