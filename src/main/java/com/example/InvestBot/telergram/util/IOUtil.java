package com.example.InvestBot.telergram.util;

import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class IOUtil {
    public String positionToPrint(String accountName, List<String> pairs){
        return "******************************" +
                "\n" + accountName +
                "\n" + "******************************" + "\n" +
                String.join("\n______________________________\n", pairs) +
                "\n" + "______________________________";
    }
}
