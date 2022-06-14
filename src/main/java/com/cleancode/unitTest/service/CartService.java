package com.cleancode.unitTest.service;

import com.cleancode.unitTest.entity.Customer;
import com.cleancode.unitTest.entity.Item;
import com.cleancode.unitTest.exception.ItemNotValidException;
import com.cleancode.unitTest.module.ItemDto;
import com.cleancode.unitTest.module.ItemTotalPriceDto;
import com.cleancode.unitTest.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cleancode.unitTest.service.mapper.ItemMapper.ITEM_MAPPER;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ItemRepository itemRepository;

    public void setItem(Integer userId, ItemDto itemDto) {
        if (Objects.nonNull(itemDto.getUserId())) {
            throw new ItemNotValidException();
        }

        itemDto.setUserId(userId);
        final Item item = ITEM_MAPPER.dtoToItem(itemDto);
        itemRepository.save(item);
    }

    public List<ItemDto> getUserItems(Integer customerId) {
        Optional<List<Item>> itemsOptional = itemRepository.findByUserId(customerId);
        if (itemsOptional.isPresent()) {
            List<Item> items = itemsOptional.get();
            return items.stream().map(ITEM_MAPPER::itemToDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Transactional
    public void grantItem(Integer userId, ItemDto itemDto) {
        if (Objects.isNull(itemDto.getUserId())) {
            throw new ItemNotValidException();
        }

        if (userId.equals(itemDto.getUserId())) {
            throw new ItemNotValidException();
        }

        final Item item1 = ITEM_MAPPER.dtoToItem(itemDto);
        itemRepository.save(item1);
        itemDto.setUserId(userId);
        final Item item2 = ITEM_MAPPER.dtoToItem(itemDto);
        itemRepository.save(item2);
    }

    public ItemTotalPriceDto getTotalPrice(Customer customer) {
        return null;
    }
}
