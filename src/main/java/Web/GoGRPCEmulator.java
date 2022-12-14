package Web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.LinkedTransferQueue;

import javax.net.ServerSocketFactory;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import GalaxyStateMachine.CurrentState;
import GalaxyStateMachine.GenerateMessage;
import Global.Globals;
import LocalClient.ClientWebResults;

public class GoGRPCEmulator {

	static Logger logger = LoggerFactory.getLogger("GoGRPCEmulator");
	static public Boolean initialized = false;

	public LinkedTransferQueue<String> myMessages = new LinkedTransferQueue<String>();
	LinkedTransferQueue<String> myOutMessages = new LinkedTransferQueue<String>();

	LinkedTransferQueue<String> requestedGameIdRuns = new LinkedTransferQueue<String>();
	
	public LinkedTransferQueue<HashMap<String,ClientWebResults>> webPluginUriResults = new LinkedTransferQueue<HashMap<String,ClientWebResults>>();

	private void processWebRequests(GenerateMessage myGenerator) throws InterruptedException, JsonProcessingException {
		logger.debug("processing web requests");
		while (!requestedGameIdRuns.isEmpty()) {
			logger.info("Processing requested run");
			String id = requestedGameIdRuns.take();
			logger.info(id);
			String myCommand = myGenerator.generateRunCommand(id);
			logger.info(myCommand);
			myOutMessages.add(myCommand);
		}
	}
	
	private void processWebResponse(GenerateMessage myGenerator) throws InterruptedException, JsonProcessingException {
		logger.debug("processing web responses");
		while (!webPluginUriResults.isEmpty())
		{
			logger.info("web responses to process");
			HashMap<String, ClientWebResults> entry = webPluginUriResults.take();
			LinkedTransferQueue<String> myCommands =  myGenerator.generateProcessedNextStep(entry);
			logger.info(myCommands.toString());
			myOutMessages.addAll(myCommands);
		}
	}


	public void runMe(int port) throws IOException, InterruptedException {
		// py -3.7-32 generic.py token 8488
		ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(port);
		logger.info("lets go");
		Socket socket = serverSocket.accept();
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		CurrentState myState = CurrentState.get_capabilities;
		GenerateMessage myGenerator = new GenerateMessage();
		DateTime timeLastSend = DateTime.now();

		while (!out.checkError()) {

			logger.debug("start loop");
			processWebRequests(myGenerator);
			processWebResponse(myGenerator);
			// If we don't have any queued messages to deal with generate a new one by
			// transitioning to the next state
			if (myOutMessages.isEmpty()) {
				Duration duration = new Duration(timeLastSend, DateTime.now());
				logger.debug(String.valueOf(duration.toStandardSeconds().getSeconds()));
				if (duration.toStandardSeconds().getSeconds() > 3) {

					timeLastSend = DateTime.now();
					String json = myGenerator.getTextForState(myState);
					logger.info(">> " + json);
					myOutMessages.add(json);
					logger.debug("len: " + myOutMessages.size());
					//TODO only change on no errors?
					myState = myState.nextState();
				}
			}
			while (!myOutMessages.isEmpty()) {
				String msg = myOutMessages.remove();
				logger.info("msg: " + msg);
				// Write pending message to client
				out.write(msg);
				out.write("\n");
				out.flush();
				logger.info("flushed");
			}

			

			if (in.ready()) {
				String read = in.readLine();

				myMessages.add(read);
				// TODO add error reading and response messages to queue here
				// TODO something if needed
				logger.info("<< " + read);
			}
			logger.debug("end loop");
			// TODO Seems to be needed? or we need to watch states for errors and retry
			// Thread.sleep(1000);
		}
		socket.close();

	}
}