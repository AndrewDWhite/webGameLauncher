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

@RestController
@EnableAutoConfiguration
public class WebController {

    static Logger logger = LoggerFactory.getLogger("WebController");

    WebController()
    {

    }


   /* @RequestMapping("/")
    @ResponseBody
    String home() {
        return "<a href = '/ui/Player.html'>Start</a>";
    }

    @RequestMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    byte[] image() throws IOException {
        return IOUtils.toByteArray(ClassLoader.getSystemResourceAsStream("image.jpg"));
    }*/
    
    // Protocol constants
    protected static final String VERSION_2_0 = "2.0";
    protected static final String RESULT = "result";
    protected static final String ERROR = "error";
    protected static final String JSONRPC = "jsonrpc";
    protected static final String ID = "id";
    protected static final String METHOD = "method";
    protected static final String PARAMS = "params";
    private ObjectNode getNode (ValueNode id, String method, JsonNode params){
    	ObjectMapper mapper = new ObjectMapper();
   	 ObjectNode requestNode = mapper.createObjectNode();
        requestNode.put(JSONRPC, VERSION_2_0);
        requestNode.put(METHOD, method);
        requestNode.set(PARAMS, params);
        if (!id.isNull()) {
            requestNode.set(ID, id);
        }
        return requestNode;
    }
    
    //py -3.7-32 generic.py token 8080
    @RequestMapping(value="*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody 
    String player() throws IOException {
    	//TODO recycle Gson object
    //	Gson value = new Gson();
        //return value.toJson("{\"id\":\"0\",\"jsonrpc\":\"2.0\",\"method\":\"get_capabilities\",\"params\":{}}");
    //    return "{\"id\":\"0\",\"jsonrpc\":\"2.0\",\"method\":\"get_capabilities\",\"params\":{}}";
    //}
    	
    ObjectNode requestNode = getNode(NullNode.instance, "get_capabilities", NullNode.instance );
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(requestNode);
    }

}