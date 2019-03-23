package service;

import dto.RateDTO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Currency;

public interface ExchangeRateService {
    RateDTO request(LocalDate date, Currency base, Collection<Currency> currencyCodes) throws IOException;
}
