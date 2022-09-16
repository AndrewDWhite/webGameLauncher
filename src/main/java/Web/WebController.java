package Web;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.arteam.simplejsonrpc.client.builder.AbstractBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@EnableAutoConfiguration
public class WebController {

	static Logger logger = LoggerFactory.getLogger("WebController");

	WebController() {

	}

	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "<a href = '/seeit'>py -3.7-32 generic.py token 8488</a>";
	}

	/*
	 * @RequestMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
	 * 
	 * @ResponseBody byte[] image() throws IOException { return
	 * IOUtils.toByteArray(ClassLoader.getSystemResourceAsStream("image.jpg")); }
	 */

	@RequestMapping("/seeit")
	@ResponseBody
	String seeit() {
		ArrayList<String>values = new ArrayList<String>();
		
		while(!GoGRPCEmulator.myMessages.isEmpty()) {
			values.add(GoGRPCEmulator.myMessages.remove());
		}
		
		return values.toString();
	}

}