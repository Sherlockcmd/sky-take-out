package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 增加套餐
     * @param setmealDTO
     */
    void setmealadd(SetmealDTO setmealDTO);

    /**
     * 菜品分页
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult setmealQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deletesetmeal(List<Long> ids);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    SetmealVO selectById(Long id);

    /**
     * 启售或停售
     * @param status
     * @param id
     */
    void startOrstop(Integer status,Long id);

    /**
     * 更新菜品信息
     * @param setmealDTO
     */
    void updatesetmeal(SetmealDTO setmealDTO);
}
