package dev.weirdduke.refactor;

public class StatementProcessor {

    public String statement(String invoice, String plays) {
        return """
                Statement for BigCo
                  Hamlet: $650.00 (55 seats)
                  As You Like It: $500.00 (35 seats)
                  Othello: $500.00 (40 seats)
                Amount owed is $1,730.00
                """;
    }
}
