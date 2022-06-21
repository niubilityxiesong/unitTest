package com.cleancode.unitTest.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ItemTotalPriceDto {
    private BigDecimal totalPrice;
    private BigDecimal discountPrice;
    private BigDecimal originalPrice;

    public ItemTotalPriceDto() {
        this.discountPrice = BigDecimal.ZERO;
        this.totalPrice = BigDecimal.ZERO;
        this.originalPrice = BigDecimal.ZERO;
    }

    public ItemTotalPriceDto copy() {
        return ItemTotalPriceDto
                .builder()
                .discountPrice(discountPrice)
                .originalPrice(originalPrice)
                .totalPrice(totalPrice)
                .build();
    }
}
