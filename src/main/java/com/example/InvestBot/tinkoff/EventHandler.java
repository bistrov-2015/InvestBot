package com.example.InvestBot.tinkoff;

import com.example.InvestBot.applicationConstant.BusinessApplicationMessage;
import com.example.InvestBot.tinkoff.builders.SequrityDtoBuilder;
import com.example.InvestBot.tinkoff.dto.OperationMessage;
import com.example.InvestBot.tinkoff.dto.SequrityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.contract.v1.OperationState;
import ru.tinkoff.piapi.core.InvestApi;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static ru.tinkoff.piapi.core.utils.DateUtils.timestampToString;
import static ru.tinkoff.piapi.core.utils.MapperUtils.moneyValueToBigDecimal;

@Component
public class EventHandler {
    static final Logger log = LoggerFactory.getLogger(EventHandler.class);
    private InvestApi api;

    public EventHandler() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        api = InvestApi.create(properties.getProperty("tinkoff.readonly.token"));
    }

    public List<SequrityDto> getMyPositions() {
        List<Account> myAccounts = api.getUserService().getAccountsSync();
        List<SequrityDto> list = new ArrayList<>();
        for (Account account : myAccounts
        ) {
            String id = account.getId();
            SequrityDto sequrityDto = new SequrityDto(api);
            sequrityDto.setAccount(account);
            list.add(sequrityDto);
        }
        return list;
    }

    public SequrityDto getPositionByAccountName(String accountName) {
        //SequrityDto sequrityDto = new SequrityDto(api).buildSequrityDto(getAccounts().get(accountName));
        SequrityDto sequrityDto = new SequrityDtoBuilder(api).buildSequrityDto(getAccounts().get(accountName));
        sequrityDto.setAccount(getAccounts().get(accountName));
        return sequrityDto;
    }

    public Map<String, Account> getAccounts() {
        List<Account> myAccounts = api.getUserService().getAccountsSync();
        Map<String, Account> accountsMap = new HashMap<>();
        for (Account element : myAccounts
        ) {
            accountsMap.put(element.getName(), element);
        }
        return accountsMap;
    }

    public String[] getAccountsNames() {
        List<Account> myAccounts = api.getUserService().getAccountsSync();
        String[] accounts = new String[myAccounts.size()];
        for (int i = 0; i < myAccounts.size(); i++) {
            accounts[i] = myAccounts.get(i).getName();
        }
        return accounts;
    }

    public String getAccountIdByAccountNames(String accountName) {
        return getAccounts().get(accountName).getId();
    }

    public String getOperations(String accountName) {
        List<OperationMessage> returnedOperations = getOperations(api, accountName);
        String result = "";
        for (OperationMessage element : returnedOperations
        ) {
            result += element.toStringForTelegramm() + "\n";
        }
        return result;
    }

    private List<OperationMessage> getOperations(InvestApi api, String accountName) {
        List<OperationMessage> returnedOperations = new ArrayList<>();
        var accounts = api.getUserService().getAccountsSync();
        var mainAccount = accounts.get(0).getId();//2071316935
        var accountId = getAccountIdByAccountNames(accountName);//2071316935
        //Получаем и печатаем список операций клиента
        var operations = api.getOperationsService()
                .getAllOperationsSync(accountId, Instant.now().minus(90, ChronoUnit.DAYS), Instant.now());
        for (int i = 0; i < Math.min(operations.size(), 5); i++) {
            var operation = operations.get(i);
            var date = timestampToString(operation.getDate());
            var state = OperationState.valueOf(operation.getState().name());
            var id = operation.getId();
            var type = operation.getType();
            var payment = moneyValueToBigDecimal(operation.getPayment());
            var figi = operation.getFigi();
            String instrumentName;
            if (!figi.isEmpty()) {
                instrumentName = api.getInstrumentsService().getInstrumentByFigiSync(figi).getName();
            } else instrumentName = BusinessApplicationMessage.NOT_INSTRUMENT.getMessage();
            OperationMessage operationMessage = new OperationMessage(date, type, state.toString(), id, payment, figi, instrumentName);
            log.info(operationMessage.toStringForLogs());
            returnedOperations.add(operationMessage);
        }
        return returnedOperations;
    }
}
