package com.example.InvestBot.tinkoff.dto;

import com.example.InvestBot.util.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.models.SecurityPosition;

import java.util.ArrayList;
import java.util.List;
@Component
public class SequrityDto {
    private String accountId;
    private String accountName;
    private List<SecurityPosition> listSecurities;
    private List<String> figi;
    private List<Long> balance;
    private List<PositionToPrint> listPositionSToPrint;
    private InvestApi investApi;
    private Account account;
    private IOUtil ioUtil;/* = new IOUtil();*/


    public SequrityDto(InvestApi api) {
        this.investApi = api;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setListSecurities(List<SecurityPosition> listSecurities) {
        this.listSecurities = listSecurities;
    }

    public void setFigi(List<String> figi) {
        this.figi = figi;
    }

    public void setBalance(List<Long> balance) {
        this.balance = balance;
    }

    public void setListPositionSToPrint(List<PositionToPrint> listPositionSToPrint) {
        this.listPositionSToPrint = listPositionSToPrint;
    }

    public void setInvestApi(InvestApi investApi) {
        this.investApi = investApi;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getAccountId() {
        return account.getId();
    }

    public String getAccountName() {
        return accountName;
    }

    public List<SecurityPosition> getListSecurities() {
        return listSecurities;
    }

    public List<String> getFigi() {
        return figi;
    }

    public List<Long> getBalance() {
        return balance;
    }

    public List<PositionToPrint> getListPositionSToPrint() {
        return listPositionSToPrint;
    }

    public InvestApi getInvestApi() {
        return investApi;
    }

    public Account getAccount() {
        return account;
    }
    public static String getInstrumentName(@NotNull InvestApi investApi, String figi) {
        return investApi.getInstrumentsService().getInstrumentByFigiSync(figi).getName();
    }

    @Override
    public String toString() {
        List<String> result = new ArrayList<>();
        for (PositionToPrint element : listPositionSToPrint
        ) {
            result.add(element.getPosName() + " => " + element.getPosBalance());
        }
        return ioUtil.positionToPrint(accountName,result);
    }
}
