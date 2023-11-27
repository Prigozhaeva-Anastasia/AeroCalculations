package com.prigozhaeva.aerocalculations.util;

import java.util.HashMap;
import java.util.Map;

public class CityCodeMap {
    private static final Map<String, String> cityCodeMap = new HashMap<>();

    static {
        cityCodeMap.put("DXB", "Дубаи");
        cityCodeMap.put("MSQ", "Минск");
        cityCodeMap.put("SHJ", "Шарджа");
        cityCodeMap.put("KUT", "Кутаиси");
        cityCodeMap.put("EVN", "Ереван");
        cityCodeMap.put("TBS", "Тбилиси");
        cityCodeMap.put("BUS", "Батуми");
        cityCodeMap.put("GYD", "Баку");
        cityCodeMap.put("KRW", "Туркменистан");
        cityCodeMap.put("TAS", "Ташкент");
        cityCodeMap.put("DEL", "Дели");
        cityCodeMap.put("NQZ", "Нурсултан");
        cityCodeMap.put("IST", "Истамбул");
        cityCodeMap.put("AER", "Сочи");
        cityCodeMap.put("KZN", "Казань");
        cityCodeMap.put("LED", "Санкт-Петербург");
        cityCodeMap.put("KGD", "Калининград");
        cityCodeMap.put("DME", "Москва");
        cityCodeMap.put("SVX", "Екатеринбург");
        cityCodeMap.put("SVO", "Москва");
        cityCodeMap.put("VKO", "Москва");
        cityCodeMap.put("FRU", "Бишкек");
        cityCodeMap.put("AYT", "Анталия");
        cityCodeMap.put("HRG", "Хургада");
        cityCodeMap.put("RMF", "Марса-эль-Алам");
        cityCodeMap.put("SSH", "Шарм-эль-Шейх");
        cityCodeMap.put("NOJ", "Ноябрьск");
        cityCodeMap.put("IAR", "Ярославль");
        cityCodeMap.put("PEK", "Пекин");
        cityCodeMap.put("KLF", "Калуга");
        cityCodeMap.put("GOJ", "Нижний Новгород");
        cityCodeMap.put("PEE", "Пермь");
        cityCodeMap.put("ESB", "Анкара");
        cityCodeMap.put("OHS", "Сохар");
        cityCodeMap.put("LXR", "Люксор");
        cityCodeMap.put("LCA", "Ларнака");
        cityCodeMap.put("KUF", "Самара");
        cityCodeMap.put("UFA", "Уфа");
        cityCodeMap.put("ISL", "Стамбул");
        cityCodeMap.put("ADD", "Аддис");
        cityCodeMap.put("DWC", "Дубаи");
        cityCodeMap.put("CEE", "Череповец");
        cityCodeMap.put("SVO", "Шереметьево");
        cityCodeMap.put("CEK", "Челябинск");
    }
    public static Map<String, String> getCityCodeMap() {
        return cityCodeMap;
    }
}
