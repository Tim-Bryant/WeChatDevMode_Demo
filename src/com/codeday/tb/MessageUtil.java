package com.codeday.tb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.codeday.tb.domain.LocationMessage;
import com.codeday.tb.domain.News;
import com.codeday.tb.domain.NewsMessage;
import com.codeday.tb.domain.TextMessage;
import com.thoughtworks.xstream.XStream;

/**
 * 消息格式的转化工具类
 * 
 * @author liuxf
 * 
 */
public class MessageUtil {

	/**
	 * 消息类型常量
	 */
	public static final String MESSAGE_TEXT = "text"; // 文本
	public static final String MESSAGE_IMAGE = "image"; // 图片
	public static final String MESSAGE_NEWS = "news"; // 图文
	public static final String MESSAGE_VOICE = "voice"; // 语音
	public static final String MESSAGE_VIDEO = "video";// 视频
	public static final String MESSAGE_LINK = "link";// 链接
	public static final String MESSAGE_LOCATION = "location";// 位置

	public static final String MESSAGE_EVENT = "event";// 事件
	public static final String MESSAGE_SUBSCRIBE = "subscribe";// 关注
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";// 取消关注
	public static final String MESSAGE_CLICK = "CLICK";// 点击
	public static final String MESSAGE_VIEW = "VIEW";

	/**
	 * 将微信xml格式的文本转化为集合数据 添加了dom4j-1.6.1.jar
	 * 
	 * @param request
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request) throws DocumentException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();

		// 从request获取输入流
		InputStream in = request.getInputStream();
		Document document = reader.read(in);
		Element rootElement = document.getRootElement();
		List<Element> elements = rootElement.elements();
		for (Element el : elements) {
			map.put(el.getName(), el.getText());
		}
		in.close();
		return map;
	}

	/**
	 * 将文本消息对象转化为微信需要的XML信息 添加了xtream-1.3.1.jar(提供将对象转为xml方法) <xml>
	 * <ToUserName><![CDATA[toUser]]></ToUserName>
	 * <FromUserName><![CDATA[fromUser]]></FromUserName>
	 * <CreateTime>1348831860</CreateTime> <MsgType><![CDATA[text]]></MsgType>
	 * <Content><![CDATA[this is a test]]></Content>
	 * <MsgId>1234567890123456</MsgId> </xml>
	 * 
	 * @param request
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static String textMessageToXml(TextMessage textMessage) {
		XStream stream = new XStream();
		// 默认根元素会以包名做根元素，微信的要求是xml,故需要將根元素替換
		stream.alias("xml", textMessage.getClass());
		String xml = stream.toXML(textMessage);
		return xml;
	}

	/**
	 * 将地址位置消息对象转化为微信需要的XML信息 添加了xtream-1.3.1.jar(提供将对象转为xml方法) <xml> <xml>
	 * <ToUserName><![CDATA[toUser]]></ToUserName>
	 * <FromUserName><![CDATA[fromUser]]></FromUserName>
	 * <CreateTime>1351776360</CreateTime>
	 * <MsgType><![CDATA[location]]></MsgType>
	 * <Location_X>23.134521</Location_X> <Location_Y>113.358803</Location_Y>
	 * <Scale>20</Scale> <Label><![CDATA[位置信息]]></Label>
	 * <MsgId>1234567890123456</MsgId> </xml>
	 * 
	 * @param request
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static String locationMessageToXml(LocationMessage locationMessage) {
		XStream stream = new XStream();
		// 默认根元素会以包名做根元素，微信的要求是xml,故需要將根元素替換
		stream.alias("xml", locationMessage.getClass());
		String xml = stream.toXML(locationMessage);
		return xml;
	}

	/**
	 * 拼接消息
	 * 
	 * @param toUserName
	 * @param fromUserName
	 * @param conetnt
	 * @return
	 */
	public static String initText(String toUserName, String fromUserName, String conetnt) {
		TextMessage textMessage = new TextMessage();
		// 反馈信息给别人，所以人是反的
		textMessage.setFromUserName(toUserName);
		textMessage.setToUserName(fromUserName);
		textMessage.setMsgType(MessageUtil.MESSAGE_TEXT);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setContent(conetnt);
		String message = textMessageToXml(textMessage);
		return message;

	}

	/**
	 * 拼接菜单
	 * 
	 * @return
	 */
	public static String menuText() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("欢迎您的关注，请按照菜单提示进行操作：\n\n");
		stringBuffer.append("1.课程介绍\n");
		stringBuffer.append("2.慕课网介绍介绍\n");
		stringBuffer.append("回复？调出此菜单。");
		return stringBuffer.toString();

	}

	/**
	 * 菜单 1
	 * 
	 * @return
	 */
	public static String firstText() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("本套课程介绍微信公众号开发，主要涉及公众号介绍，编辑模式介绍，开发模式介绍！");
		return stringBuffer.toString();

	}

	/**
	 * 菜单 1
	 * 
	 * @return
	 */
	public static String secondText() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("慕课网是垂直的互联网IT技能免费学习网站。以独家视频教程、在线编程工具、学习计划、问答社区为核心特色。在这里，你可以找到最好的互联网技术牛人，也可以通过免费的在线公开视频课程学习国内领先的互联网IT技术。");
		stringBuffer.append("慕课网课程涵盖前端开发、PHP、Html5、Android、iOS、Swift等IT前沿技术语言，包括基础课程、实用案例、高级分享三大类型，适合不同阶段的学习人群。以纯干货、短视频的形式为平台特点，为在校学生、职场白领提供了一个迅速提升技能、共同分享进步的学习平台。");
		return stringBuffer.toString();

	}

	/**
	 * 图文消息对象转换为XML
	 * 
	 * @param newsMessage
	 * @return
	 */
	public static String newsMessageToXml(NewsMessage newsMessage) {
		XStream stream = new XStream();
		// 默认根元素会以包名做根元素，微信的要求是xml,故需要將根元素替換
		stream.alias("xml", newsMessage.getClass());
		stream.alias("item", new News().getClass());
		String xml = stream.toXML(newsMessage);
		return xml;
	}

	/**
	 * 图文消息的组装
	 * 
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initNewsMessage(String toUserName, String fromUserName) {
		String message = null;
		List<News> newsList = new ArrayList<News>();
		NewsMessage newsMessage = new NewsMessage();
		News news = new News();
		news.setTitle("慕课网介绍");
		news.setDescription("慕课网是垂直的互联网IT技能免费学习网站。以独家视频教程、在线编程工具、学习计划、问答社区为核心特色。在这里，你可以找到最好的互联网技术牛人，也可以通过免费的在线公开视频课程学习国内领先的互联网IT技术。");
		news.setPicUrl("http://timbryant.tunnel.qydev.com/weixin/images/test.jpg");
		news.setUrl("http://timbryant.tunnel.qydev.com/weixin/index.jsp");
		newsList.add(news);

		newsMessage.setFromUserName(toUserName);
		newsMessage.setToUserName(fromUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MessageUtil.MESSAGE_NEWS);
		newsMessage.setArticles(newsList);
		newsMessage.setArticleCount(newsList.size());

		message = newsMessageToXml(newsMessage);
		System.out.println(message);
		return message;
	}

}
