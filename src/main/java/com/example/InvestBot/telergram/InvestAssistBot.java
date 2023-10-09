package com.example.InvestBot.telergram;

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
    public static final String BUTTON_CB = "ЦБ";
    public static final String BUTTON_EXCHANGE = "Биржа";
    public static final String BUTTON_OIL = "Нефть";
    public static final String BUTTON_POSITIONS = "Позиции по счёту";
    public static final String BUTTON_OPERATIONS = "Операции по счёту";
    public static final String BUTTON_BILL_1 = "Брокерский счёт";
    public static final String BUTTON_BILL_2 = "Анти-мейнстрим Aggressive";
    public static final String BUTTON_BILL_3 = "Новая Волна";
    public static final String BUTTON_BACK = "Назад";
    public static final String NOT_FOUND = "Обработчик команды не найден";

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
        String response = NOT_FOUND;
        switch (text) {
            case (BUTTON_OPERATIONS):
                response = "курс цб";
                break;
            case (BUTTON_EXCHANGE):

                response = "курс биржи";
                break;
            case (BUTTON_POSITIONS):
                return getReplyMarkupPositions(chatId, text);
            case (BUTTON_BILL_1):
            case (BUTTON_BILL_2):
            case (BUTTON_BILL_3):
                response = eventHandler.getOperations(text);
                //return getReplyMarkupPositions(chatId, getPositionBalanseByAccountName(text));
                break;
            case (BUTTON_BACK):
                getReplyMarkup(chatId,BUTTON_BACK);
                break;
            default:
                response = text;
        }
        return getReplyMarkup(chatId, response);
    }

    public SendMessage getPositionKeyboard(long chatId, String text) {
        switch (text) {
            case (BUTTON_BILL_1):
            case (BUTTON_BILL_2):
            case (BUTTON_BILL_3):
                return getReplyMarkupPositions(chatId, getPositionBalanseByAccountName(text));
            case (BUTTON_BACK):
                getReplyMarkup(chatId, text);
            default:
                return getReplyMarkup(chatId, text);
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
        return new SendMessage(chatId, text).replyMarkup(new ReplyKeyboardMarkup(new String[]{BUTTON_CB}, new String[]{BUTTON_EXCHANGE}, new String[]{BUTTON_POSITIONS}));
    }

    private SendMessage getReplyMarkupPositions(long chatId, String text) {

        String[] accounts = eventHandler.getAccountsNames();
        String[] buttons = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++)
            buttons[i] = accounts[i];
        return new SendMessage(chatId, text).replyMarkup(new ReplyKeyboardMarkup(buttons, new String[]{BUTTON_BACK}).resizeKeyboard(true));
    }


}

