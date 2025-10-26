package com.sky.controller.admin;

import com.github.pagehelper.PageInfo;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }


    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("分页查询，{}",employeePageQueryDTO);
        PageResult pageresult = employeeService.list(employeePageQueryDTO);
        return Result.success(pageresult);
    }


    /**
     * 添加员工
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping()
    @ApiOperation("添加员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("添加员工：{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success("添加成功");
    }


    /**
     * 更新员工的状态(status)
     *
     * @param status,id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("更新员工的状态(status)")
    public Result updateStatus(@PathVariable Integer status ,Long id){
        log.info("更新员工的状态：{}{}", status,id);
        employeeService.startOrstop(status,id);

        return Result.success("修改成功");
    }

    /**
     * 更新员工的状态(status)
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("查询员工信息")
    public Result selectById(@PathVariable Long id){
        log.info("查询员工信息：{}",id);
       Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

    /**
     * 更新员工的状态(status)
     *
     * @param employeeDTO
     * @return
     */

    @PutMapping()
    @ApiOperation("更新员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("更新员工信息：{}",employeeDTO);
        employeeService.updateEmployee(employeeDTO);
        return Result.success("更新成功");
    }




}
