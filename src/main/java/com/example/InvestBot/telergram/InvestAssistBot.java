package com.example.InvestBot.telergram;

import com.example.InvestBot.applicationConstant.TelegramDisplayConstants;
import com.example.InvestBot.tinkoff.EventHandler;
import com.example.InvestBot.tinkoff.dto.SequrityDto;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetMe;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetMeResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@PropertySource("classpath:application.properties")
public class InvestAssistBot {
    private TelegramBot telegramBot;
    @Value("${bot.token}")
    private String token;
    @Value("${bot.chat.id}")
    private String stringChatId;
    //private Long longChatId = Long.parseLong(stringChatId);
    private final EventHandler eventHandler;// = new EventHandler();


    public InvestAssistBot(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }


    @PostConstruct
    public void InvestAssistBot() throws IOException {
        telegramBot = new TelegramBot(token);

        //this.longChatId = Long.parseLong(this.stringChatId);
    }

    public void listen() {
        telegramBot.setUpdatesListener(list -> {
            list.forEach(element -> telegramBot.execute(getStartKeyboard(element.message().chat().id(), element.message().text())/*new SendMessage(element.message().chat().id(), element.message().text())*/));
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
    public void checkBotStatus() {
        GetMe getMe = new GetMe();
        GetMeResponse botUser = telegramBot.execute(getMe);
        System.out.println("Bot username: " + botUser.toString());
    }

    public SendMessage getStartKeyboard(long chatId, String text) {
        String response = TelegramDisplayConstants.NOT_FOUND;
        switch (text) {
            case (TelegramDisplayConstants.BUTTON_CB):
                response = "курс цб";
                break;
            case (TelegramDisplayConstants.BUTTON_EXCHANGE):

                response = "курс биржи";
                break;
            case (TelegramDisplayConstants.BUTTON_OPERATIONS):
                return getReplyMarkupPositions(chatId, text);
            case (TelegramDisplayConstants.BUTTON_BILL_1):
            case (TelegramDisplayConstants.BUTTON_BILL_2):
            case (TelegramDisplayConstants.BUTTON_BILL_3):
            case (TelegramDisplayConstants.BUTTON_BILL_4):
            case (TelegramDisplayConstants.BUTTON_BILL_5):
                response = eventHandler.getOperations(text);
                //return getReplyMarkupPositions(chatId, getPositionBalanseByAccountName(text));
                break;
            case (TelegramDisplayConstants.BUTTON_BACK):
                return getReplyMarkup(chatId,TelegramDisplayConstants.BUTTON_BACK);
            default:
                response = text;
        }
        return getReplyMarkup(chatId, response);
    }

    public SendMessage getPositionKeyboard(long chatId, String text) {
        switch (text) {
            case (TelegramDisplayConstants.BUTTON_BACK):
                getReplyMarkup(chatId, text);
            default:
                return getReplyMarkupPositions(chatId, getPositionBalanseByAccountName(text));
        }
    }

    public String printFigi() {
        List<SequrityDto> listSequrityDto = eventHandler.getMyPositions();
        String reult = new String();
        for (SequrityDto dto : listSequrityDto
        ) {
            reult = dto.toString();
            telegramBot.execute(new SendMessage(stringChatId, reult));
        }
        return reult;
    }

    public String getPositionBalanseByAccountName(String accountName) {
        SequrityDto dto = eventHandler.getPositionByAccountName(accountName);
        return dto.toString();
    }

    private static SendMessage getReplyMarkup(long chatId, String text) {
        return new SendMessage(chatId, text).replyMarkup(new ReplyKeyboardMarkup(new String[]{TelegramDisplayConstants.BUTTON_CB}, new String[]{TelegramDisplayConstants.BUTTON_EXCHANGE}, new String[]{TelegramDisplayConstants.BUTTON_OPERATIONS}));
    }

    private SendMessage getReplyMarkupPositions(long chatId, String text) {

        String[] accounts = eventHandler.getAccountsNames();
        String[] buttons = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++)
            buttons[i] = accounts[i];
        return new SendMessage(chatId, text).replyMarkup(new ReplyKeyboardMarkup(buttons, new String[]{TelegramDisplayConstants.BUTTON_BACK}).resizeKeyboard(true));
    }


}

