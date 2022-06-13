package com.cleancode.unitTest.service.mapper;

import com.cleancode.unitTest.entity.Item;
import com.cleancode.unitTest.module.ItemDto;
import com.cleancode.unitTest.module.ItemLabel;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public abstract class ItemMapper {

    public static final ItemMapper ITEM_MAPPER = Mappers.getMapper(ItemMapper.class);

    @BeforeMapping
    protected void enrichItemLabels(Item item, @MappingTarget ItemDto.ItemDtoBuilder itemDto) {
        final String labels = item.getLabels();
        if (labels == null) {
            return;
        }

        final String[] stringLabels = labels.split(",");
        final List<ItemLabel> itemLabels = Arrays
                .stream(stringLabels)
                .map(ItemLabel::fromString)
                .collect(Collectors.toList());
        itemDto.labels(itemLabels);
    }

    @Mapping(target = "labels", ignore = true)
    public abstract ItemDto itemToDto(Item item);

    @Mapping(target = "labels", ignore = true)
    public abstract Item dtoToItem(ItemDto itemDto);
}
