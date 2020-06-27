package dev.weirdduke.refactor;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import model.Invoice;
import model.Performance;
import model.Play;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.*;

public class StatementProcessor {

    private static final Type PLAYS = new TypeToken<Map<String, Play>>() {
    }.getType();
    private static final GsonBuilder builder = new GsonBuilder()
            .registerTypeAdapter(PLAYS, new PlaysDeserializer());
    private static final Gson gson = builder.create();

    public String statement(String invoiceJson, String playsJson) {
        var invoice = parseInvoice(invoiceJson);
        var plays = parsePlays(playsJson);
        var totalAmount = 0;
        var volumeCredits = 0;
        StringBuilder result = new StringBuilder("Statement for " + invoice.customer + "\n");

        NumberFormat format = NumberFormat.getInstance(Locale.US);
        format.setMinimumFractionDigits(2);
        format.setCurrency(Currency.getInstance("USD"));

        for (Performance performance : invoice.performances) {
            var play = plays.get(performance.getPlayID());
            var thisAmount = switch (play.getType()) {
                case "tragedy" -> {
                    var amount = 40_000;
                    if (performance.getAudience() > 30) {
                        amount += 1_000 * (performance.getAudience() - 30);
                    }
                    yield amount;
                }
                case "comedy" -> {
                    var amount = 30_000;
                    if (performance.getAudience() > 20) {
                        amount += 10_000 + 500 * (performance.getAudience() - 20);
                    }
                    amount += 300 * performance.getAudience();
                    yield amount;
                }
                default -> throw new IllegalArgumentException("Unknown play type");
            };
            volumeCredits += Math.max(performance.getAudience() - 30, 0);
            if("comedy".equalsIgnoreCase(play.getType())) {
                volumeCredits += Math.floor(performance.getAudience() / 5.0);
            }
            result.append("  ").append(play.getName()).append(": $").append(format.format(thisAmount / 100.0));
            result.append(" (").append(performance.getAudience()).append(" seats)\n");
            totalAmount += thisAmount;
        }
        result.append("Amount owed is $").append(format.format(totalAmount / 100.0)).append("\n");
        result.append("You earned ").append(volumeCredits).append(" credits\n");
        return result.toString();
    }

    private Invoice parseInvoice(String invoice) {
        return gson.fromJson(invoice, Invoice.class);
    }

    private Map<String, Play> parsePlays(String plays) {
        return gson.fromJson(plays, PLAYS);
    }
}
