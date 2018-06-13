/**
 * 
 */
package net._3tas.em.rfidmapping.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net._3tas.em.rfidmapping.webapp.service.RFIDMappingService;

/**
 * @author sat3
 *
 */
@Controller
@RequestMapping(value="/tools")
public class ToolsController{
	@Autowired
	private RFIDMappingService service;
	
	@RequestMapping(value="/redo",method=RequestMethod.GET)
	public String redoRequestHandler(Model model){
		service.redoLocalizationFromRecordedLog();
		model.addAttribute("msg","Localization calculation has been redone.");
		return "html/genmsg";
	}
}
