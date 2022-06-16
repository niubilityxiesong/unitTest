package com.cleancode.unitTest;

import com.cleancode.unitTest.controller.CartController;
import com.cleancode.unitTest.module.ItemDto;
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

@ExtendWith(SpringExtension.class)
class ControllerDemoTest {
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
    void demo() throws Exception {
        String itemDtoJsonString = GsonUtils.toJsonString("correct_itemDto.json");
        doNothing().when(cartService).setItem(eq(1), any(ItemDto.class));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/1/cart/demo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(itemDtoJsonString)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}