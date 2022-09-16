import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import GalaxyStateMachine.ProcessGalaxyResponse;
import RunnableThread.WebRunner;
import Web.GoGRPCEmulator;

@SpringBootApplication
public class Main {
	static Logger logger = LoggerFactory.getLogger("Main");

	public static void main(String[] args) throws Exception {

		try {
			WebRunner threadWeb = new WebRunner();
			threadWeb.run(args);
		} catch (Exception exception) {
			logger.error("Exception in main: ", exception);
			throw exception;

		}
		ThreadPoolExecutor executor = 
				  (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
				executor.submit(() -> {
					GoGRPCEmulator emu = new GoGRPCEmulator();
					try {
						emu.runMe();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				
				//TODO switch to always run on a schedule (also do to above as well)
				CountDownLatch lock = new CountDownLatch(1000);

				ScheduledExecutorService executorScheduled = Executors.newScheduledThreadPool(1);
				ScheduledFuture<?> future = executorScheduled.scheduleAtFixedRate(() -> {
					ProcessGalaxyResponse responder = new ProcessGalaxyResponse();
					try {
						responder.processQueue();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    lock.countDown();
				}, 3000, 5000, TimeUnit.MILLISECONDS);

				//lock.await(1000, TimeUnit.MILLISECONDS);
				lock.await();
				future.cancel(true);
		
		
		
		
	}
}