package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 增加菜品
     * @param dishDTO
     */
    void dishadd(DishDTO dishDTO);

    /**
     * 菜品分页
     * @param dishPageQueryDTO
     * @return
     */
    PageResult dishQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deletedish(List<Long> ids);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    DishVO selectById(Long id);

    /**
     * 启售或停售
     * @param status
     * @param id
     */
    void startOrstop(Integer status,Long id);

    /**
     * 更新菜品信息
     * @param dishDTO
     */
    void updatedish(DishDTO dishDTO);
}
