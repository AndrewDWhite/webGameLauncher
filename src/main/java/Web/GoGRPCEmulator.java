package Web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedTransferQueue;

import javax.net.ServerSocketFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import GalaxyStateMachine.CurrentState;

public class GoGRPCEmulator {

	static Logger logger = LoggerFactory.getLogger("GoGRPCEmulator");
	static public Boolean initialized = false;

	// Protocol constants
	protected static final String VERSION_2_0 = "2.0";
	protected static final String RESULT = "result";
	protected static final String ERROR = "error";
	protected static final String JSONRPC = "jsonrpc";
	protected static final String ID = "id";
	protected static final String METHOD = "method";
	protected static final String PARAMS = "params";
	
	public static LinkedTransferQueue<String> myMessages = new LinkedTransferQueue<String>();

	private ObjectNode getNode(ValueNode id, String method, JsonNode params) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestNode = mapper.createObjectNode();
		if (!id.isNull()) {
			requestNode.set(ID, id);
		}
		requestNode.put(JSONRPC, VERSION_2_0);
		requestNode.put(METHOD, method);
		if (!params.isNull()) {
			requestNode.set(PARAMS, params);
		}

		return requestNode;
	}

	private String getTextForState(CurrentState myState, long myId) throws JsonProcessingException {
		

		ObjectNode requestNode = getNode(new TextNode(String.valueOf(myId)), myState.remoteMethodRequestToMake(), myState.remoteParametersToMake());// NullNode.instance);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(requestNode);
	}

	public void runMe() throws IOException, InterruptedException {
		// py -3.7-32 generic.py token 8488
		ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(8488);
		logger.info("lets go");
		Socket socket = serverSocket.accept();
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		long myId = 0;

		CurrentState myState = CurrentState.get_capabilities;

		while (!out.checkError()) {

			String json = getTextForState(myState, myId);
			logger.info("start loop");
			out.write(json);
			out.write("\n");
			out.flush();
			myId++;
			myState= myState.nextState();
			logger.info(">> "+ json);
			String read = in.readLine();
			
			myMessages.add(read);
			//TODO something if needed
			logger.info("<< " + read);
			logger.info("end loop");
			//TODO Seems to be needed? or we need to watch states for errors and retry
			Thread.sleep(1000);
		}
		socket.close();

	}
}