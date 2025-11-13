package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {


    /**
     * 查询购物车
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 更新购物车商品数量
     * @param cart
     */
    void update(ShoppingCart cart);

    /**
     * 添加购物车
     * @param shoppingCart
     */
    void add(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     * 删除一个商品
     * @param shoppingCart
     */
    void clean(ShoppingCart shoppingCart);
}
