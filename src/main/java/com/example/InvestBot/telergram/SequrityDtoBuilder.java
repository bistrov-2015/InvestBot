package com.example.InvestBot.telergram;

import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.models.SecurityPosition;

import java.util.ArrayList;
import java.util.List;

public class SequrityDtoBuilder {
    private SequrityDto sequrityDto;

    /*public SequrityDtoBuilder(SequrityDto sequrityDto){
        this.sequrityDto = sequrityDto;
    }*/
    public SequrityDtoBuilder(InvestApi api){
        this.sequrityDto = new SequrityDto(api);
    }
    public SequrityDto buildSequrityDto(Account account) {
        sequrityDto.setAccount(account);
        sequrityDto.setAccountId(account.getId());
        sequrityDto.setAccountName(account.getName());
        sequrityDto.setListSecurities(sequrityDto.getInvestApi().getOperationsService().getPositionsSync(sequrityDto.getAccountId()).getSecurities());
        List<PositionToPrint> positionsToPrint = new ArrayList<>();
        List<String> listFigi = new ArrayList<>();
        List<Long> listBalance = new ArrayList<>();
        for (SecurityPosition position : sequrityDto.getListSecurities()
        ) {
            positionsToPrint.add(new PositionToPrint(sequrityDto.getInstrumentName(sequrityDto.getInvestApi(), position.getFigi()), String.valueOf(position.getBalance())));
            listFigi.add(sequrityDto.getInstrumentName(sequrityDto.getInvestApi(), position.getFigi()));
            listBalance.add(position.getBalance());
        }
        sequrityDto.setListPositionSToPrint(positionsToPrint);
        sequrityDto.setFigi(listFigi);
        sequrityDto.setBalance(listBalance);
        return sequrityDto;
    }
}
