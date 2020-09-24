package com.example.demo.service;

import com.example.demo.domain.Department;

import java.util.List;

/**
 * @author GooRay
 * 创建于 2020/9/17
 */
public interface DepartmentService {

    List<Department> listAllDepartment();

    Boolean addDepartment(Department department);

    Boolean changeDepartment(Department department);

    Boolean delDepartment(Integer id);

    Integer countUserByDepartment(Integer id);

    Department findDepartmentById(Integer id);

}
