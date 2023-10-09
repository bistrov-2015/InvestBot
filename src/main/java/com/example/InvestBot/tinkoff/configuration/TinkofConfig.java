package com.example.InvestBot.tinkoff.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.core.InvestApi;

@Configuration
@PropertySource("classpath:application.properties")
public class TinkofConfig {
    @Value("${tinkoff.readonly.token}")
    private String tinkofReadOnlyToken;

    @Bean
    public InvestApi investApi() {
        return InvestApi.createReadonly(tinkofReadOnlyToken);
    }

/*    @Bean
    public Account account() {
        return Account.getDefaultInstance();
    }*/

}
