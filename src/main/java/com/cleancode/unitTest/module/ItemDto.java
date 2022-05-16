package com.cleancode.unitTest.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private String name;
    private String describe;
    private String imageUrl;
    private BigDecimal unitPrice;
    private Double discount;
    private LocalDateTime expiredTime;
    private List<ItemLabel> labels;
}
