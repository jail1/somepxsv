package ro.activemall.photoxserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BNRCurrencyTest {

	public static void main(String[] args) {
		new BNRCurrencyTest().test();
	}

	public void test() {
		try {
			String bnrWebService = "http://www.bnro.ro/nbrfxrates.xml";
			URL bnrUrl = new URL(bnrWebService);
			URLConnection connection = bnrUrl.openConnection();
			connection.setConnectTimeout(120000);
			connection.setReadTimeout(120000);
			BufferedReader details = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "UTF-8"));
			InputSource inputSource = new InputSource(details);
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			SaxCurrencyHandler currencyHandler = new SaxCurrencyHandler();
			saxParser.parse(inputSource, currencyHandler);
			for (Map.Entry<String, String> currency : currencyHandler.currencies
					.entrySet()) {
				System.out.println(currency.getKey() + " = "
						+ currency.getValue());
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public class SaxCurrencyHandler extends DefaultHandler {

		public Map<String, String> currencies = new HashMap<String, String>();
		private String currency;
		private String currencyValue;
		private boolean readValue = false;

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("rate".equals(qName.toLowerCase())) {
				readValue = true;
				currency = attributes.getValue("currency");
			}
		}

		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if ("rate".equals(qName.toLowerCase())) {
				currencies.put(currency, currencyValue);
			}
			readValue = false;
		}

		public void characters(char ch[], int start, int length)
				throws SAXException {
			String value = new String(ch, start, length).trim();
			if (value.length() == 0)
				return; // ignore white space
			if (readValue) {
				currencyValue = value;
			}
		}
	}
}
