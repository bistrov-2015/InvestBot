package com.example.InvestBot.util;

import com.example.InvestBot.applicationConstant.BusinessApplicationMessage;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Component
public class IOUtil {
    public String positionToPrint(String accountName, List<String> pairs){
        return "******************************" +
                "\n" + accountName +
                "\n" + "******************************" + "\n" +
                String.join("\n______________________________\n", pairs) +
                "\n" + "______________________________";
    }
    public static String getDateForInsertToTelegrammMessage(String date){
        return date.format(String.valueOf(DateTimeFormatter.ofPattern(BusinessApplicationMessage.DATE_PATTERN_FOR_TELEGRAMM.getMessage(), new Locale("ru"))));
    }
}
