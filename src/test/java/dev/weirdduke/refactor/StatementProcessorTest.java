package dev.weirdduke.refactor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class StatementProcessorTest {

    private static String INVOICE;
    private static String PLAYS;

    private StatementProcessor statementProcessor;

    @BeforeAll
    static void loadTestData() throws Exception {
        String path = StatementProcessor.class.getResource("/invoice.json").getFile();
        INVOICE = Files.readString(Path.of(path));
        assertNotNull(INVOICE);

        path = StatementProcessor.class.getResource("/plays.json").getFile();
        PLAYS = Files.readString(Path.of(path));

        assertNotNull(PLAYS);
    }

    @BeforeEach
    void initialize() {
        statementProcessor = new StatementProcessor();
    }

    @Test
    void shouldGenerateStatementForInvoice() throws Exception {
        assertEquals("""
                Statement for BigCo
                  Hamlet: $650.00 (55 seats)
                  As You Like It: $580.00 (35 seats)
                  Othello: $500.00 (40 seats)
                Amount owed is $1,730.00
                You earned 47 credits
                """, statementProcessor.statement(INVOICE, PLAYS));
    }

    @Test
    void shouldThrowExceptionForUnknownPlayInPlays() {
        String invalidInvoice = """
                {
                  "customer": "BigCo",
                  "performances": [
                    {
                      "playID": "dirtyShit",
                      "audience": 33
                    }
                  ]
                }            
                """;
        assertThrows(IllegalArgumentException.class, () -> statementProcessor.statement(invalidInvoice, PLAYS));
    }

    @Test
    void shouldThrowExceptionForUnknownPlayInInvoice() {
        String invalidInvoice = """
                {
                  "customer": "BigCo",
                  "performances": [
                    {
                      "playID": "dirty",
                      "audience": 33
                    }
                  ]
                }            
                """;
        assertThrows(IllegalArgumentException.class, () -> statementProcessor.statement(invalidInvoice, PLAYS));
    }
}
