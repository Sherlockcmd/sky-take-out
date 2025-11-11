package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "C端分类相关接口")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * C端分类查询
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("C端分类查询")
    public Result list(Integer type){
        log.info("C端分类查询：{}", type!=null ? type :"全部查询");
        List<Category> categories = categoryService.selectByType(type);
        return Result.success(categories);
    }

}
