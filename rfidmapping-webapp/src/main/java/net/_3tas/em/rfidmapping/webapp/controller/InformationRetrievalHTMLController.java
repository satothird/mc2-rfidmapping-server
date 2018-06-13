/**
 * 
 */
package net._3tas.em.rfidmapping.webapp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import net._3tas.em.rfidmapping.core.exception.NoSuchTagException;
import net._3tas.em.rfidmapping.webapp.service.RFIDMappingService;

/**
 * @author sat3
 *
 */
@Controller
@RequestMapping(value="/html")
public class InformationRetrievalHTMLController{
	@Autowired
	private RFIDMappingService service;

	@RequestMapping(value="",method=RequestMethod.GET)
	public String indexRequestHandler(Model model){
		model.addAttribute("currentLocations",service.getAvailableTagsLocation());
		model.addAttribute("loggedIDs",service.getLoggedTagIDs());
		return "html/index";
	}
	
	@RequestMapping(value="/taginfo",method=RequestMethod.GET)
	public String taginfoRequestHandler(Model model,WebRequest req){
		String tagId=req.getParameter("id");
		if(tagId!=null){
			try{
				tagId=URLDecoder.decode(req.getParameter("id"),"UTF-8");
			}catch(UnsupportedEncodingException e){
			}
		}else{
			return errorHandler(model,"Tag ID is not specified.");
		}
		model.addAttribute("tagId",tagId);
		try{
			model.addAttribute("currentLocation",service.getCurrentTagLocation(tagId));
		}catch(NoSuchTagException e){
			model.addAttribute("currentLocation",null);
		}
		try{
			model.addAttribute("readLogs",service.getTagReadLogs(tagId));
		}catch(NoSuchTagException e){
			model.addAttribute("readLogs",null);
		}
		return "html/taginfo";
	}
	
	private String errorHandler(Model model,String errorMsg){
		model.addAttribute("msg",errorMsg);
		return "html/genmsg";
	}
}
