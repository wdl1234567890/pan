package com.example.demo.service.impl;

import com.example.demo.domain.Department;
import com.example.demo.service.DepartmentService;

import java.util.List;

/**
 * @author GooRay
 * 创建于 2020/9/17
 */
public class DepartmentServiceImpl implements DepartmentService {

    /**
     * 查询所有部门信息
     * @return  List类型的部门信息
     * @throws Exception
     */
    @Override
    public List<Department> listAllDepartment() throws Exception {
        return null;
    }

    /**
     * 添加一个部门
     * @param department Deaprtment类型的数据
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean addDepartment(Department department) throws Exception {
        return null;
    }

    /**
     * 修改部门信息
     * @param department Department类型
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean changeDepartment(Department department) throws Exception {
        return null;
    }

    /**
     * 删除一个部门
     * @param id Integer的部门id
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean delDepartment(Integer id) throws Exception {
        return null;
    }

    /**
     * 查询指定部门中的员工人数
     * @param id Integer类型的id
     * @return  Integer类型的员工数量
     * @throws Exception
     */
    @Override
    public Integer countUserByDepartment(Integer id) throws Exception {
        return null;
    }
}
