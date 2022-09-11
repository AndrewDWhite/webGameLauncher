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

import java.io.IOException;

@RestController
@EnableAutoConfiguration
public class WebController {

    static Logger logger = LoggerFactory.getLogger("WebController");

    WebController()
    {

    }


    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "<a href = '/ui/Player.html'>Start</a>";
    }


    @RequestMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    byte[] image() throws IOException {
        return IOUtils.toByteArray(ClassLoader.getSystemResourceAsStream("image.jpg"));
    }



}