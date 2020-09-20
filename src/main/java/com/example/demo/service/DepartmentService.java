package com.example.demo.service;

import com.example.demo.domain.Department;

import java.util.List;

/**
 * @author GooRay
 * 创建于 2020/9/17
 */
public interface DepartmentService {

    List<Department> listAllDepartment() throws Exception;

    Boolean addDepartment(Department department) throws Exception;

    Boolean changeDepartment(Department department) throws Exception;

    Boolean delDepartment(Integer id) throws Exception;

    Integer countUserByDepartment(Integer id) throws Exception;

}
