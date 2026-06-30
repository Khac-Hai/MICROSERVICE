package com.example.pharmacyservice;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@RequestMapping("/api/v1/bill")
public class BillController {

    @Value("${pharmacy.vat-rate}")
    private BigDecimal vatRate;

    @PostMapping
    public BillResponse createBill(@RequestBody BillRequest request) {
        BigDecimal totalAmount = request.totalAmount();
        BigDecimal vatAmount = totalAmount.multiply(vatRate).divide(BigDecimal.valueOf(100));
        BigDecimal finalAmount = totalAmount.add(vatAmount);

        return new BillResponse(totalAmount, vatRate, vatAmount, finalAmount);
    }

    public record BillRequest(BigDecimal totalAmount) {
    }

    public record BillResponse(
            BigDecimal totalAmount,
            BigDecimal vatRate,
            BigDecimal vatAmount,
            BigDecimal finalAmount) {
    }
}
