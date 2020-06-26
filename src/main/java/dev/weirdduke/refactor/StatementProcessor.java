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
        var result = "Statement for " + invoice.customer + "\n";

        NumberFormat format = NumberFormat.getInstance(Locale.US);
        format.setMinimumFractionDigits(2);
        format.setCurrency(Currency.getInstance("USD"));

        for (Performance performance : invoice.performances) {
            var play = plays.get(performance.getPlayID());
            var thisAmount = 0;
            switch (play.getType()) {
                case "tragedy":
                    thisAmount = 40_000;
                    if (performance.getAudience() > 30) {
                        thisAmount += 1_000 * (performance.getAudience() - 30);
                    }
                    break;
                case "comedy":
                    thisAmount = 30_000;
                    if (performance.getAudience() > 20) {
                        thisAmount += 10_000 + 500 * (performance.getAudience() - 20);
                    }
                    thisAmount += 300 * performance.getAudience();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown play type");
            }
            volumeCredits += Math.max(performance.getAudience() - 30, 0);
            if("comedy".equalsIgnoreCase(play.getType())) {
                volumeCredits += Math.floor(performance.getAudience() / 5.0);
            }
            result += "  " + play.getName() + ": $" + format.format(thisAmount/100.0);
            result += " (" + performance.getAudience() + " seats)\n";
            totalAmount += thisAmount;
        }
        result += "Amount owed is $" + format.format(totalAmount/100.0) +"\n";
        result += "You earned " + volumeCredits + " credits\n";
        return result;
    }

    private Invoice parseInvoice(String invoice) {
        return gson.fromJson(invoice, Invoice.class);
    }

    private Map<String, Play> parsePlays(String plays) {
        return gson.fromJson(plays, PLAYS);
    }
}
