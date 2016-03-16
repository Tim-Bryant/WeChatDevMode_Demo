package com.codeday.tb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeday.tb.domain.LocationMessage;
import com.codeday.tb.domain.TextMessage;
import com.codeday.tb.util.HttpRequest;
import com.codeday.tb.util.TokenThread;
import com.codeday.tb.util.WeixinUtil;

/**
 * 微信接入和开发测试
 * 
 * @author liuxf
 */
public class WeixinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);

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
			log.info("===================恭喜你，微信接入成功===================");
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
			// 文本字段
			String Content = map.get("Content");
			// 地址位置字段
			String Location_X = map.get("Location_X");
			String Location_Y = map.get("Location_Y");
			String Scale = map.get("Scale");
			String Label = map.get("Label");

			// 链接信息
			String Url = map.get("Url");
			String message = null;
			// 如果是文本消息
			if (MessageUtil.MESSAGE_TEXT.equals(MsgType)) {
				// 根据用户输入菜单执行相应操作
				if ("1".equals(Content)) {
					message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.firstText());
				} else if ("2".equals(Content)) {
					message = MessageUtil.initNewsMessage(ToUserName, FromUserName);
				} else if ("？".equals(Content) || "?".equals(Content)) {
					message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.menuText());
				} else {
					TextMessage textMessage = new TextMessage();
					// 反馈信息给别人，所以人是反的
					textMessage.setFromUserName(ToUserName);
					textMessage.setToUserName(FromUserName);

					textMessage.setMsgType("text");
					textMessage.setCreateTime(new Date().getTime());
					textMessage.setContent("您发送的消息是：" + Content + ",请输入？号获取操作菜单。\n");
					message = MessageUtil.textMessageToXml(textMessage);
					// System.out.println("ToUserName:"+ToUserName+";FromUserName:"+FromUserName);
					System.out.println(message);
				}
			} else if (MessageUtil.MESSAGE_EVENT.equals(MsgType)) {
				String eventType = map.get("Event");
				// 如果是关注事件
				if (MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)) {
					message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.menuText());
				}
			}// 如果是位置信息
			else if (MessageUtil.MESSAGE_LOCATION.equals(MsgType)) {
				LocationMessage locationMessage = new LocationMessage();
				locationMessage.setFromUserName(ToUserName);
				locationMessage.setToUserName(FromUserName);
				locationMessage.setScale(Scale);
				locationMessage.setLabel(Label);
				locationMessage.setLocation_X(Location_X);
				locationMessage.setLocation_Y(Location_Y);
				System.out.println("位置信息X:" + Location_X + ";Y:" + Location_Y + ";Label:" + Label + ";Scale:" + Scale);
				message = MessageUtil.initText(ToUserName, FromUserName, "位置信息X:" + Location_X + ";Y:" + Location_Y + ";Label:" + Label + ";Scale:" + Scale);
			}// 如果是链接信息
			else if (MessageUtil.MESSAGE_LINK.equals(MsgType)) {
				log.info("缓存的Access_token is :" + TokenThread.accessToken.getToken());
				String str = HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/getcallbackip", "access_token=" + TokenThread.accessToken.getToken());
				JSONObject object = JSONObject.fromObject(str);
				JSONArray jsonArray = object.getJSONArray("ip_list");
				ListIterator listIterator = jsonArray.listIterator();
				for (; listIterator.hasNext();) {
					String str1 = (String) listIterator.next();
					System.out.println(str1);
				}

			}

			// response.sendRedirect("http://www.baidu.com");
			out.print(message);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 初始化方法获取access_token(在tomcat启动时就已经获取到了)
	 */

	@Override
	public void init() throws ServletException {
		// 获取web.xml中配置的参数
		TokenThread.appid = getInitParameter("appid");
		TokenThread.appsecret = getInitParameter("appsecret");

		log.info("weixin api appid:{}", TokenThread.appid);
		log.info("weixin api appsecret:{}", TokenThread.appsecret);

		// 未配置appid、appsecret时给出提示
		if ("".equals(TokenThread.appid) || "".equals(TokenThread.appsecret)) {
			log.error("appid and appsecret configuration error, please check carefully.");
		} else {
			// 启动定时获取access_token的线程
			new Thread(new TokenThread()).start();
		}
	}

	/**
	 * 测试函数
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * JSONObject对象使用测试
		 */
		String json = "{\"ip_key\":[\"w_ap_lxf\",\"w_ademin\"],\"val\":\"xujing\",\"child\":{\"id\":\"0001\",\"name\":\"liuxiaofeng\"}}";
		JSONObject jsonObject = JSONObject.fromObject(json);

		Set<String> keySet = jsonObject.keySet();
		for (String key : keySet) {
			System.out.println(key);
		}

		System.out.println("====================手动输出模式==================");
		JSONArray jsonArray = jsonObject.getJSONArray("ip_key");
		String jsonObject2 = jsonObject.getString("val");
		JSONObject jsonObject3 = jsonObject.getJSONObject("child");
		System.out.println(jsonArray.toString());
		System.out.println(jsonObject2.toString());
		System.out.println(jsonObject3.toString());
		System.out.println(jsonObject3.get("name"));
	}

}
