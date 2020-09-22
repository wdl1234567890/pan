package com.example.demo.service.impl;

import com.example.demo.domain.Department;
import com.example.demo.domain.DepartmentExample;
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
    public List<Department> listAllDepartment() throws Exception {
        try {
            List<Department> list = departmentMapper.selectByExample(new DepartmentExample());
            return list;
        }catch (Exception e){
            throw new Exception("查询失败");
        }
    }

    /**
     * 添加一个部门
     * @param department Deaprtment类型的数据
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean addDepartment(Department department) throws Exception {
        try {
            int insert = departmentMapper.insert(department);
            if(insert==1){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            throw new Exception("插入失败");
        }
    }

    /**
     * 修改部门信息
     * @param department Department类型
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean changeDepartment(Department department) throws Exception {
        try{
            int update = departmentMapper.updateByExample(department, new DepartmentExample());
            if(update==1){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            throw new Exception("修改失败");
        }
    }

    /**
     * 删除一个部门
     * @param id Integer的部门id
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean delDepartment(Integer id) throws Exception {
        try{
            int delete = departmentMapper.deleteByPrimaryKey(id);
            if(delete==1){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            throw new Exception("删除失败");
        }
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

    /**
     * 根据id查部门信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Department findDepartmentById(Integer id) throws Exception {
        try{
            Department department = departmentMapper.selectByPrimaryKey(id);
            return department;
        }catch (Exception e){
            throw new Exception("查询失败");
        }
    }
}
