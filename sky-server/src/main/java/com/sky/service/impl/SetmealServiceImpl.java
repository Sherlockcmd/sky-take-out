package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    @Override
    @Transactional
    public void setmealadd(SetmealDTO setmealDTO) {

        //添加套餐
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        setmealMapper.setmealadd(setmeal);

        //在套餐中关联菜品
        Long setmealId = setmeal.getId();
        setmealDTO.getSetmealDishes().forEach(f -> f.setSetmealId(setmealId));
        setmealDishMapper.setmealdishadd(setmealDTO.getSetmealDishes());
    }

    @Override
    public PageResult setmealQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> list = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult<SetmealVO>(list.getTotal(), list.getResult());
    }

    @Override
    @Transactional
    public void deletesetmeal(List<Long> ids) {

        //判断当前套餐是否能够删除--是否在启售中
        for(Long x:ids){
            if(setmealMapper.selectById(x).getStatus()==StatusConstant.ENABLE){
                //当前菜品处于启售中，不能删除
                    throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //删除菜品
        setmealMapper.deletesetmeal(ids);
        //删除套餐和菜品的关联
        setmealDishMapper.deletesetmealdish(ids);
    }

    @Override
    public SetmealVO selectById(Long id) {
         Setmeal setmeal = setmealMapper.selectById(id);
         List<SetmealDish> sd = setmealDishMapper.selectBySetmealId(id);

         SetmealVO vo = new SetmealVO();
         BeanUtils.copyProperties(setmeal,vo);
         vo.setSetmealDishes(sd);
         return vo;
    }

    @Override
    public void startOrstop(Integer status, Long id) {
        if(status == StatusConstant.ENABLE){
            List<Dish> dishlist = dishMapper.selectBySetmealId(id);
            if(dishlist!=null && dishlist.size()!=0){
                dishlist.forEach(dish->{
                    if(dish.getStatus()==StatusConstant.DISABLE){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        Setmeal setmeal = Setmeal.builder().id(id).status(status).build();
        setmealMapper.update(setmeal);
    }

    @Override
    public void updatesetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);

        if(setmealDTO.getSetmealDishes()!=null && setmealDTO.getSetmealDishes().size()!=0) {
            List<Long> list = new ArrayList<>();
            list.add(setmeal.getId());
           setmealDishMapper.deletesetmealdish(list);

           for(SetmealDish x:setmealDTO.getSetmealDishes()){
               x.setSetmealId(setmeal.getId());
           }
           setmealDishMapper.setmealdishadd(setmealDTO.getSetmealDishes());
        }

    }

    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        List<DishItemVO> dishItemVOS = setmealMapper.getDishItemBySetmealId(id);
        return dishItemVOS;
    }


}