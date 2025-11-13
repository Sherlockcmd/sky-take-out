package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {

       @Autowired
       private SetmealService setmealService;

       /**
        * 添加套餐
        *
        * @param setmealDTO
        * @return
        */
       @PostMapping
       @ApiOperation("添加套餐")
       @CacheEvict(cacheNames = "setmealCache",key = "#setmealDTO.categoryId")//key：setmealCache::(categoryId)
       public Result setmealadd(@RequestBody SetmealDTO setmealDTO){

              log.info("添加套餐：{}",setmealDTO);
              setmealService.setmealadd(setmealDTO);

              return Result.success("添加成功");
       }

       /**
        * 套餐分页
        *
        * @param setmealPageQueryDTO
        * @return
        */
       @GetMapping("/page")
       @ApiOperation("套餐分页查询")
       public Result page(SetmealPageQueryDTO setmealPageQueryDTO){
              log.info("分页查询：{}",setmealPageQueryDTO);
              PageResult pageresult = setmealService.setmealQuery(setmealPageQueryDTO);
              return Result.success(pageresult);
       }

       /**
        * 批量删除套餐
        * @param ids
        * @return
        */
       @DeleteMapping
       @ApiOperation("批量删除套餐")
       @CacheEvict(cacheNames = "setmealCache", allEntries = true)//缓存全部清除
       public Result delete(@RequestParam List<Long> ids){
              log.info("批量删除套餐：{}", ids);
              setmealService.deletesetmeal(ids);

            return Result.success("成功删除");
       }


       /**
        *根据id查询套餐
        * @param id
        * @return
        */
       @GetMapping("/{id}")
       @ApiOperation("根据id查询套餐")
       public Result selectById(@PathVariable Long id){
              log.info("根据id查询套餐：{}",id );
             SetmealVO setmealVO = setmealService.selectById(id);
              return Result.success(setmealVO);
       }

       /**
        * 启售或停售
        * @param status
        * @param id
        * @return
        */
       @PostMapping("status/{status}")
       @ApiOperation("启售或停售")
       @CacheEvict(cacheNames = "setmealCache", allEntries = true)//缓存全部清除
       public Result updatestatus(@PathVariable Integer status,Long id){
              log.info("启售或停售：{}{}",status,id);
              setmealService.startOrstop(status,id);
              return Result.success("修改成功");
       }

       /**
        * 修改套餐信息
        * @param setmealDTO
        * @return
        */
       @PutMapping
       @ApiOperation("修改套餐信息")
       @CacheEvict(cacheNames = "setmealCache", allEntries = true)//缓存全部清除
       public Result updatesetmeal(@RequestBody SetmealDTO setmealDTO){
              log.info("修改套餐信息：{}",setmealDTO);
              setmealService.updatesetmeal(setmealDTO);
              return Result.success("修改成功");
       }





}
