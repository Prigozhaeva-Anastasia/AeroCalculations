package com.prigozhaeva.aerocalculations.requester;

import com.prigozhaeva.aerocalculations.exception.RequesterException;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class CbrRequesterImpl implements CbrRequester{
    @Override
    public String getRatesAsXml(String url) {
        try {
            var client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception ex) {
            if (ex instanceof  InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RequesterException(ex);
        }
    }
}
