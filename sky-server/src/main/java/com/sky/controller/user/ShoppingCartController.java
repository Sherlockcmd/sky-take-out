package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "C端购物车相关接口")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result shoppingcartadd(@RequestBody ShoppingCartDTO shoppingCartDTO){
           log.info("添加购物车，商品信息为：{}",shoppingCartDTO);

           shoppingCartService.add(shoppingCartDTO);

           return Result.success("添加成功");
    }

    /**
     * 查询购物车全部商品
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询购物车全部商品")
    public Result<List<ShoppingCart>> list(){
        log.info("查询购物车全部商品");
        List<ShoppingCart>  shoppingCarts = shoppingCartService.showShoppingCart();

        return Result.success(shoppingCarts);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result clean(){
        log.info("清空购物车");
        shoppingCartService.clean();

        return Result.success("已清空购物车");
    }

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/sub")
    @ApiOperation("删除购物车中一个商品")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO){
         log.info("删除购物车中一个商品,商品信息为：{}",shoppingCartDTO);
         shoppingCartService.sub(shoppingCartDTO);
         return Result.success("删除成功");
    }




}
