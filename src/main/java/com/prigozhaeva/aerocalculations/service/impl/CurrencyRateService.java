package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.config.CbrConfig;
import com.prigozhaeva.aerocalculations.entity.CachedCurrencyRates;
import com.prigozhaeva.aerocalculations.entity.CurrencyRate;
import com.prigozhaeva.aerocalculations.exception.CurrencyRateNotFoundException;
import com.prigozhaeva.aerocalculations.exception.CurrencyRateParsingException;
import com.prigozhaeva.aerocalculations.requester.CbrRequester;
import lombok.RequiredArgsConstructor;
import org.ehcache.Cache;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyRateService {
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private final CbrRequester cbrRequester;
    private final CbrConfig cbrConfig;
    private final Cache<LocalDate, CachedCurrencyRates> currencyRateCache;

    public CurrencyRate getCurrencyRate(String currency, LocalDate date) {
        List<CurrencyRate> rates;
        var cachedCurrencyRates =  currencyRateCache.get(date);
        if (cachedCurrencyRates == null) {
            var urlWithParams = String.format("%s?date_req=%s", cbrConfig.getUrl(), DATE_FORMATTER.format(date));
            var ratesAsXml = cbrRequester.getRatesAsXml(urlWithParams);
            rates = parse(ratesAsXml);
            currencyRateCache.put(date, new CachedCurrencyRates(rates));
        } else {
            rates = cachedCurrencyRates.getCurrencyRates();
        }

        return rates.stream().filter(rate -> currency.equals(rate.getCharCode()))
                .findFirst()
                .orElseThrow(() -> new CurrencyRateNotFoundException("Currency Rate not found. Currency:" + currency + ", date:" + date));
    }

    private List<CurrencyRate> parse(String ratesAsXml) {
        var rates = new ArrayList<CurrencyRate>();
        var dbf = DocumentBuilderFactory.newInstance();
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            var db = dbf.newDocumentBuilder();

            try (var reader = new StringReader(ratesAsXml)) {
                Document doc = db.parse(new InputSource(reader));
                doc.getDocumentElement().normalize();
                NodeList list = doc.getElementsByTagName("Valute");
                for (var valuteIdx = 0; valuteIdx < list.getLength(); valuteIdx++) {
                    var node = list.item(valuteIdx);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        var element = (Element) node;
                        var rate = CurrencyRate.builder()
                                .numCode(element.getElementsByTagName("NumCode").item(0).getTextContent())
                                .charCode(element.getElementsByTagName("CharCode").item(0).getTextContent())
                                .nominal(element.getElementsByTagName("Nominal").item(0).getTextContent())
                                .name(element.getElementsByTagName("Name").item(0).getTextContent())
                                .value(BigDecimal.valueOf(Double.parseDouble(element.getElementsByTagName("Value").item(0).getTextContent().replace(",", "."))))
                                .build();
                        rates.add(rate);
                    }
                }
            }
        } catch (Exception ex) {
            throw new CurrencyRateParsingException(ex);
        }
        return rates;
    }
}
