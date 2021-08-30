package rutyfuty;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Converter {

    public static void main(String[] args) {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CurrenciesBox.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            CurrenciesBox currencyBox =
                    (CurrenciesBox) jaxbUnmarshaller
                            .unmarshal(new URL("http://www.cbr.ru/scripts/XML_daily.asp"));

            List<Currency> currencies = currencyBox.getCurrencies();

            Currency norKrone = currencies
                    .stream()
                    .filter(currency -> "NOK".equals(currency.getCharCode()))
                    .findAny()
                    .orElse(null);
            Currency hunForint = currencies
                    .stream()
                    .filter(currency -> "HUF".equals(currency.getCharCode()))
                    .findAny()
                    .orElse(null);

            BigDecimal singleNorKroneValue =
                    new BigDecimal(norKrone.getValue()
                            .replace(',', '.'))
                            .divide(new BigDecimal(norKrone.getNominal()), RoundingMode.CEILING);

            BigDecimal singleHunForintValue =
                    new BigDecimal(hunForint.getValue()
                            .replace(',', '.'))
                            .divide(new BigDecimal(hunForint.getNominal()), RoundingMode.CEILING);

            System.out.printf("1 %s = %s %s",
                    norKrone.getName(),
                    singleNorKroneValue.divide(singleHunForintValue, RoundingMode.CEILING),
                    hunForint.getName());

        } catch (MalformedURLException | JAXBException e) {
            e.printStackTrace();
        }
    }

}
