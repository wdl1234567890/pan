package com.example.demo.controller;

import com.example.demo.domain.Department;
import com.example.demo.service.DepartmentService;
import com.example.demo.vo.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author GooRay
 * 创建于 2020/9/22
 */
@Controller
@RequestMapping("/api/v1/pri/admin/")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(value = "department",method = RequestMethod.POST)
    public JsonData addDepartment(Department department){
        try{
            if(departmentService.addDepartment(department)){
                return JsonData.buildSuccess(departmentService.findDepartmentById(department.getId()));
            }else{
                return JsonData.buildError(20000,"添加失败");
            }
        }catch (Exception e){
            return JsonData.buildError(20000,e.getMessage());
        }
    }

    @RequestMapping(value = "department/{id}",method = RequestMethod.DELETE)
    public JsonData deleteDepartment(@PathVariable("id")Integer id){
        try{
            if(departmentService.delDepartment(id)){
                return JsonData.buildSuccess("删除成功");
            }else{
                return JsonData.buildError(20000,"删除失败");
            }
        }catch (Exception e){
            return JsonData.buildError(20000,e.getMessage());
        }
    }

    @RequestMapping(value = "department",method = RequestMethod.PUT)
    public JsonData updateDepartment(Department department){
        try{
            if(departmentService.changeDepartment(department)){
                return JsonData.buildSuccess(department);
            }else{
                return JsonData.buildError(20000,"修改失败");
            }
        }catch (Exception e){
            return JsonData.buildError(20000,e.getMessage());
        }
    }

    @RequestMapping(value = "departments",method = RequestMethod.GET)
    public JsonData findDepartments(){
        try{
            List<Department> list;
            if((list=departmentService.listAllDepartment())!=null){
                return JsonData.buildSuccess(list);
            }else{
                return JsonData.buildError(20000,"查询失败");
            }
        }catch (Exception e){
            return JsonData.buildError(20000,e.getMessage());
        }
    }
}
