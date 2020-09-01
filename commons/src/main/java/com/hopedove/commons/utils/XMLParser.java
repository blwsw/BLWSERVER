package com.hopedove.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: rizenguo Date: 2014/11/1 Time: 14:06
 */
public class XMLParser {

	private static Logger log = LoggerFactory.getLogger(XMLParser.class);

//	/**
//	 * 从RefunQueryResponseString里面解析出退款订单数据
//	 *
//	 * @param refundQueryResponseString
//	 *            RefundQuery API返回的数据
//	 * @return 因为订单数据有可能是多个，所以返回一个列表
//	 */
//	public static List<RefundOrderData> getRefundOrderList(String refundQueryResponseString)
//			throws IOException, SAXException, ParserConfigurationException {
//		List<RefundOrderData> list = new ArrayList<>();
//		Map<String, Object> map = XMLParser.getMapFromXML(refundQueryResponseString);
//
//		int count = Integer.parseInt((String) map.get("refund_count"));
//		log.info("count:" + count);
//
//		if (count < 1) {
//			return list;
//		}
//		RefundOrderData refundOrderData;
//
//		for (int i = 0; i < count; i++) {
//			refundOrderData = new RefundOrderData();
//			refundOrderData.setOutRefundNo(Util.getStringFromMap(map, "out_refund_no_" + i, ""));
//			refundOrderData.setRefundID(Util.getStringFromMap(map, "refund_id_" + i, ""));
//			refundOrderData.setRefundChannel(Util.getStringFromMap(map, "refund_channel_" + i, ""));
//			refundOrderData.setRefundFee(Util.getIntFromMap(map, "refund_fee_" + i));
//			refundOrderData.setCouponRefundFee(Util.getIntFromMap(map, "coupon_refund_fee_" + i));
//			refundOrderData.setRefundStatus(Util.getStringFromMap(map, "refund_status_" + i, ""));
//			list.add(refundOrderData);
//		}
//		return list;
//	}

	public static Map<String, Object> getMapFromXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {
		// 这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream is = Util.getStringStream(xmlString);
		Document document = builder.parse(is);

		// 获取到document里面的全部结点
		NodeList allNodes = document.getFirstChild().getChildNodes();
		Node node;
		Map<String, Object> map = new HashMap<String, Object>();
		int i = 0;
		while (i < allNodes.getLength()) {
			node = allNodes.item(i);
			if (node instanceof Element) {
				map.put(node.getNodeName(), node.getTextContent());
			}
			i++;
		}
		return map;
	}

	/**
	 　　* 将对象直接转换成String类型的 XML输出
	 　　*
	 　　* @param obj
	 　　* @return
	 　　*/
	public static String convertToXml(Object obj) {
		// 创建输出流
		StringWriter sw = new StringWriter();
		try {
			// 利用jdk中自带的转换类实现
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller();
			// 格式化xml输出的格式
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// 将对象转换成输出流形式的xml
			marshaller.marshal(obj, sw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}

	/**
	 　　* 将String类型的xml转换成对象
	 　　*
	 　　* @param clazz
	 　　* @param xmlStr
	 　　* @return
	 　　*/
	public static Object convertXmlStrToObject(Class clazz, String xmlStr) {
		Object xmlObject = null;
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			// 进行将Xml转成对象的核心接口
			Unmarshaller unmarshaller = context.createUnmarshaller();
			StringReader sr = new StringReader(xmlStr);
			xmlObject = unmarshaller.unmarshal(sr);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return xmlObject;
	}
}
