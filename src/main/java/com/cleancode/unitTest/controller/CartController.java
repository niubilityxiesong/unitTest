package com.cleancode.unitTest.controller;

import com.cleancode.unitTest.module.ItemDto;
import com.cleancode.unitTest.module.ItemTotalPriceDto;
import com.cleancode.unitTest.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/{customerId}/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/demo")
    public ItemDto forControllerTestDemo(@PathVariable Integer customerId, @RequestBody @Valid ItemDto itemDto) {
        return itemDto;
    }

    @PostMapping("/item")
    public ResponseEntity<String> setItem(@PathVariable Integer customerId, @RequestBody @Valid ItemDto itemDto) {
        cartService.setItem(customerId, itemDto);
        return ResponseEntity.created(URI.create("/cart/" + customerId.toString() + "/item")).build();
    }

    @GetMapping("/total-price")
    public ItemTotalPriceDto calculateTotalPrice(@PathVariable Integer customerId) {
        return cartService.getTotalPrice(customerId);
    }
}
