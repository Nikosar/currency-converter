package сontroller;

import calculator.ProfitCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Currency;

@RestController
@SpringBootApplication(scanBasePackages = {"calculator", "service"})
@CrossOrigin
public class Controller {
    private final ProfitCalculator calculator;

    @Autowired
    public Controller(ProfitCalculator calculator) {
        this.calculator = calculator;
    }

    public static void main(String[] args) {
        SpringApplication.run(Controller.class);
    }

    @GetMapping(value = "/calculate")
    public Double calculate(String date, Double amount) throws IOException {
        return calculator.calculate(LocalDate.parse(date), LocalDate.now(), amount, Currency.getInstance("USD"),
                Currency.getInstance("RUB"));
    }
}
