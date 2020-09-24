package com.example.demo.controller;

import com.example.demo.domain.Department;
import com.example.demo.service.DepartmentService;
import com.example.demo.vo.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author GooRay
 * 创建于 2020/9/22
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/pri/admin/")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(value = "department",method = RequestMethod.POST)
    public JsonData addDepartment(@RequestBody Department department){

        departmentService.addDepartment(department);
        return JsonData.buildSuccess(departmentService.findDepartmentById(department.getId()));

    }

    @RequestMapping(value = "department/{id}",method = RequestMethod.DELETE)
    public JsonData deleteDepartment(@PathVariable("id")Integer id){

        if(departmentService.delDepartment(id)){
            return JsonData.buildSuccess("删除成功");
        }else{
            return JsonData.buildError(20000,"删除失败");
        }

    }

    @RequestMapping(value = "department",method = RequestMethod.PUT)
    public JsonData updateDepartment(@RequestBody Department department){
        if(departmentService.changeDepartment(department)){
            return JsonData.buildSuccess(department);
        }else{
            return JsonData.buildError();
        }
    }

    @RequestMapping(value = "departments",method = RequestMethod.GET)
    public JsonData findDepartments(){
        List<Department> list;
        if((list=departmentService.listAllDepartment())!=null){
            return JsonData.buildSuccess(list);
        }else{
            return JsonData.buildError();
        }
    }
}
