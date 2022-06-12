package com.cleancode.unitTest.service;

import com.cleancode.unitTest.entity.Item;
import com.cleancode.unitTest.module.ItemDto;
import com.cleancode.unitTest.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cleancode.unitTest.service.mapper.ItemMapper.ITEM_MAPPER;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ItemRepository itemRepository;

    public List<ItemDto> getUserItems(Integer customerId) {
        Optional<List<Item>> itemsOptional = itemRepository.findByUserId(customerId);
        if (itemsOptional.isPresent()) {
            List<Item> items = itemsOptional.get();
            return items.stream().map(ITEM_MAPPER::toDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
