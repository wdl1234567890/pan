package com.example.demo.service.impl;

import com.example.demo.domain.Department;
import com.example.demo.domain.DepartmentExample;
import com.example.demo.enums.StatusCode;
import com.example.demo.exception.PanException;
import com.example.demo.mapper.DepartmentMapper;
import com.example.demo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GooRay
 * 创建于 2020/9/17
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 查询所有部门信息
     * @return  List类型的部门信息
     * @throws Exception
     */
    @Override
    public List<Department> listAllDepartment() {
        List<Department> list;
        try {
             list = departmentMapper.selectByExample(new DepartmentExample());
        }catch (Exception e){
            throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        }
        return list;
    }

    /**
     * 添加一个部门
     * @param department Deaprtment类型的数据
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean addDepartment(Department department){
        try {
            int insert = departmentMapper.insert(department);
            if(1!=insert) throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        }catch (Exception e){
            throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        }
        return true;
    }

    /**
     * 修改部门信息
     * @param department Department类型
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean changeDepartment(Department department) {
        try{
            int update = departmentMapper.updateByExample(department, new DepartmentExample());
            if(1!=update) throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        }catch (Exception e){
            throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        }
        return true;
    }

    /**
     * 删除一个部门
     * @param id Integer的部门id
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean delDepartment(Integer id) {
        try{
            int delete = departmentMapper.deleteByPrimaryKey(id);
            if(1!=delete) throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        }catch (Exception e){
            throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        }
        return true;
    }

    /**
     * 查询指定部门中的员工人数
     * @param id Integer类型的id
     * @return  Integer类型的员工数量
     * @throws Exception
     */
    @Override
    public Integer countUserByDepartment(Integer id) {
        return null;
    }

    /**
     * 根据id查部门信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Department findDepartmentById(Integer id){
        try{
            Department department = departmentMapper.selectByPrimaryKey(id);
            return department;
        }catch (Exception e){
            throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        }
    }
}
