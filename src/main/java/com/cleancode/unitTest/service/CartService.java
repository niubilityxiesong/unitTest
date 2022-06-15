package com.cleancode.unitTest.service;

import com.cleancode.unitTest.entity.Customer;
import com.cleancode.unitTest.entity.Item;
import com.cleancode.unitTest.exception.ItemNotValidException;
import com.cleancode.unitTest.module.ItemDto;
import com.cleancode.unitTest.module.ItemTotalPriceDto;
import com.cleancode.unitTest.repository.ItemRepository;
import com.cleancode.unitTest.service.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private static final String VIP_DISCOUNT_LIMIT = "100";
    private static final String VIP_DISCOUNT = "10";

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public void setItem(Integer userId, ItemDto itemDto) {
        if (Objects.nonNull(itemDto.getUserId())) {
            throw new ItemNotValidException();
        }

        final Item item = itemMapper.dtoToItem(itemDto);
        item.setUserId(userId);
        itemRepository.save(item);
    }

    public List<ItemDto> getUserItems(Integer customerId) {
        Optional<List<Item>> itemsOptional = itemRepository.findByUserId(customerId);
        if (itemsOptional.isPresent()) {
            List<Item> items = itemsOptional.get();
            return items.stream().map(itemMapper::itemToDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public void grantItem(Integer userId, ItemDto itemDto) {
        if (Objects.isNull(itemDto.getUserId()) || userId.equals(itemDto.getUserId())) {
            throw new ItemNotValidException();
        }
        saveItemToUsers(userId, itemDto);
    }

    @Transactional
    void saveItemToUsers(Integer userId, ItemDto itemDto) {
        final Item userItem = itemMapper.dtoToItem(itemDto);
        itemRepository.save(userItem);
        itemDto.setUserId(userId);
        final Item anotherUserItem = itemMapper.dtoToItem(itemDto);
        itemRepository.save(anotherUserItem);
    }

    public ItemTotalPriceDto getTotalPrice(Customer customer) {
        Optional<List<Item>> optionalUserItems = itemRepository.findByUserId(customer.getId());
        if (optionalUserItems.isEmpty()) {
            return new ItemTotalPriceDto();
        }

        return calculateTotalPrice(customer, optionalUserItems.get());
    }

    private ItemTotalPriceDto calculateTotalPrice(Customer customer, List<Item> userItems) {
        ItemTotalPriceDto itemsTotalPrice = calculateItemsPrice(userItems);
        return calculateVipDiscount(customer.isVip(), itemsTotalPrice);
    }

    private ItemTotalPriceDto calculateVipDiscount(boolean isVip, ItemTotalPriceDto itemsPrice) {
        ItemTotalPriceDto vipTotalPrice = ItemTotalPriceDto
                .builder()
                .totalPrice(itemsPrice.getTotalPrice())
                .discountPrice(itemsPrice.getDiscountPrice())
                .originalPrice(itemsPrice.getOriginalPrice())
                .build();
        if (isVip && itemsPrice.getTotalPrice().compareTo(new BigDecimal(VIP_DISCOUNT_LIMIT)) > 0) {
            vipTotalPrice.setTotalPrice(vipTotalPrice.getTotalPrice().subtract(new BigDecimal(VIP_DISCOUNT)));
            vipTotalPrice.setDiscountPrice(vipTotalPrice.getDiscountPrice().add(new BigDecimal(VIP_DISCOUNT)));
        }
        return vipTotalPrice;
    }

    private ItemTotalPriceDto calculateItemsPrice(List<Item> userItems) {
        ItemTotalPriceDto itemTotalPriceDto = new ItemTotalPriceDto();
        userItems.forEach(item -> {
            BigDecimal decimalDiscount = BigDecimal.valueOf(item.getDiscount());
            itemTotalPriceDto.setOriginalPrice(
                    itemTotalPriceDto.getOriginalPrice().add(item.getUnitPrice())
            );
            itemTotalPriceDto.setTotalPrice(
                    itemTotalPriceDto.getTotalPrice().add(
                            item.getUnitPrice().multiply(decimalDiscount)
                    )
            );
        });
        itemTotalPriceDto.setDiscountPrice(
                itemTotalPriceDto.getOriginalPrice().subtract(itemTotalPriceDto.getTotalPrice())
        );
        return itemTotalPriceDto;
    }
}
