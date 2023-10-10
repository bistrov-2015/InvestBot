package com.example.InvestBot.tinkoff.dto;

import com.example.InvestBot.applicationConstant.ApplicationConstant;
import com.example.InvestBot.util.IOUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OperationMessage {
    private String date;
    private String type;
    private String state;
    private String id;
    private BigDecimal payment;
    private String figi;
    private String instrumentName;

    public OperationMessage(String date, String type, String state, String id, BigDecimal payment, String figi, String instrumentName) {
        this.date = date;
        this.type = type;
        this.state = state;
        this.id = id;
        this.payment = payment;
        this.figi = figi;
        this.instrumentName = instrumentName;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public String getFigi() {
        if (figi.isEmpty()) {
            return ApplicationConstant.NOT_CONTAIN_FIGI.getMessege();
        } else
            return figi;
    }

    public void setFigi(String figi) {
        this.figi = figi;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public String toStringForTelegramm(){
        //return IOUtil.getDateForInsertToTelegrammMessage(String.valueOf(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:MM:SSZ")))) +", "+type+", "+instrumentName+", Сумма: "+ payment;
        return date +", "+type+", "+instrumentName+", Сумма: "+ payment;
    }
    public String toStringForLogs(){
        return date+", "+type+", "+instrumentName+", Сумма: "+ payment+", Статус: "+state+", id: "+id;
    }
}
