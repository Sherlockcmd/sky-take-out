package com.sky.service.impl;

import com.fasterxml.jackson.databind.JsonSerializable;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;


    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //判断当前加入到购物车中的商品是否已经存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //用户id前端没传过来，我们可以从ThreadLocal里取
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        //如果存在，只需要将其数量加一
        if(list!=null && list.size()>0){
            ShoppingCart Cart = list.get(0);
            Cart.setNumber(Cart.getNumber()+1);
            shoppingCartMapper.update(Cart);
        }
        else {
            //如果不存在，需要插入一条购物车数据
            //首先判断是菜品还是套餐
            if(shoppingCartDTO.getDishId()!=null){
                //本次添加到购物车的是菜品
                //获取菜品信息填入
                Dish dish = dishMapper.selectById(shoppingCartDTO.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }
            else{
                //本次添加到购物车的套餐
                Setmeal setmeal = setmealMapper.selectById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.add(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> showShoppingCart() {
        //获取用户id
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
       return shoppingCartMapper.list(shoppingCart);
    }

    @Override
    public void clean() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart= new ShoppingCart();
        shoppingCart.setUserId(userId);
        shoppingCartMapper.clean(shoppingCart);
    }

    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(userId);
        //判断该商品的数量是不是1
        //如果数量大于1的话，就只用将数量-1就行
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);
        ShoppingCart cart = shoppingCarts.get(0);
        if(cart.getNumber()>1){
            cart.setNumber(cart.getNumber()-1);
            shoppingCartMapper.update(cart);
        } else if(cart.getNumber()==1){
            //如果数量==1的话，就直接删除这条菜品信息
            shoppingCartMapper.clean(cart);
        }
    }


}
