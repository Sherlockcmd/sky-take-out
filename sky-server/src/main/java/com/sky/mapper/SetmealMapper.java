package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 添加菜品
     *
     * @param setmeal
     */
    @AutoFill(value = OperationType.INSERT)
    void setmealadd(Setmeal setmeal);

    /**
     * 菜品分页
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 菜品批量删除
     * @param ids
     */
    void deletesetmeal(List<Long> ids);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    Setmeal selectById(Long id);


    /**
     * 更新菜品信息
     * @param setmeal
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);

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
    List<DishItemVO> getDishItemBySetmealId(Long id);
}
