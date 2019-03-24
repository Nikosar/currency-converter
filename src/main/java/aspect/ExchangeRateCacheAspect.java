package aspect;

import dto.RateDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Aspect
@Component
public class ExchangeRateCacheAspect {
    public static final int CACHE_SIZE_LIMIT = 10000;
    private TreeMap<LocalDate, RateDTO> cacheMap = new TreeMap<>();

    @Pointcut(value = "execution(* service.ExchangeRateServiceRemote.request(..))", argNames = "date,base")
    private void exchangeRateRequest() {}

    @Around("exchangeRateRequest()")
    public Object aroundRequest(ProceedingJoinPoint pjp) throws Throwable {
        RateDTO rateDTO = findInCache(pjp);
        if (rateDTO != null) {
            return rateDTO;
        }

        RateDTO responseRate = (RateDTO) pjp.proceed();
        LocalDate requestDate = LocalDate.parse(responseRate.getDate());

        if (cacheMap.size() >= CACHE_SIZE_LIMIT) {
            cacheMap.pollFirstEntry();
        }
        cacheMap.put(requestDate, responseRate);
        return responseRate;
    }

    private RateDTO findInCache(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        LocalDate date = (LocalDate) args[0];
        Currency baseCurrency = (Currency) args[1];

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
            date = date.minusDays(1);
        } else if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            date = date.minusDays(2);
        }

        RateDTO rateDTO = cacheMap.get(date);
        if (rateDTO == null) {
            return null;
        }

        if (rateDTO.getBase().equals(baseCurrency.getCurrencyCode())) {
            return rateDTO;
        } else {
            return recalculateBase(rateDTO, baseCurrency);
        }
    }

    RateDTO recalculateBase(RateDTO rateDTO, Currency newBase) {
        Map<String, Double> rates = rateDTO.getRates();

        Double newBaseCurrencyRate = rates.get(newBase.getCurrencyCode());
        Map<String, Double> newRates = rates.entrySet().stream()
                .filter((e) -> !e.getKey().equals(newBase.getCurrencyCode()))
                .collect(Collectors.toMap(Map.Entry::getKey, (e) -> e.getValue() / newBaseCurrencyRate));
        newRates.put(rateDTO.getBase(), 1 / newBaseCurrencyRate);

        RateDTO newRateDTO = new RateDTO();
        newRateDTO.setDate(rateDTO.getDate());
        newRateDTO.setBase(newBase.getCurrencyCode());
        newRateDTO.setRates(newRates);
        return newRateDTO;
    }

}
