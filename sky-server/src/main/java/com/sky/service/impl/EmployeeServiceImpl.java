package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // 对前端传过来的密码进行MD5加密
         password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 添加员工
     *
     * @param employeeDTO
     * @return
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee emp = new Employee();
        //把employeeDTO里的对象值赋给新建的对象emp
        //注：DTO只是有少部分的员工信息，所以下面要补全员工信息
        BeanUtils.copyProperties(employeeDTO,emp);
        //补全员工状态
        emp.setStatus(StatusConstant.ENABLE);
        //补全密码(md5加密过后)
        emp.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        //新增用户的创建时间和更新时间是一样的，都是当前时间
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        //新增用户的创建id和更新id是一样的，都是当前id
        emp.setUpdateUser(BaseContext.getCurrentId());
        emp.setCreateUser(BaseContext.getCurrentId());

        employeeMapper.insert(emp);
    }

    /**
     * 更新员工状态
     *
     * @param status
     * @param id
     * @return
     */
    @Override
    public void startOrstop(Integer status, Long id) {
        Employee emp = Employee.builder().id(id).status(status).build();
        employeeMapper.update(emp);
    }

    /**
     * 查询员工
     *
     * @param id
     * @return
     */
    @Override
    public Employee selectById(Long id) {
        return employeeMapper.selectById(id);
    }

    /**
     * 修改员工信息
     *
     * @param employeeDTO
     * @return
     */
    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        Employee emp = new Employee();
        //把employeeDTO里的对象值赋给新建的对象emp
        BeanUtils.copyProperties(employeeDTO,emp);
        //获取当前时间作为更新时间
        emp.setUpdateTime(LocalDateTime.now());
        //获取当前id为emp中的updateuser赋值
        emp.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(emp);
    }

    /**
     * 员工分页
     *
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        Page<Employee> list = employeeMapper.pageQuery(employeePageQueryDTO);
        return new PageResult<Employee>(list.getTotal(),list.getResult());
    }


}
