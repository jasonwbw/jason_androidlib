package personal.jason.androidlib.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * the SAXService ,can use it handle all xml string. but should use handler in
 * right way and handler should be the child class in package sax
 * 
 * 详细使用demo见 personal.jason.androidlib.demo.xml.XMLAnalyticDemo
 * 
 * @author vb.wbw
 * @create 2010.10.13
 */

public class SAXService {

	/**
	 * readXMLString
	 * 
	 * @param source
	 * @param handler
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static DefaultHandler readXML(InputSource source,
			DefaultHandler handler) throws ParserConfigurationException,
			SAXException, IOException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser saxParser = spf.newSAXParser(); // 创建解析器
		XMLReader xr = saxParser.getXMLReader();
		xr.setContentHandler(handler);
		xr.parse(source);
		return handler;
	}
}
