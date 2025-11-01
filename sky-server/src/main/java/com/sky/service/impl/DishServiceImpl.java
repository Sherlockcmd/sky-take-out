package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorsMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorsMapper dishFlavorsMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    @Transactional
    public void dishadd(DishDTO dishDTO) {

        //添加菜品
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.dishadd(dish);

        //添加菜品口味
        Long dishId = dish.getId();
        dishDTO.getFlavors().forEach(f -> f.setDishId(dishId));
        dishFlavorsMapper.disnflavorsadd(dishDTO.getFlavors());
    }

    @Override
    public PageResult dishQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> list = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult<DishVO>(list.getTotal(), list.getResult());
    }

    @Override
    @Transactional
    public void deletedish(List<Long> ids) {

        //判断当前菜品是否能够删除--是否在启售中
        for(Long x:ids){
            if(dishMapper.selectById(x).getStatus()==StatusConstant.ENABLE){
                //当前菜品处于启售中，不能删除
                    throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //判断当前菜品是否能够删除--是否被套餐关联了
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds!=null && setmealIds.size()>0){
            //当前菜品被套餐关联了，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品
        //删除菜品口味
        dishMapper.deletedish(ids);
        dishFlavorsMapper.deleteflavor(ids);



    }

    @Override
    public DishVO selectById(Long id) {
         Dish dish = dishMapper.selectById(id);
         List<DishFlavor> fs = dishFlavorsMapper.selectByDishId(id);

         DishVO vo = new DishVO();
         BeanUtils.copyProperties(dish,vo);
         vo.setFlavors(fs);
         return vo;
    }

    @Override
    public void startOrstop(Integer status, Long id) {
        Dish dish = Dish.builder().id(id).status(status).build();
        dishMapper.update(dish);
    }

    @Override
    public void updatedish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);

        if(dishDTO.getFlavors()!=null && dishDTO.getFlavors().size()!=0) {
            List<Long> list = new ArrayList<>();
            list.add(dish.getId());
           dishFlavorsMapper.deleteflavor(list);

           for(DishFlavor x:dishDTO.getFlavors()){
               x.setDishId(dish.getId());
           }
           dishFlavorsMapper.disnflavorsadd(dishDTO.getFlavors());
        }

    }

}