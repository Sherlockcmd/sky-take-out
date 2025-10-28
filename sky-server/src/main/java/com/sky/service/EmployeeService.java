package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 添加员工
     *
     * @param employeeDTO
     * @return
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 更新员工状态
     *
     * @param status
     * @param id
     * @return
     */
    void startOrstop(Integer status, Long id);

    /**
     * 查询员工
     *
     * @param id
     * @return
     */
    Employee selectById(Long id);

    /**
     * 修改员工信息
     *
     * @param employeeDTO
     * @return
     */
    void updateEmployee(EmployeeDTO employeeDTO);

    /**
     * 员工分页
     *
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
}
