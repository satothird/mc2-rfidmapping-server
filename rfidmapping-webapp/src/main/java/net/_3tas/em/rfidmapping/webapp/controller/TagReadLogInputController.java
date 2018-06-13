/**
 * 
 */
package net._3tas.em.rfidmapping.webapp.controller;

import java.io.IOException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import net._3tas.em.rfidmapping.core.model.TagReadLog;
import net._3tas.em.rfidmapping.webapp.service.RFIDMappingService;

/**
 * @author sat3
 *
 */
@Configuration
@EnableWebSocket
public class TagReadLogInputController extends TextWebSocketHandler implements WebSocketConfigurer{
	@Autowired
	private RFIDMappingService service;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
		registry.addHandler(this,"/input/combinedreadlog").setAllowedOrigins("*");
	}
	
	@Override
	public void handleTextMessage(WebSocketSession session,TextMessage message){
		String responseText="OK";
		try{
			service.acceptTagReadLog(parseAsTagReadLog(message.getPayload()));
		}catch(JSONException e){
			responseText=e.toString();
		}
		try{
			session.sendMessage(new TextMessage("{\"response\":\""+responseText+"\"}"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private TagReadLog parseAsTagReadLog(String json) throws JSONException{
		JSONObject obj=new JSONObject(new JSONTokener(json));
		String tagId=obj.getString("tagId");
		double rssi=obj.getDouble("rssi");
		double readerX=obj.getDouble("readerX");
		double readerY=obj.getDouble("readerY");
		double readerZ=obj.getDouble("readerZ");
		double readerAlpha=obj.getDouble("readerAlpha");
		double readerDelta=obj.getDouble("readerDelta");
		Date readDate=new Date();
		String readingSession=obj.getString("readingSession");
		return new TagReadLog(tagId,rssi,readerX,readerY,readerZ,readerAlpha,readerDelta,readDate,readingSession);
	}
}
