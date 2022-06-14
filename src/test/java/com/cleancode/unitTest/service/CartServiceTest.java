package com.cleancode.unitTest.service;

import com.cleancode.unitTest.entity.Item;
import com.cleancode.unitTest.exception.ItemNotValidException;
import com.cleancode.unitTest.module.ItemDto;
import com.cleancode.unitTest.repository.ItemRepository;
import com.cleancode.unitTest.service.mapper.ItemMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CartServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private CartService cartService;

    @Test
    void should_return_0_itemDto_when_user_have_no_item_in_cart() {
        Integer customerId = 1;
        when(itemRepository.findByUserId(customerId)).thenReturn(Optional.of(Collections.emptyList()));

        final List<ItemDto> items = cartService.getUserItems(customerId);

        assertThat(items.size()).isEqualTo(0);
    }

    @Test
    void should_return_1_itemDto_when_user_have_only_one_item_in_cart() {
        Integer customerId = 1;
        when(itemRepository.findByUserId(customerId)).thenReturn(Optional.of(Collections.singletonList(Item.builder().build())));

        final List<ItemDto> items = cartService.getUserItems(customerId);

        assertThat(items.size()).isEqualTo(1);
    }

    @Test
    void should_return_2_itemDto_when_user_have_items_in_cart() {
        Integer customerId = 1;
        ArrayList<Item> itemsList = new ArrayList<>();
        itemsList.add(Item.builder().build());
        itemsList.add(Item.builder().build());
        when(itemRepository.findByUserId(customerId)).thenReturn(Optional.of(itemsList));

        final List<ItemDto> items = cartService.getUserItems(customerId);

        assertThat(items.size()).isEqualTo(2);
    }

    @Test
    void should_throw_exception_when_itemDto_has_user_id() {
        ItemDto itemDto = ItemDto
                .builder()
                .userId(2)
                .build();

        assertThrows(ItemNotValidException.class, () -> cartService.setItem(1, itemDto));
    }

    @Test
    void should_save_item_when_itemDto_has_no_user_id() {
        ItemDto itemDto = ItemDto.builder().build();

        cartService.setItem(1, itemDto);

        verify((itemRepository), times(1)).save(any(Item.class));
    }

    @Test
    void should_save_item_with_id_1_when_input_user_id_is_1() {
        ItemDto itemDto = ItemDto.builder().build();
        Item expected = Item.builder().build();
        when(itemMapper.dtoToItem(itemDto)).thenReturn(expected);

        cartService.setItem(1, itemDto);

        verify((itemRepository), times(1)).save(expected);
        assertThat(expected.getUserId()).isEqualTo(1);
    }

    @Test
    void should_throw_exception_when_itemDto_has_no_user_id() {
        ItemDto itemDto = ItemDto.builder().build();

        assertThrows(ItemNotValidException.class, () -> cartService.grantItem(1, itemDto));
    }

    @Test
    void should_throw_exception_when_itemDto_has_user_id_and_same_with_input_user_id() {
        ItemDto itemDto = ItemDto
                .builder()
                .userId(1)
                .build();

        assertThrows(ItemNotValidException.class, () -> cartService.grantItem(1, itemDto));
    }

    @Test
    void should_save_items_for_both_both_user_when_itemDto_has_user_id_and_not_same_with_input_user_id() {
        ItemDto itemDto = ItemDto
                .builder()
                .userId(1)
                .build();

        cartService.grantItem(2, itemDto);

        verify((itemRepository), times(2)).save(any(Item.class));
    }
}
