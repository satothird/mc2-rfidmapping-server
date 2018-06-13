/**
 * 
 */
package net._3tas.em.rfidmapping.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author sat3
 *
 */
@Controller
@RequestMapping(value="/")
public class IndexController{
	@RequestMapping(value="",method=RequestMethod.GET)
	public String indexRequestHandler(){
		return "redirect:/html";
	}
}
