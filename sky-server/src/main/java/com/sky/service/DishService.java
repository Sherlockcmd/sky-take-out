package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface DishService {

    void dishadd(DishDTO dishDTO);

    PageResult dishQuery(DishPageQueryDTO dishPageQueryDTO);

    void deletedish(List<Long> ids);

}
