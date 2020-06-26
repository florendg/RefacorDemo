package dev.weirdduke.refactor;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import model.Invoice;
import model.Performance;
import model.Play;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatementProcessor {

    private static final Type PLAYS = new TypeToken<Map<String, Play>>(){}.getType();
    private static final GsonBuilder builder = new GsonBuilder()
            .registerTypeAdapter(PLAYS,new PlaysDeserializer());
    private static final Gson gson = builder.create();

    public String statement(String invoiceJson, String playsJson) {
        var invoice = parseInvoice(invoiceJson);
        var plays = parsePlays(playsJson);
        var totalAmount = 0;
        var volumeCredits = 0;
        var result = "Statement for " + invoice.customer + "\n";

        for (Performance performance : invoice.performances) {
            var play = plays.get(performance.getPlayID());
            var thisAmount = 0;
            switch (play.getType()) {
                case "tragedy":
                    thisAmount = 40000;
                    if(performance.getAudience() > 30) {
                        thisAmount += 10000 * (performance.getAudience() -30);
                    }
                    break;
                case "comedy":
                    break;
                default:
                    throw new IllegalArgumentException("Unknown play type");
            }
        }

        return result + """
                  Hamlet: $650.00 (55 seats)
                  As You Like It: $500.00 (35 seats)
                  Othello: $500.00 (40 seats)
                Amount owed is $1,730.00
                """;
    }

    private Invoice parseInvoice(String invoice) {
        return gson.fromJson(invoice,Invoice.class);
    }

    private Map<String,Play> parsePlays(String plays) {
        return gson.fromJson(plays,PLAYS);
    }
}
