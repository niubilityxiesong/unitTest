package com.cleancode.unitTest.controller;

import com.cleancode.unitTest.module.ItemTotalPriceDto;
import com.cleancode.unitTest.service.CartService;
import com.cleancode.unitTest.utils.JsonFileReader;
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
    void should_not_throw_exception_when_calculate_total_price() throws Exception {
        ItemTotalPriceDto itemTotalPriceDto = new ItemTotalPriceDto();
        when(cartService.getTotalPrice(1)).thenReturn(itemTotalPriceDto);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/1/cart/total-price")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void should_not_throw_exception_when_itemDto_is_correct() throws Exception {
        String jsonString = JsonFileReader.toJsonString("correct_itemDto.json");

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/1/cart/item")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonString)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void should_throw_exception_when_itemDto_without_unit_price() throws Exception {
        String jsonString = JsonFileReader.toJsonString("without_unit_price_itemDto.json");

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
    void should_throw_exception_when_itemDto_without_discount() throws Exception {
        String jsonString = JsonFileReader.toJsonString("without_discount_itemDto.json");

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
    void should_throw_exception_when_itemDto_expired_date_in_post() throws Exception {
        String jsonString = JsonFileReader.toJsonString("expired_time_post_itemDto.json");

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