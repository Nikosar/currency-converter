package aspect;

import dto.RateDTO;
import org.junit.Test;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ExchangeRateCacheAspectTest {

    @Test
    public void recalculateBase() {
        RateDTO rateDTO = new RateDTO();
        HashMap<String, Double> rates = new HashMap<>();
        rates.put("RUB", 50d);
        rates.put("EUR", 1.2);
        rates.put("JPY", 100d);
        rateDTO.setRates(rates);
        rateDTO.setDate("2019-01-01");
        rateDTO.setBase("USD");

        RateDTO recalculateBase = new ExchangeRateCacheAspect().recalculateBase(rateDTO, Currency.getInstance("RUB"));
        Map<String, Double> recalcRates = recalculateBase.getRates();
        assertEquals(2d, recalcRates.get("JPY"), 0.000001);
        assertEquals(0.02d, recalcRates.get("USD"), 0.000001);
        assertEquals(0.024d, recalcRates.get("EUR"), 0.000001);
    }
}