package dev.weirdduke.refactor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class StatementProcessorTest {

    private static String INVOICE;
    private static String PLAYS;

    @BeforeAll
    static void loadTestData() throws Exception {
        String path = StatementProcessor.class.getResource("/invoice.json").getFile();
        INVOICE = Files.readString(Path.of(path));
        assertNotNull(INVOICE);

        path=StatementProcessor.class.getResource("/plays.json").getFile();
        PLAYS = Files.readString(Path.of(path));

        assertNotNull(PLAYS);
    }

    @Test
    void shouldGenerateStatementForInvoice() throws Exception {

        StatementProcessor sut = new StatementProcessor();
        assertEquals("""
                Statement for BigCo
                  Hamlet: $650.00 (55 seats)
                  As You Like It: $500.00 (35 seats)
                  Othello: $500.00 (40 seats)
                Amount owed is $1,730.00
                """, sut.statement(INVOICE, PLAYS));
    }
}
