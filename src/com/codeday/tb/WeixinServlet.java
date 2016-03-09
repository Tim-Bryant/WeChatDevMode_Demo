package com.codeday.tb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;
import org.dom4j.DocumentException;

import com.sun.xml.internal.bind.v2.model.core.ID;

/**
 * 微信接入和开发测试
 * 
 * @author liuxf
 */
public class WeixinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WeixinServlet() {
		super();
	}

	/**
	 * 开发者通过检验signature对请求进行校验（下面有校验方式）。
	 * 若确认此次GET请求来自微信服务器，请原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败。
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");

		PrintWriter pw = response.getWriter();
		if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
			pw.print(echostr);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");

			PrintWriter out = response.getWriter();
			Map<String, String> map = MessageUtil.xmlToMap(request);
			String FromUserName = map.get("FromUserName");
			String ToUserName = map.get("ToUserName");
			String MsgType = map.get("MsgType");
			String Content = map.get("Content");
			String message = null;
			// 如果是文本消息
			if (MessageUtil.MESSAGE_TEXT.equals(MsgType)) {
				// 根据用户输入菜单执行相应操作
				if ("1".equals(Content)) {
					message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.firstText());
				} else if ("2".equals(Content)) {
					message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.secondText());
				} else if ("？".equals(Content) || "?".equals(Content)) {
					message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.menuText());
				}else{
					TextMessage textMessage = new TextMessage();
					// 反馈信息给别人，所以人是反的
					textMessage.setFromUserName(ToUserName);
					textMessage.setToUserName(FromUserName);
					textMessage.setMsgType("text");
					textMessage.setCreateTime(new Date().getTime());
					textMessage.setContent("您发送----的消息是：" + Content);
					message = MessageUtil.textMessageToXml(textMessage);
				}
			} else if (MessageUtil.MESSAGE_EVENT.equals(MsgType)) {
				String eventType = map.get("Event");
				// 如果是关注事件
				if (MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)) {
					message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.menuText());
				}
			}
			out.print(message);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

}
