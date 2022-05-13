package com.cleancode.unitTest.service;

import com.cleancode.unitTest.entity.Item;
import com.cleancode.unitTest.module.ItemDto;
import com.cleancode.unitTest.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CartServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void should_return_itemDto_list_when_repository_have_items() {
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(Item.builder().build()));

        final List<ItemDto> items = cartService.getItems();

        assertThat(items.size()).isEqualTo(1);
    }
}
