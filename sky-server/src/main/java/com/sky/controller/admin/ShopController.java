package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺相关接口")
public class ShopController {

    public static final String KEY = "SHOP_STATUS";


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置店铺营业状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result setStatus(@PathVariable Integer status){
        log.info("设置店铺营业状态为：{}",status==1 ? "营业中" : "打烊中");
        //保存到redis数据库中
        redisTemplate.opsForValue().set(KEY,status);

        return Result.success();
    }

    /**
     * 查询店铺营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("查询店铺营业状态")
    public Result getStatus(){
        //从redis数据库中获取status
        String shopStatusstr =(String) redisTemplate.opsForValue().get(KEY);
        //解析成整数类型
        Integer shopStatus = Integer.parseInt(shopStatusstr);

        log.info("查询店铺营业状态：{}",shopStatus==1 ? "营业中" : "打烊中");
        return Result.success(shopStatus);
    }
}
