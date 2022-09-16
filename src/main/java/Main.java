import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import GalaxyStateMachine.ProcessGalaxyResponse;
import Global.Globals;
import RunnableThread.WebRunner;
import Web.GoGRPCEmulator;

@SpringBootApplication
public class Main {
	static Logger logger = LoggerFactory.getLogger("Main");
	
	public static void main(String[] args) throws Exception {

		//TODO dynamically find and add plugins
		int port = 8488;
		 
		ProcessGalaxyResponse myGenericPlugin = new ProcessGalaxyResponse();
		
		Globals.plugins.put(Integer.valueOf(port), myGenericPlugin);
		
		try {
			WebRunner threadWeb = new WebRunner();
			threadWeb.run(args);
		} catch (Exception exception) {
			logger.error("Exception in main: ", exception);
			throw exception;

		}
		for (Integer myPluginKey: Globals.plugins.keySet()) {
		ThreadPoolExecutor executor = 
				  (ThreadPoolExecutor) Executors.newFixedThreadPool(Globals.plugins.size());
				executor.submit(() -> {
					GoGRPCEmulator emu = Globals.plugins.get(myPluginKey).emulator;
					try {
						emu.runMe(port);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				
				//TODO switch to always run on a schedule (also do to above as well)
				
				ScheduledExecutorService executorScheduled = Executors.newScheduledThreadPool(Globals.plugins.size());
				ScheduledFuture<?> future = executorScheduled.scheduleAtFixedRate(() -> {
					ProcessGalaxyResponse responder = Globals.plugins.get(myPluginKey);
					try {
						responder.processQueue();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}, 3000, 3000, TimeUnit.MILLISECONDS);
		}
		
	}
}