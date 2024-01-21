package com.prigozhaeva.aerocalculations.entity;

import lombok.Data;
import lombok.Value;

import java.util.List;

@Value
public class CachedCurrencyRates {
    List<CurrencyRate> currencyRates;
}
