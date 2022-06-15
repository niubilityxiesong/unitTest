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

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private static final String VIP_DISCOUNT_LIMIT = "100";
    private static final String VIP_DISCOUNT = "10";

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final CustomerService customerService;

    public void setItem(Integer customerId, ItemDto itemDto) {
        customerService.isValidUser(customerId);

        if (Objects.nonNull(itemDto.getCustomerId())) {
            throw new ItemNotValidException();
        }

        final Item item = itemMapper.dtoToItem(itemDto);
        item.setCustomerId(customerId);
        itemRepository.save(item);
    }

    public ItemTotalPriceDto getTotalPrice(Integer customerId) {
        Customer customer = customerService.getCustomer(customerId);
        Optional<List<Item>> optionalUserItems = itemRepository.findByCustomerId(customer.getId());
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
