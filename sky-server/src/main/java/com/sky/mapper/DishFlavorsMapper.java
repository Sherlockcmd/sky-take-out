package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorsMapper {

    void disnflavorsadd(List dishFlavor);

    void deleteflavor(List<Long> ids);
}
