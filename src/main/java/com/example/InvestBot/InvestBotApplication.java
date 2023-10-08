package com.example.InvestBot;

import com.example.InvestBot.telergram.InvestAssistBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

import java.io.IOException;

@SpringBootApplication
public class InvestBotApplication {
	@Autowired
	private InvestAssistBot investAssistBot;

	public static void main(String[] args) {
		SpringApplication.run(InvestBotApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void runBot() {
		investAssistBot.checkBotStatus();
		investAssistBot.listen();
	}
}
/*			InvestAssistBot investAssistBot = new InvestAssistBot();
			investAssistBot.checkBotStatus();
			investAssistBot.listen();



}*/
