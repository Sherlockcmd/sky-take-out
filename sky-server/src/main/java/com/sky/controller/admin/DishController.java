package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {

       @Autowired
       private DishService dishService;

       /**
        * 添加菜品
        *
        * @param dishDTO
        * @return
        */
       @PostMapping
       @ApiOperation("添加菜品")
       public Result dishadd(@RequestBody DishDTO dishDTO){

              log.info("添加菜品：{}",dishDTO);
              dishService.dishadd(dishDTO);

              return Result.success("添加成功");
       }

       /**
        * 菜品分页
        *
        * @param dishPageQueryDTO
        * @return
        */
       @GetMapping("/page")
       @ApiOperation("菜品分页查询")
       public Result page(DishPageQueryDTO dishPageQueryDTO){
              log.info("分页查询：{}",dishPageQueryDTO);
              PageResult pageresult = dishService.dishQuery(dishPageQueryDTO);
              return Result.success(pageresult);
       }

       /**
        * 批量删除菜品
        * @param ids
        * @return
        */
       @DeleteMapping
       @ApiOperation("批量删除菜品")
       public Result delete(@RequestParam List<Long> ids){
              log.info("批量删除菜品：{}", ids);
              dishService.deletedish(ids);

            return Result.success("成功删除");
       }


       /**
        *根据id查询菜品
        * @param id
        * @return
        */
       @GetMapping("/{id}")
       @ApiOperation("根据id查询菜品")
       public Result selectById(@PathVariable Long id){
              log.info("根据id查询菜品：{}",id );
             DishVO dishVO = dishService.selectById(id);
              return Result.success(dishVO);
       }

       /**
        * 启售或停售
        * @param status
        * @param id
        * @return
        */
       @PostMapping("status/{status}")
       @ApiOperation("启售或停售")
       public Result updatestatus(@PathVariable Integer status,Long id){
              log.info("启售或停售：{}{}",status,id);
              dishService.startOrstop(status,id);
              return Result.success("修改成功");
       }

       /**
        * 修改菜品信息
        * @param dishDTO
        * @return
        */
       @PutMapping
       @ApiOperation("修改菜品信息")
       public Result updatedish(@RequestBody DishDTO dishDTO){
              log.info("修改菜品信息：{}",dishDTO);
              dishService.updatedish(dishDTO);
              return Result.success("修改成功");
       }

       /**
        *根据分类id查询菜品
        * @param categoryId
        * @return
        */
       @GetMapping("/list")
       @ApiOperation("根据分类id查询菜品")
       public Result selectBycategoryId(Long categoryId){
              log.info("根据分类id查询菜品：{}",categoryId );
             List<DishVO> dishVO = dishService.selectBycategoryId(categoryId);
              return Result.success(dishVO);
       }





}
