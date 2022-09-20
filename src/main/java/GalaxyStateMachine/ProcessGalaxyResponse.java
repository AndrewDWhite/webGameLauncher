package GalaxyStateMachine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.LinkedTransferQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Web.GoGRPCEmulator;

public class ProcessGalaxyResponse {

	static Logger logger = LoggerFactory.getLogger("ProcessGalaxyResponse");
	public GoGRPCEmulator emulator = new GoGRPCEmulator();

	// TODO make this so nothing else writes to it
	public static HashMap<String, String> idsToTitles = new HashMap<String, String>();
	public LinkedTransferQueue<HashMap<String,String>> pluginURINotifications = new LinkedTransferQueue<HashMap<String,String>>();

	public void processQueue() throws IOException {
		ArrayList<String> values = new ArrayList<String>();

		logger.info("lets check");

		while (!emulator.myMessages.isEmpty()) {
			String myValue = emulator.myMessages.remove();
			logger.info(myValue);
			values.add(myValue);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(myValue);
			JsonNode idNode = rootNode.path("id");
			String myReadId = idNode.asText();
			logger.info("id: " + myReadId);

			JsonNode resultNode = rootNode.path("result");
			logger.info("result has owned games: " + resultNode.has("owned_games"));

			if (resultNode.has("owned_games"))
				processOwned(resultNode.path("owned_games"));

			logger.info("result has next_steps: " + resultNode.has("next_step"));
			if (resultNode.has("next_step"))
				processNextStep(myReadId, resultNode);

		}

	}

	public void processNextStep(String myReadId, JsonNode resultNode) {

		logger.info(resultNode.toPrettyString());
		String next_stepType = resultNode.path("next_step").asText();
		logger.info("next_step type:  = " + next_stepType);
		// TODO determine if there are any other tpyes
		if (next_stepType.equals("web_session")) {
			JsonNode myAuthParams = resultNode.path("auth_params");
			String myRead_start_uri = myAuthParams.path("start_uri").asText();
			logger.info("URI: " + myRead_start_uri);
			
			HashMap<String,String> myEntryToAdd = new HashMap<String,String>();
			myEntryToAdd.put(myReadId, myRead_start_uri);
			pluginURINotifications.add(myEntryToAdd);
		}

	}
	// {"next_step": "web_session", "auth_params": {"window_title": "GOG Galaxy 2.0
	// - Amazon Games Integration", "window_width": 560, "window_height": 710,
	// "start_uri":
	// "file:///C:/Users/andyn/AppData/Local/GOG.com/Galaxy/plugins/installed/amazon_c2cd2e29-8b02-35a9-86fc-3faf90255857/splash/index.html?view=splash",
	// "end_uri_regex": ".*splash_continue.*"}}

	public void processOwned(JsonNode resultNode) {
		// owned_games
		Iterator<JsonNode> elements = resultNode.elements();
		while (elements.hasNext()) {
			JsonNode mySubNode = elements.next();
			String gameId = mySubNode.path("game_id").asText();
			logger.info("Sub game_id:  = " + gameId);
			String gameTitle = mySubNode.path("game_title").asText();
			logger.info("Sub game_title:  = " + gameTitle);

			idsToTitles.put(gameId, gameTitle);
		}
		// {"game_id": "13046c9a85a2311dffd7af7055476f60d580e10b", "game_title": "
		// observer_", "license_info": {"license_type": "SinglePurchase"}}

		// TODO
	}
}
