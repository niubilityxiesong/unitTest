package com.cleancode.unitTest.module;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
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
    private String context;
    private String imageUrl;
    @NotNull(message = "unit price could not be null")
    private BigDecimal unitPrice;
    @NotNull(message = "discount could not be null")
    private Double discount;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]")
    @Future(message = "invalid expired time")
    private LocalDateTime expiredTime;
    private List<ItemLabel> labels;
    private Integer customerId;
}
