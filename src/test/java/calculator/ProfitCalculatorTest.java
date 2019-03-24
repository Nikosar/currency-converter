package calculator;

import dto.RateDTO;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.Mockito;
import service.ExchangeRateService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ProfitCalculatorTest {

    @Test
    public void calculate() throws IOException {
        LocalDate purchaseDate = LocalDate.of(2019, 1, 1);
        LocalDate sellDate = LocalDate.of(2019, 1, 1);
        Currency usd = Currency.getInstance("USD");
        Currency rub = Currency.getInstance("RUB");

        RateDTO rateDTO = new RateDTO();
        HashMap<String, Double> rates = new HashMap<>();
        rates.put("RUB", 100d);

        rateDTO.setRates(rates);
        ExchangeRateService mock = mock(ExchangeRateService.class);
        when(mock.request(purchaseDate, usd)).thenReturn(rateDTO);
        when(mock.request(sellDate, usd)).thenReturn(rateDTO);

        ProfitCalculator profitCalculator = new ProfitCalculator(mock);
        double profit = profitCalculator.calculate(purchaseDate, sellDate, 1, usd, rub);
        assertEquals(-1d, profit, 0.0000001);

    }
}