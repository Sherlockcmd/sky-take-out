package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *分类管理
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类相关接口")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;



    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询，{}",categoryPageQueryDTO);
        PageResult pageresult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageresult);
    }




    /**
     * 添加分类
     *
     * @param categoryDTO
     * @return
     */
    @PostMapping()
    @ApiOperation("添加分类")
    public Result save(@RequestBody CategoryDTO categoryDTO){
        log.info("添加分类：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success("添加成功");
    }


    /**
     * 查询分类
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("按类型查询分类信息")
    public Result selectByType(Integer type){
        log.info("按类型查询分类信息：{}",type);
        List<Category> list = categoryService.selectByType(type);
        return Result.success(list);
    }


    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @DeleteMapping()
    @ApiOperation("删除分类")
    public Result selectById(Long id){
        log.info("删除分类：{}",id);
        categoryService.deleteById(id);
        return Result.success("删除成功");
    }


    /**
     * 更新分类
     *
     * @param categoryDTO
     * @return
     */
    @PutMapping()
    @ApiOperation("修改分类信息")
    public Result update(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类信息：{}",categoryDTO);
        categoryService.updateCategory(categoryDTO);
        return Result.success("修改成功");
    }

    /**
     * 更新分类的状态(status)
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("更新分类的状态(status)")
    public Result updateStatus(@PathVariable Integer status ,Long id){
        log.info("更新分类的状态：{}{}", status,id);
        categoryService.startOrstop(status,id);

        return Result.success("修改成功");
    }




}
