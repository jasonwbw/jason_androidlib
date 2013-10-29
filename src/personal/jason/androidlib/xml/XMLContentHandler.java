/**
 * 解析xml，并提供获取结果的方法
 * 需要继承，实现相应方法，默认遇到标签无详细取词操作
 * 建议添加list和相应javabean 属性，并提供getlist方法获取最终结果
 *
 * 具体实现demo见   personal.jason.androidlib.demo.xml.XMLContentHandlerDemo
 *
 * @author vb.wbw
 * @create 2010.10.13
 */

package personal.jason.androidlib.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLContentHandler extends DefaultHandler {

	/**
	 * 用来记录当前前缀,在获取文本内容时可根据此属性值判断当前标签
	 */
	public String preTag;

	@Override
	public void startDocument() throws SAXException {
		startDoc();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		start(localName, attributes);
		preTag = localName;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String data = new String(ch, start, length);
		body(data);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		end(localName);
		preTag = null;

	}

	/**
	 * 子类需要重写，处理文档开始时的初始化
	 * 
	 * demo: List<Object> objs = new ArrayList<Object>(); 构建列表
	 */
	public void startDoc() {

	}

	/**
	 * 子类需要重写，处理每个开始标签，完成初始化等工作
	 * 
	 * demo： if("".equals(localName)){ obj = new Object(); }
	 * 
	 * @param localName
	 *            前最中的标签名
	 */
	public void start(String localName, Attributes attributes) {

	}

	/**
	 * 子类需要重写，获取每个标签内容
	 * 
	 * demo: if("".equals(preTag)){ obj.set……(data); }
	 * 
	 * @param data
	 */
	public void body(String data) {

	}

	/**
	 * 子类需要重写，处理每个结束标签，完成添加对象到列表
	 * 
	 * demo: if("".endsWith(localName) && obj != null){ objs.add(obj); obj =null; //清空对象内容，遇到新标签时重建 }
	 * 
	 * @param localName
	 */
	public void end(String localName) {

	}
}
