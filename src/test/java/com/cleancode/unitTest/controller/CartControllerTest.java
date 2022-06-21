package com.cleancode.unitTest.controller;

import com.cleancode.unitTest.module.ItemDto;
import com.cleancode.unitTest.module.ItemTotalPriceDto;
import com.cleancode.unitTest.service.CartService;
import com.cleancode.unitTest.utils.GsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CartControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void should_return_ok_when_calculate_total_price() throws Exception {
        ItemTotalPriceDto itemTotalPriceDto = new ItemTotalPriceDto();
        when(cartService.getTotalPrice(1)).thenReturn(itemTotalPriceDto);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/1/cart/total-price")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void should_return_ok_when_itemDto_is_correct() throws Exception {
        String itemDtoJsonString = GsonUtils.toJsonString("correct_itemDto.json");
        doNothing().when(cartService).setItem(eq(1), any(ItemDto.class));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/1/cart/item")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(itemDtoJsonString)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void should_return_bad_request_when_itemDto_without_unit_price() throws Exception {
        String jsonString = GsonUtils.toJsonString("without_unit_price_itemDto.json");
        doNothing().when(cartService).setItem(eq(1), any(ItemDto.class));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/1/cart/item")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonString)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void should_return_bad_request_when_itemDto_without_discount() throws Exception {
        String jsonString = GsonUtils.toJsonString("without_discount_itemDto.json");
        doNothing().when(cartService).setItem(eq(1), any(ItemDto.class));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/1/cart/item")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonString)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void should_return_bad_request_when_itemDto_expired_date_in_post() throws Exception {
        String jsonString = GsonUtils.toJsonString("expired_time_post_itemDto.json");
        doNothing().when(cartService).setItem(eq(1), any(ItemDto.class));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/1/cart/item")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonString)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}