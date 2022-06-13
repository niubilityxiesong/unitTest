package com.cleancode.unitTest.controller;

import com.cleancode.unitTest.module.ItemDto;
import com.cleancode.unitTest.service.CartService;
import com.cleancode.unitTest.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
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

    @PostMapping("/{userId}/item")
    public ResponseEntity<String> setItem(@PathVariable Integer userId, @RequestBody ItemDto itemDto) {
        customerService.isValidUser(userId);
        cartService.setItem(userId, itemDto);
        return ResponseEntity.created(URI.create("/cart/" + userId.toString() + "/item")).build();
    }

    @PostMapping("/gift/{userId}/item")
    public ResponseEntity<String> grantItem(@PathVariable Integer userId, @RequestBody ItemDto itemDto) {
        customerService.isValidUser(userId);
        cartService.grantItem(userId, itemDto);
        return ResponseEntity.created(URI.create("/cart/gift/" + userId.toString() + "/item")).build();
    }
}
