package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Api(tags = "C端菜品类接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    //不用注解缓存要注入bean类
//    @Autowired
//    private RedisTemplate redisTemplate;

    /**
     * 条件查询菜品和口味
     * @param categoryId
     * @return
     */
    @GetMapping("list")
    @ApiOperation("C端菜品查询")
    @Cacheable(cacheNames = "dishCache",key = "#categoryId")//key：dishCache::(categoryId)
    public Result list(Long categoryId){

        log.info("C端菜品查询：{}",categoryId);

        //不用注解的写法
//        //构造redis中的key，规则：dish_分类id
//        String key = "dish_" + categoryId;
//
//        //查询redis中是否存在菜品数据
//        List<DishVO> dishlist = (List<DishVO>) redisTemplate.opsForValue().get(key);
//
//        //如果存在，直接返回，无须查询数据库
//        if(dishlist!=null && dishlist.size()>0){
//            return Result.success(dishlist);
//        }

        //如果不存在，查询数据库，将查询到的数据放入redis中

        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);

        List<DishVO> dishlist = dishService.listWithFlavor(dish);

//        redisTemplate.opsForValue().set(key,dishlist);

        return Result.success(dishlist);
    }


}
