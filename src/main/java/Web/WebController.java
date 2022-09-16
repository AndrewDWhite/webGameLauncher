package Web;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
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

import GalaxyStateMachine.ProcessGalaxyResponse;

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
	
	@RequestMapping("/games")
	@ResponseBody
	String gamesHome() {
		String result = 
				  "<html>\n"
				+ "    <head>\n"
				+ "                <meta charset='UTF-8'>\n"
				+ "                <title>Games</title>\n"
				+ "    </head>\n"
				+ "    <body>\n"
				+ "                <script src='https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js'></script>\n"
				+ "                <link rel='stylesheet' type='text/css' href='https://cdn.datatables.net/1.11.0/css/jquery.dataTables.css'>\n"
				+ "                <script type='text/javascript' charset='utf8' src='https://cdn.datatables.net/1.11.0/js/jquery.dataTables.js'></script>\n"
				+ "                <script type='text/javascript' charset='utf8' src='https://cdn.datatables.net/select/1.3.3/js/dataTables.select.js'></script>\n"
				+ "                <script type='text/javascript' charset='utf8' src='/games/script.js'></script>\n"
				+ "                <link rel='stylesheet' type='text/css' href='https://cdn.datatables.net/select/1.3.3/css/select.dataTables.css'>\n"
				+ "                <link rel='stylesheet' type='text/css' href='/games/style.css'>\n"
				+ "\n"
				+ "                <button onclick='myOpenGalaxyFunction()' id ='openGalaxyButton'>Open selected in galaxy</button>\n"
				+ "                \n"
				+ "                <table id='mytable' class='display'>\n"
				+ "                   <thead>\n<tr>\n"
				;
		//TODO write header for each field
		result = result + "<th>"+"link to online db"+"</th>\n";
		//ProcessGalaxyResponse.idsToTitles
		result = result + "<th>id</th>\n";
		result = result + "<th>title</th>\n";
		result = result + "<th>search on igdb</th>\n";
		result = result + "</tr>\n</thead>\n<tbody>\n";
			for (String key : ProcessGalaxyResponse.idsToTitles.keySet()) {
				logger.info(key);
				result = result + "<tr>\n";
				result = result + "<td><a href='https://gamesdb.gog.com/platforms/test/external_releases/"+key+"'>"+StringEscapeUtils.escapeHtml4(key)+"</td>\n";
				result = result + "<td>"+StringEscapeUtils.escapeHtml4(key)+"</td>\n";
				result = result + "<td>\n";
				result = result + StringEscapeUtils.escapeHtml4(ProcessGalaxyResponse.idsToTitles.get(key));
				result = result + "</td>\n";
				//probally want to change spaces to pluses
				result = result + "<td><a href='https://www.igdb.com/search?type=1&q="+StringEscapeUtils.escapeHtml4(ProcessGalaxyResponse.idsToTitles.get(key)).replace(" ", "+")+"'>"+StringEscapeUtils.escapeHtml4(ProcessGalaxyResponse.idsToTitles.get(key))+"</td>\n";
				result = result + "</tr>\n";
            
			}
		
		result = result +
				"</tbody>\n"
				+ "            </table>\n"
				+ "            <h2 id='start'>Press a button on your controller to show</h2>\n"
				+ "            <script type='text/javascript'>\n"
				+ "              $(document).ready( \n"
				+ "                function () {\n"
				+ "                  var myDataTable =  $('#mytable').DataTable(\n"
				+ "                    {\n"
				+ "                      select: {style: 'single'},\n"
				+ "                      columnDefs: [\n"
				+ "                        {visible: false, targets: [1] }\n"
				+ "                       ]\n"
				+ "                    }\n"
				+ "                  );\n"
				+ "                }\n"
				+ "              );\n"
				+ "            </script>\n"
				+ "            <script type='text/javascript'>\n"
				+ "            $.fn.dataTable.Api.register('row().next()', function() {\n"
				+ "                // Current row position\n"
				+ "                var nrp = this.table().rows()[0].indexOf( this.index() ) + 1;\n"
				+ "                // Exists ?\n"
				+ "                if( nrp < 0 ) {\n"
				+ "                    return null;\n"
				+ "                }\n"
				+ "                // Next row index by position\n"
				+ "                var nri = this.table().rows()[0][ nrp ];\n"
				+ "                // Return next row by its index\n"
				+ "                return this.table().row( nri );\n"
				+ "            });\n"
				+ "            $.fn.dataTable.Api.register('row().prev()', function() {\n"
				+ "                // Next row position\n"
				+ "                var prp = this.table().rows()[0].indexOf( this.index() ) - 1;\n"
				+ "                // Exists ?\n"
				+ "                if( prp < 0 ) {\n"
				+ "                    return null;\n"
				+ "                }\n"
				+ "                // Previous row index by position\n"
				+ "                var pri = ( this.table().rows()[0][ prp ] );\n"
				+ "                // Return previous row by its index\n"
				+ "                return this.table().row( pri );\n"
				+ "            });\n"
				+ "            \\\r\n"
				+ "            </script>\n"
				+ "            <script type='text/javascript'>\n"
				+ "                var haveEvents = 'GamepadEvent' in window;\n"
				+ "                var haveWebkitEvents = 'WebKitGamepadEvent' in window;\n"
				+ "                var controllers = {};\n"
				+ "                var prevTimestamps = [];\n"
				+ "                var rAF = window.mozRequestAnimationFrame ||\n"
				+ "                  window.webkitRequestAnimationFrame ||\n"
				+ "                  window.requestAnimationFrame;\n"
				+ "                \n"
				+ "                function connecthandler(e) {\n"
				+ "                  addgamepad(e.gamepad);\n"
				+ "                }\n"
				+ "                function addgamepad(gamepad) {\n"
				+ "                  controllers[gamepad.index] = gamepad;\n"
				+ "                  var d = document.createElement('div');\n"
				+ "                  d.setAttribute('id', 'controller' + gamepad.index);\n"
				+ "                  var t = document.createElement('h1');\n"
				+ "                  t.appendChild(document.createTextNode('gamepad: ' + gamepad.id));\n"
				+ "                  d.appendChild(t);\n"
				+ "                  var b = document.createElement('div');\n"
				+ "                  b.className = 'buttons';\n"
				+ "                  for (var i=0; i<gamepad.buttons.length; i++) {\n"
				+ "                    var e = document.createElement('span');\n"
				+ "                    e.className = 'button';\n"
				+ "                    //e.id = 'b' + i;\n"
				+ "                    e.innerHTML = i;\n"
				+ "                    b.appendChild(e);\n"
				+ "                  }\n"
				+ "                  d.appendChild(b);\n"
				+ "                  var a = document.createElement('div');\n"
				+ "                  a.className = 'axes';\n"
				+ "                  for (i=0; i<gamepad.axes.length; i++) {\n"
				+ "                    e = document.createElement('meter');\n"
				+ "                    e.className = 'axis';\n"
				+ "                    //e.id = 'a' + i;\n"
				+ "                    e.setAttribute('min', '-1');\n"
				+ "                    e.setAttribute('max', '1');\n"
				+ "                    e.setAttribute('value', '0');\n"
				+ "                    e.innerHTML = i;\n"
				+ "                    a.appendChild(e);\n"
				+ "                  }\n"
				+ "                  d.appendChild(a);\n"
				+ "                  document.getElementById('start').style.display = 'none';\n"
				+ "                  document.body.appendChild(d);\n"
				+ "                  rAF(updateStatus);\n"
				+ "                }\n"
				+ "                \n"
				+ "                function disconnecthandler(e) {\n"
				+ "                  removegamepad(e.gamepad);\n"
				+ "                }\n"
				+ "                \n"
				+ "                function removegamepad(gamepad) {\n"
				+ "                  var d = document.getElementById('controller' + gamepad.index);\n"
				+ "                  document.body.removeChild(d);\n"
				+ "                  delete controllers[gamepad.index];\n"
				+ "                }\n"
				+ "                \n"
				+ "                function updateStatus() {\n"
				+ "                \\\r\n"
				+ "                  scangamepads();\n"
				+ "                  for (j in controllers) {\n"
				+ "                    var controller = controllers[j];\n"
				+ "                    var d = document.getElementById('controller' + j);\n"
				+ "                    var buttons = d.getElementsByClassName('button');\n"
				+ "                    for (var i=0; i<controller.buttons.length; i++) {\n"
				+ "                      var b = buttons[i];\n"
				+ "                      var val = controller.buttons[i];\n"
				+ "                      var pressed = val == 1.0;\n"
				+ "                      var touched = false;\n"
				+ "                      if (typeof(val) == 'object') {\n"
				+ "                        pressed = val.pressed;\n"
				+ "                        if ('touched' in val) {\n"
				+ "                          touched = val.touched;\n"
				+ "                        }\n"
				+ "                        val = val.value;\n"
				+ "                      }\n"
				+ "                      var pct = Math.round(val * 100) + '%';\n"
				+ "                      b.style.backgroundSize = pct + ' ' + pct;\n"
				+ "                      b.className = 'button';\n"
				+ "                      if (pressed) {\n"
				+ "                        b.className += ' pressed';\n"
				+ "                      }\n"
				+ "                      if (touched) {\n"
				+ "                        b.className += ' touched';\n"
				+ "                      }\n"
				+ "                    }\n"
				+ "                \n"
				+ "                    var axes = d.getElementsByClassName('axis');\n"
				+ "                    for (var i=0; i<controller.axes.length; i++) {\n"
				+ "                      var a = axes[i];\n"
				+ "                      a.innerHTML = i + ': ' + controller.axes[i].toFixed(4);\n"
				+ "                      a.setAttribute('value', controller.axes[i]);\n"
				+ "                    }\n"
				+ "                  }\n"
				+ "                  rAF(updateStatus);\n"
				+ "                }\n"
				+ "                \n"
				+ "                function scangamepads() {\n"
				+ "                  var gamepads = navigator.getGamepads ? navigator.getGamepads() : (navigator.webkitGetGamepads ? navigator.webkitGetGamepads() : []);\n"
				+ "                  for (var i = 0; i < gamepads.length; i++) {\n"
				+ "                    if (gamepads[i] && (gamepads[i].index in controllers)) {\n"
				+ "                      controllers[gamepads[i].index] = gamepads[i];\n"
				+ "                    }\n"
				+ "                  }\n"
				+ "                  if (gamepads[0]){\n"
				+ "                      if(!( gamepads[0].timestamp && \n"
				+ "                          (gamepads[0].timestamp === prevTimestamps[i]))) {\n"
				+ "                            prevTimestamps[0] = gamepads[0].timestamp;\n"
				+ "                            myTableUpdatesFunction();\n"
				+ "                      }\n"
				+ "                  }\n"
				+ "                }\n"
				+ "                \n"
				+ "                var myStartTime = Date.now();\n"
				+ "                if (haveEvents) {\n"
				+ "                  window.addEventListener('gamepadconnected', connecthandler);\n"
				+ "                  window.addEventListener('gamepaddisconnected', disconnecthandler);\n"
				+ "                } else if (haveWebkitEvents) {\n"
				+ "                  window.addEventListener('webkitgamepadconnected', connecthandler);\n"
				+ "                  window.addEventListener('webkitgamepaddisconnected', disconnecthandler);\n"
				+ "                } else {\n"
				+ "                  setInterval(scangamepads, 500);\n"
				+ "                }\n"
				+ "                </script>\n"
				+ "            </body>\n"
				+ "            </html>\n";
		
		return result;
	}
	
	
	@RequestMapping("/games/script.js")
    @ResponseBody
    String gameJs() throws IOException {
        return IOUtils.toString(ClassLoader.getSystemResourceAsStream("script.js"));
    }
	
	@RequestMapping("/games/style.css")
    @ResponseBody
    String gameCss() throws IOException {
        return IOUtils.toString(ClassLoader.getSystemResourceAsStream("style.css"));
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
			values.add(StringEscapeUtils.escapeHtml4(GoGRPCEmulator.myMessages.remove()));
		}
		
		return values.toString();
	}

}