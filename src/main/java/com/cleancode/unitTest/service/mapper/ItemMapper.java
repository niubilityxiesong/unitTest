package com.cleancode.unitTest.service.mapper;

import com.cleancode.unitTest.entity.Item;
import com.cleancode.unitTest.module.ItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {

    ItemMapper ITEM_MAPPER = Mappers.getMapper(ItemMapper.class);

    ItemDto toDto(Item item);
}
