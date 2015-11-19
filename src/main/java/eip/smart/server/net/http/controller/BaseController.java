package eip.smart.server.net.http.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@SuppressWarnings("static-method")
public class BaseController {

	@RequestMapping(value = "/panel/", method = RequestMethod.GET)
	public String webpanel() {
		return "/smartweb/index.html";
	}

}