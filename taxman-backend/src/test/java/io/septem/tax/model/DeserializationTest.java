package io.septem.tax.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.septem.tax.model.input.Invoice;
import io.septem.tax.model.input.TaxDetail;
import io.septem.tax.web.ServiceFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DeserializationTest {

    public static ObjectMapper objectMapper;

    @BeforeAll
    public static void setup() {
        objectMapper = new ServiceFactory().newObjectMapper();
    }

    @Test
    public void testInvoice() throws JsonProcessingException {
        Invoice invoice = Invoice.builder()
                .netValue(BigDecimal.valueOf(100))
                .gstPercent(BigDecimal.valueOf(15))
                .customer("my customer")
                .particulars("an invoice")
                .dateIssued(LocalDate.now()).withholdingTaxPercent(BigDecimal.valueOf(20))
                .build();

        String invoiceJsonString = objectMapper.writeValueAsString(invoice);

        System.out.println(invoiceJsonString);

    }
}
