package com.cleancode.unitTest.controller;

import com.cleancode.unitTest.module.ItemDto;
import com.cleancode.unitTest.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/items")
    public List<ItemDto> getItems() {
        return cartService.getItems();
    }
}
