package com.example.InvestBot.applicationConstant;

public enum BusinessApplicationMessage {
    DATE_PATTERN_FOR_TELEGRAMM("dd MMMM yyyy"),
    NOT_CONTAIN_FIGI("не содержит figt"),
    NOT_INSTRUMENT("не является инструментом");
    final String message;

    BusinessApplicationMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
