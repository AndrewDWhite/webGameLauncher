import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import RunnableThread.WebRunner;
import Web.GoGRPCEmulator;

@SpringBootApplication
public class Main {
    static Logger logger = LoggerFactory.getLogger("Main");
    
    public static void main (String[] args) throws Exception {
    	
    /*	try {
            WebRunner threadWeb = new WebRunner();
            threadWeb.run(args);
    	} catch (Exception exception) {
            logger.error("Exception in main: ", exception);
            throw exception;
        }    
    }*/
    
    	GoGRPCEmulator emu = new GoGRPCEmulator();
    	emu.runMe();
    	
    }
}