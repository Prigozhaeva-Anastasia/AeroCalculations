package com.prigozhaeva.aerocalculations.parser;

import com.prigozhaeva.aerocalculations.entity.Airline;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class STAXParser {
    public static List<Airline> parse(String path) {
        List<Airline> airlines;
        try {
            XMLStreamReader reader = XMLInputFactory.newInstance()
                    .createXMLStreamReader(new FileInputStream(path));
            airlines = new ArrayList<>();
            Airline airline = new Airline();
            while (reader.hasNext()) {
                reader.next();
                if (reader.getEventType() == XMLEvent.START_ELEMENT) {
                    switch (reader.getName().getLocalPart()) {
                        case "corp":
                            airline = new Airline();
                            break;
                        case "id":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                airline.setId(Long.valueOf(reader.getText()));
                            }
                        case "name":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                airline.setPayerName(reader.getText());
                            }
                            break;
                        case "name_short":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                airline.setName(reader.getText());
                            }
                            break;
                        case "country_code":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                airline.setCountryCode(reader.getText());
                            }
                            break;
                        case "TEL":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                airline.setPhone_number(reader.getText());
                            }
                            break;
                        case "ADR":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                airline.setAddress(reader.getText());
                            }
                            break;
                    }
                }
                if (reader.getEventType() == XMLEvent.END_ELEMENT) {
                    Airline fAirline = airline;
                    boolean elementExists = airlines.stream()
                            .anyMatch(obj -> obj.getId().equals(fAirline.getId()));
                    if (!elementExists) {
                        airlines.add(fAirline);
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return airlines;
    }
}
