package service;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import dto.RateDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Currency;
import java.util.stream.Collectors;

/**
 * class send request to ratesapi.io to get foreign exchange rates and currency conversion
 */
@Component
public class ExchangeRateServiceRemote implements ExchangeRateService{

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     *
     * @param date - historical rate date
     * @param base - base exchange currency
     * @param currencyCodes - currency codes to compare
     * @return RateDTO with exchange rates and currency conversion
     * @throws IOException
     */
    public RateDTO request(LocalDate date, Currency base, Collection<Currency> currencyCodes) throws IOException {
        String formattedDate = dateTimeFormatter.format(date);
        String joinedCurrencyCodes = currencyCodes.stream()
                .map(Currency::getCurrencyCode)
                .collect(Collectors.joining(","));

        URL url = null;
        url = new URL("https://ratesapi.io/api/" + formattedDate + "?" +
                "&base=" + base.getCurrencyCode() + "&symbols=" + joinedCurrencyCodes);
        URLConnection connection = url.openConnection();

        RateDTO rateDTO;
        try (InputStream is = connection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            rateDTO = new Gson().fromJson(reader, RateDTO.class);
        }

        return rateDTO;
    }
}
