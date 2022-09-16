package GalaxyStateMachine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Web.GoGRPCEmulator;

public class ProcessGalaxyResponse {
	
	static Logger logger = LoggerFactory.getLogger("ProcessGalaxyResponse");
	public GoGRPCEmulator emulator = new GoGRPCEmulator();
	
	//TODO make this so nothing else writes to it
	public static HashMap<String,String> idsToTitles = new HashMap<String, String>();
	
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
			logger.info("id: "+ idNode.asText());
			
			JsonNode resultNode = rootNode.path("result");
			logger.info("result has owned games: "+ resultNode.has("owned_games"));
			
			if (resultNode.has("owned_games")) 
				processOwned(resultNode.path("owned_games"));

		}
		
	}

	public void processOwned(JsonNode resultNode) {
		// owned_games
		Iterator<JsonNode> elements = resultNode.elements();
		while(elements.hasNext()){
			JsonNode mySubNode = elements.next();
			String gameId = mySubNode.path("game_id").asText();
			logger.info("Sub game_id:  = "+gameId);
			String gameTitle = mySubNode.path("game_title").asText();
			logger.info("Sub game_title:  = "+gameTitle);
			
			idsToTitles.put(gameId, gameTitle);
		}
		//{"game_id": "13046c9a85a2311dffd7af7055476f60d580e10b", "game_title": " observer_", "license_info": {"license_type": "SinglePurchase"}}
		
		//TODO
	}
}
