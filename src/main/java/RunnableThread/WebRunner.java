package RunnableThread;

import Web.WebController;
import org.springframework.boot.SpringApplication;

public class WebRunner extends Thread{

    public void run(String[] args){
        SpringApplication.run(WebController.class, args);
    }

}