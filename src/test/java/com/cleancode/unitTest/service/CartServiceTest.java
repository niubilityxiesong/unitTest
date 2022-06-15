package com.cleancode.unitTest.service;

import com.cleancode.unitTest.entity.Customer;
import com.cleancode.unitTest.entity.Item;
import com.cleancode.unitTest.exception.CustomerDoNotExistException;
import com.cleancode.unitTest.exception.ItemNotValidException;
import com.cleancode.unitTest.module.ItemDto;
import com.cleancode.unitTest.module.ItemTotalPriceDto;
import com.cleancode.unitTest.repository.ItemRepository;
import com.cleancode.unitTest.service.mapper.ItemMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CartServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private CartService cartService;

    @Test
    void should_throw_exception_when_customer_validation_get_error() {
        int customerId = 1;
        ItemDto itemDto = ItemDto
                .builder()
                .build();
        doThrow(CustomerDoNotExistException.class).when(customerService).isValidUser(customerId);

        assertThrows(CustomerDoNotExistException.class, () -> cartService.setItem(customerId, itemDto));
    }

    @Test
    void should_throw_exception_when_itemDto_has_user_id() {
        int customerId = 1;
        ItemDto itemDto = ItemDto
                .builder()
                .customerId(2)
                .build();
        doNothing().when(customerService).isValidUser(customerId);

        assertThrows(ItemNotValidException.class, () -> cartService.setItem(customerId, itemDto));
    }

    @Test
    void should_save_item_when_itemDto_has_no_user_id() {
        int customerId = 1;
        doNothing().when(customerService).isValidUser(customerId);
        ItemDto itemDto = ItemDto.builder().build();
        Item expectedItem = Item.builder().build();
        when(itemMapper.dtoToItem(itemDto)).thenReturn(expectedItem);

        cartService.setItem(customerId, itemDto);

        verify((itemRepository), times(1)).save(expectedItem);
    }

    @Test
    void should_save_item_with_id_1_when_input_user_id_with_1() {
        int customerId = 1;
        doNothing().when(customerService).isValidUser(customerId);
        ItemDto itemDto = ItemDto.builder().build();
        Item expectedItem = Item.builder().build();
        when(itemMapper.dtoToItem(itemDto)).thenReturn(expectedItem);

        cartService.setItem(customerId, itemDto);

        verify((itemRepository), times(1)).save(expectedItem);
        assertThat(expectedItem.getCustomerId()).isEqualTo(customerId);
    }

    @Test
    void should_return_total_price_0_when_user_without_item() {
        int customerId = 1;
        final Customer customer = Customer.builder().id(customerId).build();
        when(customerService.getCustomer(customerId)).thenReturn(customer);
        when(itemRepository.findByCustomerId(customer.getId())).thenReturn(Optional.empty());

        final ItemTotalPriceDto totalPriceDto = cartService.getTotalPrice(customerId);

        assertThat(totalPriceDto.getTotalPrice()).isEqualTo(BigDecimal.ZERO);
        assertThat(totalPriceDto.getDiscountPrice()).isEqualTo(BigDecimal.ZERO);
        assertThat(totalPriceDto.getOriginalPrice()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void should_return_total_price_dto_without_any_discount_when_user_with_item_and_without_vip() {
        int customerId = 1;
        final Customer customer = Customer.builder().id(customerId).isVip(false).build();
        when(customerService.getCustomer(customerId)).thenReturn(customer);
        Item item = Item.
                builder()
                .unitPrice(new BigDecimal("1000"))
                .discount(1.0)
                .build();
        when(itemRepository.findByCustomerId(customer.getId()))
                .thenReturn(Optional.of(Collections.singletonList(item)));

        final ItemTotalPriceDto totalPriceDto = cartService.getTotalPrice(customerId);

        assertThat(totalPriceDto.getTotalPrice().compareTo(new BigDecimal("1000"))).isEqualTo(0);
        assertThat(totalPriceDto.getOriginalPrice().compareTo(new BigDecimal("1000"))).isEqualTo(0);
        assertThat(totalPriceDto.getDiscountPrice().compareTo(new BigDecimal("0"))).isEqualTo(0);
    }

    @Test
    void should_return_total_price_dto_with_item_discount_when_user_with_item_and_without_vip() {
        int customerId = 1;
        final Customer customer = Customer
                .builder()
                .id(customerId)
                .isVip(false)
                .build();
        when(customerService.getCustomer(customerId)).thenReturn(customer);
        Item item = Item.
                builder()
                .unitPrice(new BigDecimal("1000"))
                .discount(0.9)
                .build();
        when(itemRepository.findByCustomerId(customer.getId()))
                .thenReturn(Optional.of(Collections.singletonList(item)));

        final ItemTotalPriceDto totalPriceDto = cartService.getTotalPrice(customerId);

        assertThat(totalPriceDto.getTotalPrice().compareTo(new BigDecimal("900"))).isEqualTo(0);
        assertThat(totalPriceDto.getOriginalPrice().compareTo(new BigDecimal("1000"))).isEqualTo(0);
        assertThat(totalPriceDto.getDiscountPrice().compareTo(new BigDecimal("100"))).isEqualTo(0);
    }

    @Test
    void should_return_total_price_dto_with_vip_discount_when_user_with_item_and_with_vip() {
        int customerId = 1;
        final Customer customer = Customer
                .builder()
                .id(customerId)
                .isVip(true)
                .build();
        when(customerService.getCustomer(customerId)).thenReturn(customer);
        Item item = Item.
                builder()
                .unitPrice(new BigDecimal("1000"))
                .discount(1.0)
                .build();
        when(itemRepository.findByCustomerId(customer.getId()))
                .thenReturn(Optional.of(Collections.singletonList(item)));

        final ItemTotalPriceDto totalPriceDto = cartService.getTotalPrice(customerId);

        assertThat(totalPriceDto.getTotalPrice().compareTo(new BigDecimal("990"))).isEqualTo(0);
        assertThat(totalPriceDto.getOriginalPrice().compareTo(new BigDecimal("1000"))).isEqualTo(0);
        assertThat(totalPriceDto.getDiscountPrice().compareTo(new BigDecimal("10"))).isEqualTo(0);
    }

    @Test
    void should_return_total_price_dto_with_item_and_vip_discount_when_user_with_item_and_with_vip() {
        int customerId = 1;
        final Customer customer = Customer
                .builder()
                .id(customerId)
                .isVip(true)
                .build();
        when(customerService.getCustomer(customerId)).thenReturn(customer);
        Item item = Item.
                builder()
                .unitPrice(new BigDecimal("1000"))
                .discount(0.9)
                .build();
        when(itemRepository.findByCustomerId(customer.getId()))
                .thenReturn(Optional.of(Collections.singletonList(item)));

        final ItemTotalPriceDto totalPriceDto = cartService.getTotalPrice(customerId);

        assertThat(totalPriceDto.getTotalPrice().compareTo(new BigDecimal("890"))).isEqualTo(0);
        assertThat(totalPriceDto.getOriginalPrice().compareTo(new BigDecimal("1000"))).isEqualTo(0);
        assertThat(totalPriceDto.getDiscountPrice().compareTo(new BigDecimal("110"))).isEqualTo(0);
    }

    @Test
    void should_return_total_price_dto_with_item_and_vip_discount_when_user_with_more_than_1_items_and_with_vip() {
        int customerId = 1;
        final Customer customer = Customer
                .builder()
                .id(customerId)
                .isVip(true)
                .build();
        when(customerService.getCustomer(customerId)).thenReturn(customer);
        Item apple = Item.
                builder()
                .unitPrice(new BigDecimal("1000"))
                .discount(0.9)
                .build();
        Item book = Item.
                builder()
                .unitPrice(BigDecimal.ONE)
                .discount(1.0)
                .build();
        ArrayList<Item> items = new ArrayList<>();
        items.add(apple);
        items.add(book);
        when(itemRepository.findByCustomerId(customer.getId())).thenReturn(Optional.of(items));

        final ItemTotalPriceDto totalPriceDto = cartService.getTotalPrice(customerId);
        assertThat(totalPriceDto.getTotalPrice().compareTo(new BigDecimal("891"))).isEqualTo(0);
        assertThat(totalPriceDto.getOriginalPrice().compareTo(new BigDecimal("1001"))).isEqualTo(0);
        assertThat(totalPriceDto.getDiscountPrice().compareTo(new BigDecimal("110"))).isEqualTo(0);
    }

}
