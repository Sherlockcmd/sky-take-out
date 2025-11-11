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

    /**
     * 条件查询菜品和口味
     * @param categoryId
     * @return
     */
    @GetMapping("list")
    @ApiOperation("C端菜品查询")
    public Result list(Long categoryId){
        log.info("C端菜品查询：{}",categoryId);
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);

        List<DishVO> dishVOS = dishService.listWithFlavor(dish);

        return Result.success(dishVOS);
    }


}
