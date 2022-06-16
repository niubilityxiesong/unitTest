package com.cleancode.unitTest;

import com.cleancode.unitTest.entity.Item;
import com.cleancode.unitTest.exception.CustomerDoNotExistException;
import com.cleancode.unitTest.module.ItemDto;
import com.cleancode.unitTest.repository.ItemRepository;
import com.cleancode.unitTest.service.CartService;
import com.cleancode.unitTest.service.CustomerService;
import com.cleancode.unitTest.service.mapper.ItemMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ServiceDemoTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private CartService cartService;

    @Test
    void mockVoidMethodReturn() {
        doNothing().when(customerService).isValidUser(1);
    }

    @Test
    void mockVoidMethodThrowException() {
        doThrow(CustomerDoNotExistException.class).when(customerService).isValidUser(1);
    }

    @Test
    void mockNullVoidMethodReturnMockData() {
        ItemDto input = ItemDto.builder().build();
        Item mockOutput = Item.builder().build();
        when(itemMapper.dtoToItem(input)).thenReturn(mockOutput);
    }

    @Test
    void assertUsage() {
        assertThat(1).isEqualTo(1);
    }

    @Test
    void verifyUsage() {
        verify((itemRepository), times(1)).save(any());
    }
}
