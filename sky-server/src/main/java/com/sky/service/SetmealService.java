package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 增加套餐
     * @param setmealDTO
     */
    void setmealadd(SetmealDTO setmealDTO);

    /**
     * 套餐分页
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult setmealQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐
     * @param ids
     */
    void deletesetmeal(List<Long> ids);

    /**
     * 根据id查询套餐
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
     * 更新套餐信息
     * @param setmealDTO
     */
    void updatesetmeal(SetmealDTO setmealDTO);


    /**
     * 根据分类id查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询套餐中菜品
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
