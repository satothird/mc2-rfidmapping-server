/**
 * 
 */
package net._3tas.em.rfidmapping.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import net._3tas.em.rfidmapping.webapp.service.RFIDMappingService;

/**
 * @author sat3
 *
 */
@Controller
@RequestMapping(value="/api")
public class InformationRetrievalAPIController{
	@Autowired
	private RFIDMappingService service;
	
	@RequestMapping(value="/list")
	public String listRequestHandler(Model model){
		model.addAttribute("currentLocations",service.getAvailableTagsLocation());
		return "api/list";
	}
}
