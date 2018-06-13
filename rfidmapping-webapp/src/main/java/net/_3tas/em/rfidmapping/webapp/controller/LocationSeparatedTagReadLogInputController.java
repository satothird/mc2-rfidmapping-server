/**
 * 
 */
package net._3tas.em.rfidmapping.webapp.controller;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import net._3tas.em.rfidmapping.core.model.InfiniteVector3D;
import net._3tas.em.rfidmapping.core.model.TagReadLog;
import net._3tas.em.rfidmapping.webapp.dao_ext.ReaderLocationDao;
import net._3tas.em.rfidmapping.webapp.service.RFIDMappingService;

/**
 * @author sat3
 *
 */
@Controller
@Configuration
@EnableWebSocket
public class LocationSeparatedTagReadLogInputController extends TextWebSocketHandler implements WebSocketConfigurer,Runnable{
	private class TagReadInput{
		private final String tagId;
		private final double rssi;
		private final Date readDate;
		private final String readerId;
		
		private TagReadInput(String tagId,double rssi,Date readDate,String readerId){
			this.tagId=tagId;
			this.rssi=rssi;
			this.readDate=readDate;
			this.readerId=readerId;
		}
		
		private TagReadLog toTagReadLog(InfiniteVector3D readerLocation){
			return new TagReadLog(tagId,rssi,readerLocation,readDate,readerId);
		}
	}
	
	private class ReaderLocationInput{
		private final String readerId;
		private final InfiniteVector3D readerLocation;
		
		private ReaderLocationInput(String readerId,InfiniteVector3D readerLocation){
			this.readerId=readerId;
			this.readerLocation=readerLocation;
		}
	}
	
	@Autowired
	private RFIDMappingService service;
	@Autowired
	private ReaderLocationDao readerLocationDao;
	@Autowired
	private TaskExecutor taskExecuter;
	private boolean inputThreadRunning=false;
	private ConcurrentLinkedQueue<TagReadLog> inputQueue=new ConcurrentLinkedQueue<TagReadLog>();
	
	@Override
	public void run(){
		while(!inputQueue.isEmpty()){
			service.acceptTagReadLog(inputQueue.poll());
		}
		inputThreadRunning=false;
	}
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
		registry.addHandler(this,"/input/readtaginfo").setAllowedOrigins("*");
	}
	
	@Override
	public void handleTextMessage(WebSocketSession session,TextMessage message){
		String responseText="OK";
		try{
			TagReadInput input=parseAsTagReadInput(message.getPayload());
			InfiniteVector3D readerLocation=readerLocationDao.find(input.readerId);
			if(readerLocation!=null){
				inputQueue.offer(input.toTagReadLog(readerLocation));
				if(!inputThreadRunning){
					inputThreadRunning=true;
					taskExecuter.execute(this);
				}
			}else{
				responseText="Reader location is not avaiable.";
			}
		}catch(JSONException e){
			responseText=e.toString();
		}
		try{
			session.sendMessage(new TextMessage("{\"response\":\""+responseText+"\"}"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/input/readerlocation",method=RequestMethod.POST)
	public void readerLocationUpdateRequestHandler(HttpServletRequest req,HttpServletResponse res){
		try{
			String responseText="OK";
			int responseCode=200;
			try{
				ReaderLocationInput input=parseAsReaderLocationInput(req.getReader().lines().collect(Collectors.joining()));
				readerLocationDao.update(input.readerId,input.readerLocation);
			}catch(JSONException e){
				responseText=e.toString();
				responseCode=400;
			}
			res.setContentType("application/json");
			res.setStatus(responseCode);
			res.getWriter().write("{\"response\":\""+responseText+"\"}");
			res.getWriter().flush();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private TagReadInput parseAsTagReadInput(String json) throws JSONException{
		JSONObject obj=new JSONObject(new JSONTokener(json));
		String tagId=obj.getString("tagId");
		double rssi=obj.getDouble("rssi");
		Date readDate=new Date();
		String readerId=obj.getString("readerId");
		return new TagReadInput(tagId,rssi,readDate,readerId);
	}
	
	private ReaderLocationInput parseAsReaderLocationInput(String json) throws JSONException{
		JSONObject obj=new JSONObject(new JSONTokener(json));
		String readerId=obj.getString("readerId");
		double x=obj.getDouble("x");
		double y=obj.getDouble("y");
		double z=obj.getDouble("z");
		double alpha=obj.getDouble("alpha");
		double delta=obj.getDouble("delta");
		return new ReaderLocationInput(readerId,new InfiniteVector3D(x,y,z,alpha,delta));
	}
}
