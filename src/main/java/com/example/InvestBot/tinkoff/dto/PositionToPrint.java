package com.example.InvestBot.tinkoff.dto;

import org.springframework.stereotype.Component;

public class PositionToPrint {
    private  String posName;
    private String posBalance;

    public PositionToPrint(String posName, String posBalance) {
        this.posName = posName;
        this.posBalance = posBalance;
    }

    public String getPosName() {
        return posName;
    }

    public String getPosBalance() {
        return posBalance;
    }
}
