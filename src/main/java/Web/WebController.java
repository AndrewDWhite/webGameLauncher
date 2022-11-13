package Web;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.igdb.apicalypse.APICalypse;
import com.api.igdb.exceptions.RequestException;
import com.api.igdb.request.IGDBWrapper;
import com.api.igdb.request.ProtoRequestKt;
import com.api.igdb.request.TwitchAuthenticator;
import com.api.igdb.utils.Endpoints;
import com.api.igdb.utils.ImageBuilderKt;
import com.api.igdb.utils.ImageSize;
import com.api.igdb.utils.ImageType;
import com.api.igdb.utils.TwitchToken;

import GalaxyStateMachine.ProcessGalaxyResponse;
import Global.Globals;
import LocalClient.ClientWebResults;
import LocalClient.RequestURI;
import proto.Artwork;
import proto.Cover;
import proto.Game;
import proto.GameResult;
import proto.Search;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

@RestController
@EnableAutoConfiguration
public class WebController {

	static Logger logger = LoggerFactory.getLogger("WebController");

	WebController() {

	}

	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "<a href = '/games'>py -3.7-32 generic.py token 8488</a>";
	}

	@RequestMapping("/games")
	@ResponseBody
	String gamesHome() throws RequestException {
		String result = "<html>\n" + "    <head>\n" + "                <meta charset='UTF-8'>\n"
				+ "                <title>Games</title>\n" + "    </head>\n" + "    <body>\n"
				+ "                <script src='https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js'></script>\n"
				+ "                <link rel='stylesheet' type='text/css' href='https://cdn.datatables.net/1.11.0/css/jquery.dataTables.css'>\n"
				+ "                <script type='text/javascript' charset='utf8' src='https://cdn.datatables.net/1.11.0/js/jquery.dataTables.js'></script>\n"
				+ "                <script type='text/javascript' charset='utf8' src='https://cdn.datatables.net/select/1.3.3/js/dataTables.select.js'></script>\n"
				+ "                <script type='text/javascript' charset='utf8' src='/games/script.js'></script>\n"
				+ "                <link rel='stylesheet' type='text/css' href='https://cdn.datatables.net/select/1.3.3/css/select.dataTables.css'>\n"
				+ "                <link rel='stylesheet' type='text/css' href='/games/style.css'>\n" + "\n"
				+ "                <button onclick='myOpenGalaxyFunction()' id ='openGalaxyButton'>Open selected in galaxy</button>\n"
				+ "                <button onclick='getNotificationPluginURI(8488)' id ='openGetNotificationButton'>Open notification in new browser</button>\n"
				+ "                \n" + "                <table id='mytable' class='display'>\n"
				+ "                   <thead>\n<tr>\n";
		// TODO write header for each field
		result = result + "<iframe name='dummyframe' id='dummyframe' style='display: none;'></iframe>";
		result = result + "<th>" + "link to play" + "</th>\n";
		// ProcessGalaxyResponse.idsToTitles
		result = result + "<th>id</th>\n";
		result = result + "<th>title</th>\n";
		result = result + "<th>search on igdb</th>\n";
		result = result + "<th>plugin port</th>\n";
		result = result + "<th>image</th>\n";
		result = result + "</tr>\n</thead>\n<tbody>\n";

		for (Integer port : Globals.plugins.keySet()) {
			for (String key : Globals.plugins.get(port).idsToTitles.keySet()) {
				logger.info(key);
				result = result + "<tr>\n";
				result = result + "<td><form action='/start' target='dummyframe' method='post'>"
						+ "     <input type='hidden' id='port' name='port' value='" + String.valueOf(port) + "'>"
						+ "    <button type='submit' name='id' value='" + StringEscapeUtils.escapeHtml4(key)
						+ "' class='btn-link'>" + StringEscapeUtils.escapeHtml4(key) + "</button>\r\n"
						+ "</form></td>\n";
				result = result + "<td>" + StringEscapeUtils.escapeHtml4(key) + "</td>\n";
				result = result + "<td>\n";
				result = result + StringEscapeUtils.escapeHtml4(ProcessGalaxyResponse.idsToTitles.get(key));
				result = result + "</td>\n";
				// probally want to change spaces to pluses
				result = result + "<td><a href='https://www.igdb.com/search?type=1&q="
						+ StringEscapeUtils.escapeHtml4(ProcessGalaxyResponse.idsToTitles.get(key)).replace(" ", "+")
						+ "'>" + StringEscapeUtils.escapeHtml4(ProcessGalaxyResponse.idsToTitles.get(key)) + "</td>\n";
				result = result + "<td>\n";
				result = result + String.valueOf(port);
				result = result + "</td>\n";
				result = result + "<td>\n";

				logger.info("Enrichment");
				logger.info(StringEscapeUtils.escapeHtml4(ProcessGalaxyResponse.idsToTitles.get(key)));

				result = result + " <script type='text/javascript'>" + "  myStack.push({\"key\" : \""
						+ StringEscapeUtils.escapeHtml4(key) + "\", " + "\"value\":\""
						+ StringEscapeUtils.escapeHtml4(String.valueOf(port)) + "\"});\n" + "</script>\n"

				;
				result = result + "<div id='myCoverImage" + StringEscapeUtils.escapeHtml4(key) + "' />";

				result = result + "</td>\n";
				result = result + "</tr>\n";

			}

		}

		result = result + "</tbody>\n" + "            </table>\n"
				+ "            <h2 id='start'>Press a button on your controller to show</h2>\n"
				+ "            <script type='text/javascript'>\n" + "              $(document).ready( \n"
				+ "                function () {\n" + "                  var myDataTable =  $('#mytable').DataTable(\n"
				+ "                    {\n" + "                      select: {style: 'single'},\n"
				+ "                      columnDefs: [\n" + "                        {visible: false, targets: [1] }\n"
				+ "                       ]\n" + "                    }\n" + "                  );\n"
				+ "                }\n" + "              );\n" + "            </script>\n"
				+ "            <script type='text/javascript'>\n"
				+ "            $.fn.dataTable.Api.register('row().next()', function() {\n"
				+ "                // Current row position\n"
				+ "                var nrp = this.table().rows()[0].indexOf( this.index() ) + 1;\n"
				+ "                // Exists ?\n" + "                if( nrp < 0 ) {\n"
				+ "                    return null;\n" + "                }\n"
				+ "                // Next row index by position\n"
				+ "                var nri = this.table().rows()[0][ nrp ];\n"
				+ "                // Return next row by its index\n"
				+ "                return this.table().row( nri );\n" + "            });\n"
				+ "            $.fn.dataTable.Api.register('row().prev()', function() {\n"
				+ "                // Next row position\n"
				+ "                var prp = this.table().rows()[0].indexOf( this.index() ) - 1;\n"
				+ "                // Exists ?\n" + "                if( prp < 0 ) {\n"
				+ "                    return null;\n" + "                }\n"
				+ "                // Previous row index by position\n"
				+ "                var pri = ( this.table().rows()[0][ prp ] );\n"
				+ "                // Return previous row by its index\n"
				+ "                return this.table().row( pri );\n" + "            });\n" + "            \n"
				+ "            </script>\n" + "            <script type='text/javascript'>\n"
				+ "                var haveEvents = 'GamepadEvent' in window;\n"
				+ "                var haveWebkitEvents = 'WebKitGamepadEvent' in window;\n"
				+ "                var controllers = {};\n" + "                var prevTimestamps = [];\n"
				+ "                var rAF = window.mozRequestAnimationFrame ||\n"
				+ "                  window.webkitRequestAnimationFrame ||\n"
				+ "                  window.requestAnimationFrame;\n" + "                \n"
				+ "                function connecthandler(e) {\n" + "                  addgamepad(e.gamepad);\n"
				+ "                }\n" + "                function addgamepad(gamepad) {\n"
				+ "                  controllers[gamepad.index] = gamepad;\n"
				+ "                  var d = document.createElement('div');\n"
				+ "                  d.setAttribute('id', 'controller' + gamepad.index);\n"
				+ "                  var t = document.createElement('h1');\n"
				+ "                  t.appendChild(document.createTextNode('gamepad: ' + gamepad.id));\n"
				+ "                  d.appendChild(t);\n" + "                  var b = document.createElement('div');\n"
				+ "                  b.className = 'buttons';\n"
				+ "                  for (var i=0; i<gamepad.buttons.length; i++) {\n"
				+ "                    var e = document.createElement('span');\n"
				+ "                    e.className = 'button';\n" + "                    //e.id = 'b' + i;\n"
				+ "                    e.innerHTML = i;\n" + "                    b.appendChild(e);\n"
				+ "                  }\n" + "                  d.appendChild(b);\n"
				+ "                  var a = document.createElement('div');\n"
				+ "                  a.className = 'axes';\n"
				+ "                  for (i=0; i<gamepad.axes.length; i++) {\n"
				+ "                    e = document.createElement('meter');\n"
				+ "                    e.className = 'axis';\n" + "                    //e.id = 'a' + i;\n"
				+ "                    e.setAttribute('min', '-1');\n"
				+ "                    e.setAttribute('max', '1');\n"
				+ "                    e.setAttribute('value', '0');\n" + "                    e.innerHTML = i;\n"
				+ "                    a.appendChild(e);\n" + "                  }\n"
				+ "                  d.appendChild(a);\n"
				+ "                  document.getElementById('start').style.display = 'none';\n"
				+ "                  document.body.appendChild(d);\n" + "                  rAF(updateStatus);\n"
				+ "                }\n" + "                \n" + "                function disconnecthandler(e) {\n"
				+ "                  removegamepad(e.gamepad);\n" + "                }\n" + "                \n"
				+ "                function removegamepad(gamepad) {\n"
				+ "                  var d = document.getElementById('controller' + gamepad.index);\n"
				+ "                  document.body.removeChild(d);\n"
				+ "                  delete controllers[gamepad.index];\n" + "                }\n"
				+ "                \n" + "                function updateStatus() {\n" + "                \n"
				+ "                  scangamepads();\n" + "                  for (j in controllers) {\n"
				+ "                    var controller = controllers[j];\n"
				+ "                    var d = document.getElementById('controller' + j);\n"
				+ "                    var buttons = d.getElementsByClassName('button');\n"
				+ "                    for (var i=0; i<controller.buttons.length; i++) {\n"
				+ "                      var b = buttons[i];\n"
				+ "                      var val = controller.buttons[i];\n"
				+ "                      var pressed = val == 1.0;\n" + "                      var touched = false;\n"
				+ "                      if (typeof(val) == 'object') {\n"
				+ "                        pressed = val.pressed;\n"
				+ "                        if ('touched' in val) {\n"
				+ "                          touched = val.touched;\n" + "                        }\n"
				+ "                        val = val.value;\n" + "                      }\n"
				+ "                      var pct = Math.round(val * 100) + '%';\n"
				+ "                      b.style.backgroundSize = pct + ' ' + pct;\n"
				+ "                      b.className = 'button';\n" + "                      if (pressed) {\n"
				+ "                        b.className += ' pressed';\n" + "                      }\n"
				+ "                      if (touched) {\n" + "                        b.className += ' touched';\n"
				+ "                      }\n" + "                    }\n" + "                \n"
				+ "                    var axes = d.getElementsByClassName('axis');\n"
				+ "                    for (var i=0; i<controller.axes.length; i++) {\n"
				+ "                      var a = axes[i];\n"
				+ "                      a.innerHTML = i + ': ' + controller.axes[i].toFixed(4);\n"
				+ "                      a.setAttribute('value', controller.axes[i]);\n" + "                    }\n"
				+ "                  }\n" + "                  rAF(updateStatus);\n" + "                }\n"
				+ "                \n" + "                function scangamepads() {\n"
				+ "                  var gamepads = navigator.getGamepads ? navigator.getGamepads() : (navigator.webkitGetGamepads ? navigator.webkitGetGamepads() : []);\n"
				+ "                  for (var i = 0; i < gamepads.length; i++) {\n"
				+ "                    if (gamepads[i] && (gamepads[i].index in controllers)) {\n"
				+ "                      controllers[gamepads[i].index] = gamepads[i];\n" + "                    }\n"
				+ "                  }\n" + "                  if (gamepads[0]){\n"
				+ "                      if(!( gamepads[0].timestamp && \n"
				+ "                          (gamepads[0].timestamp === prevTimestamps[i]))) {\n"
				+ "                            prevTimestamps[0] = gamepads[0].timestamp;\n"
				+ "                            myTableUpdatesFunction();\n" + "                      }\n"
				+ "                  }\n" + "                }\n" + "                \n"
				+ "                var myStartTime = Date.now();\n" + "                if (haveEvents) {\n"
				+ "                  window.addEventListener('gamepadconnected', connecthandler);\n"
				+ "                  window.addEventListener('gamepaddisconnected', disconnecthandler);\n"
				+ "                } else if (haveWebkitEvents) {\n"
				+ "                  window.addEventListener('webkitgamepadconnected', connecthandler);\n"
				+ "                  window.addEventListener('webkitgamepaddisconnected', disconnecthandler);\n"
				+ "                } else {\n" + "                  setInterval(scangamepads, 500);\n"
				+ "                }\n" + "                </script>\n" + "            </body>\n"
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

	@RequestMapping(path = "/img", method = RequestMethod.POST)
	@ResponseBody
	String img(String id, String port) throws RequestException {

		logger.info("image request: " + id + " : " + port);
		String result = new String();
		logger.info("logging in for enrichment");
		TwitchAuthenticator tAuth = TwitchAuthenticator.INSTANCE;
		TwitchToken token = tAuth.requestTwitchToken(Globals.twitchClientId, Globals.twitchPassword);

		IGDBWrapper wrapper = IGDBWrapper.INSTANCE;
		wrapper.setCredentials(Globals.twitchClientId, token.getAccess_token());
		logger.info("logged in");

		logger.info(id);
		String mySearchString = StringEscapeUtils.escapeHtml4(Globals.plugins.get(port).idsToTitles.get(id));
		logger.info(mySearchString);

		// String mySearchString = "zelda";//
		// StringEscapeUtils.escapeHtml4(ProcessGalaxyResponse.idsToTitles.get(key));
		logger.info(mySearchString);

		APICalypse apicalypse = new APICalypse().search(mySearchString).fields("*")// "*")
				.limit(1);
		logger.info(apicalypse.buildQuery());

		List<Game> searchResult = ProtoRequestKt.games(wrapper, apicalypse);

		for (Game resultOfSearchTop : searchResult) {
			logger.info(resultOfSearchTop.getAllFields().toString());
			Cover myCover = resultOfSearchTop.getCover();
			String coverId = String.valueOf(myCover.getId());

			apicalypse = new APICalypse().where("id = " + coverId + ";").fields("url");
			logger.info(apicalypse.buildQuery());

			List<Cover> myCoverSearchResult = ProtoRequestKt.covers(wrapper, apicalypse);
			logger.info(myCoverSearchResult.toString());
			for (Cover myNewCover : myCoverSearchResult) {
				result = result + "https:" + myNewCover.getUrl();
			}

			/*
			 * CloseableHttpClient httpclient = HttpClients.createDefault(); try {
			 * HttpUriRequest httppost = RequestBuilder.post() .setUri(new
			 * URI("https://api.igdb.com/v4/covers"))//games .addHeader("Client-ID",
			 * twitchClientId) .addHeader("Authorization", "Bearer "+
			 * token.getAccess_token()) .setEntity(new
			 * StringEntity("fields url; where id="+coverId+";",//apicalypse.buildQuery(),
			 * ContentType.TEXT_HTML)) .build();
			 * 
			 * CloseableHttpResponse response = httpclient.execute(httppost); try {
			 * logger.info(EntityUtils.toString(response.getEntity())); } finally {
			 * response.close(); } } finally { httpclient.close(); }
			 */

		}

		result = "<img alt='cover' src='" + result + "'>";
		logger.info(result);
		return result;

	}

	/*
	 * @RequestMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
	 * 
	 * @ResponseBody byte[] image() throws IOException { return
	 * IOUtils.toByteArray(ClassLoader.getSystemResourceAsStream("image.jpg")); }
	 */

	/*
	 * @RequestMapping("/seeit")
	 * 
	 * @ResponseBody String seeit() { ArrayList<String>values = new
	 * ArrayList<String>();
	 * 
	 * while(!GoGRPCEmulator.myMessages.isEmpty()) {
	 * values.add(StringEscapeUtils.escapeHtml4(GoGRPCEmulator.myMessages.remove()))
	 * ; }
	 * 
	 * return values.toString(); }
	 */

	// TODO add security for this to only launch for authorized
	@RequestMapping(path = "/start", method = RequestMethod.POST)
	@ResponseBody
	String start(String id, String port) {
		logger.info("request: " + id + " " + port);
		logger.info(String.valueOf(Integer.parseInt(port)));
		Globals.plugins.get(Integer.parseInt(port)).emulator.requestedGameIdRuns.add(id);
		return "running : " + id + " from: " + port;
	}

	// TODO add security for this to only launch for authorized
	@RequestMapping(path = "/notificationURINext", method = RequestMethod.POST)
	@ResponseBody
	String notificationURINext(String port) throws NumberFormatException, InterruptedException {
		logger.info("request notification next");
		LinkedTransferQueue<HashMap<String, RequestURI>> myQueue = Globals.plugins
				.get(Integer.parseInt(port)).pluginURINotifications;
		
		LinkedTransferQueue<HashMap<String,ClientWebResults>> outBoundQueue = Globals.plugins
				.get(Integer.parseInt(port)).emulator.webPluginUriResults; 
		
		
		logger.info(String.valueOf(myQueue.size()));
		RequestURI myResult = new RequestURI("","");
		if (myQueue.size() > 0) {
			HashMap<String, RequestURI> myCurrentEntry = myQueue.take();
			logger.info(myCurrentEntry.toString());
			
			for (String myCurrentKey : myCurrentEntry.keySet()) {
				myResult = myCurrentEntry.get(myCurrentKey);
				ClientWebResults myClientWebResults = LocalClient.LocalClient.run(myResult);
				HashMap<String, ClientWebResults> myReturnResult = new HashMap<String, ClientWebResults>();
				//TODO fill client results with data from browser 
				//myClientWebResults.setURI(port);
				myReturnResult.put(myCurrentKey, myClientWebResults );
				outBoundQueue.put(myReturnResult);
				logger.info("Added data to result processing queue");
			}
		}

		// TODO remove any unnecessary content
		logger.info(myResult.get_start_uri());
		logger.info(myResult.get_end_regex());
		return myResult.get_start_uri();
	}
	
	

}