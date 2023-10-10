package com.example.InvestBot.applicationConstant;

public enum ApplicationConstant {
    DATE_PATTERN_FOR_TELEGRAMM("dd MMMM yyyy"),
    NOT_CONTAIN_FIGI("не содержит figt"),
    NOT_INSTRUMENT("не является инструментом");
    final String messege;

    ApplicationConstant(String messege) {
        this.messege = messege;
    }

    public String getMessege() {
        return messege;
    }
}
