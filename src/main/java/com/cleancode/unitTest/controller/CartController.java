package com.cleancode.unitTest.controller;

import com.cleancode.unitTest.module.ItemDto;
import com.cleancode.unitTest.service.CartService;
import com.cleancode.unitTest.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private final CustomerService customerService;

    @GetMapping("/{userId}/items")
    public List<ItemDto> getItems(@PathVariable Integer userId) {
        Integer customerId = customerService.getCustomerId(userId);
        return cartService.getUserItems(customerId);
    }
}
