package calculator;

import dto.RateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.ExchangeRateService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

@Component
public class ProfitCalculator {
    private static final double SPREAD = 0.005;

    private ExchangeRateService rateRequest;

    @Autowired
    ProfitCalculator(ExchangeRateService rateRequest) {
        this.rateRequest = rateRequest;
    }

    public Double calculate(LocalDate purchaseDate, LocalDate sellDate, double amount, Currency base, Currency other)
            throws IOException {
        List<Currency> currencyCode = Collections.singletonList(other);
        RateDTO purchaseRateDTO = rateRequest.request(purchaseDate, base, currencyCode);
        RateDTO sellRateDTO = rateRequest.request(sellDate, base, currencyCode);

        String currencyCode2 = other.getCurrencyCode();
        Double purchaseRate = purchaseRateDTO.getRates().get(currencyCode2);
        Double sellRate = sellRateDTO.getRates().get(currencyCode2);

        return sellRate * amount * (1 - SPREAD) - purchaseRate * amount * (1 + SPREAD);
    }
}
