package GalaxyStateMachine;

import java.util.HashMap;
import java.util.concurrent.LinkedTransferQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import LocalClient.ClientWebResults;

public class GenerateMessage {

	static Logger logger = LoggerFactory.getLogger("GenerateMessage");

	public String generateRunCommand(String idGame) throws JsonProcessingException {
		// TODO only allow this if plugin supports
		logger.info("Generating run command");
		JsonNode params;
		ObjectMapper mapper = new ObjectMapper();

		ObjectNode myObjectNode = mapper.createObjectNode();

		myObjectNode.put("game_id", idGame);
		params = myObjectNode;

		ObjectNode myNodeToReturn = getNodeNotification("launch_game", params);
		logger.info("Here generated node");
		String myFinalResult = getTextForNode(myNodeToReturn);
		logger.info(myFinalResult);
		return myFinalResult;

	}

	public LinkedTransferQueue<String> generateProcessedNextStep(HashMap<String, ClientWebResults> webPluginUriResults)
			throws JsonProcessingException {
		LinkedTransferQueue<String> TextPacketsToSend = new LinkedTransferQueue<String>();
		logger.info("Generating web config command");
		for (String myId : webPluginUriResults.keySet()) {

			CurrentState myState = CurrentState.pass_login_credentials;
			HashMap<String, String> myValues = new HashMap<String, String>();
			myValues.put("myURI", webPluginUriResults.get(myId).getUri());
			myState.setValues(myValues);
			String myStringResult = getTextForNode(getNode(new TextNode(String.valueOf(myId)), myState));
			logger.info("[[ " + myStringResult);
			TextPacketsToSend.put(myStringResult);
		}
		return TextPacketsToSend;
	}
	/*
	 * 2022-09-19 22:38:32,580 - galaxy.api.jsonrpc - INFO - Handling request: id=5,
	 * method=pass_login_credentials, params={'cookies': '****', 'credentials':
	 * '****', 'step': ''}
	 */

	// Protocol constants
	protected static final String VERSION_2_0 = "2.0";
	protected static final String RESULT = "result";
	protected static final String ERROR = "error";
	protected static final String JSONRPC = "jsonrpc";
	protected static final String ID = "id";
	protected static final String METHOD = "method";
	protected static final String PARAMS = "params";

	long myId = 0;

	public ObjectNode getNode(String method, JsonNode params) {

		ObjectNode result = getNode(new TextNode(String.valueOf(myId)), method, params);
		myId++;
		return result;
	}

	public ObjectNode getNodeNotification(String method, JsonNode params) {

		ObjectNode result = getNode(NullNode.getInstance(), method, params);
		return result;
	}

	private ObjectNode getNode(ValueNode id, CurrentState myState) {
		return getNode(id, myState.name(), myState.remoteParametersToMake());
	}

	private ObjectNode getNode(ValueNode id, String method, JsonNode params) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestNode = mapper.createObjectNode();
		if (!id.isNull()) {
			logger.info("notification");
			requestNode.set(ID, id);
		}
		requestNode.put(JSONRPC, VERSION_2_0);
		requestNode.put(METHOD, method);
		if (!params.isNull()) {
			requestNode.set(PARAMS, params);
		}

		return requestNode;
	}

	public String getTextForState(CurrentState myState) throws JsonProcessingException {

		ObjectNode requestNode;
		if (!myState.remoteMethodRequestToMakeIsNotification()) {
			requestNode = getNode(myState.remoteMethodRequestToMake(), myState.remoteParametersToMake());
		} else { // NullNode.instance);
			requestNode = getNodeNotification(myState.remoteMethodRequestToMake(), myState.remoteParametersToMake());
		}
		return getTextForNode(requestNode);
	}

	public String getTextForNode(ObjectNode requestNode) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(requestNode);
	}

}
