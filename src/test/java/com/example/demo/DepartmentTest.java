package com.example.demo;

import com.example.demo.domain.Department;
import com.example.demo.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class DepartmentTest {

    @Autowired
    DepartmentService departmentService;

    @Test
    public void testAddDepartment(){
        Department department = new Department();
        department.setName("测试");
        department.setLead("1");
        Boolean aBoolean = departmentService.addDepartment(department);
        System.out.println(aBoolean);
    }

    @Test
    public void testDeleteDepartment(){
        Boolean aBoolean = departmentService.delDepartment(17);
        System.out.println(aBoolean);
    }

    @Test
    public void testUpdateDepartment(){
        Department departmentById = departmentService.findDepartmentById(17);
        departmentById.setName("后勤部");
        Boolean aBoolean = departmentService.changeDepartment(departmentById);
        System.out.println(aBoolean);
    }

    @Test
    public void testFindDepartments(){
        List<Department> departments = departmentService.listAllDepartment();
        for (Department department : departments) {
            System.out.println(department);
        }
    }

    @Test void testGetDepartmentById(){
        Department departmentById = departmentService.findDepartmentById(17);
        System.out.println(departmentById);
    }
}
