package com.sky.mapper;


import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    List<Long> getSetmealIdsByDishIds(List<Long> ids);

    void setmealdishadd(List<SetmealDish> setmealDishes);

    void deletesetmealdish(List<Long> ids);

    List<SetmealDish> selectBySetmealId(Long id);
}
