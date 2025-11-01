package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorsMapper {

    /**
     * 增加菜品口味
     * @param dishFlavor
     */
    void disnflavorsadd(List dishFlavor);

    /**
     * 删除菜品口味
     * @param ids
     */
    void deleteflavor(List<Long> ids);

    /**
     * 根据id查询菜品口味
     * @param id
     * @return
     */
    List<DishFlavor> selectByDishId(Long id);


}
