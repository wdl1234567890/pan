package com.example.demo;

import com.example.demo.domain.PageResult;
import com.example.demo.domain.User;
import com.example.demo.domain.UserExample;
import com.example.demo.service.UserService;
import com.example.demo.utils.PageRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setName("lfk");
        user.setPwd("123");
        user.setMail("461593941@qq.com");
        user.setPhone("15917745060");
        user.setLevel(0);
        Boolean aBoolean = userService.addUser(user);
        System.out.println(aBoolean);
    }

    @Test
    public void testImportUsers() {
        try {
            Boolean aBoolean = userService.importUsers(new FileInputStream("E:\\WorkSpace\\clone\\pan\\src\\main\\resources\\default.xlsx"), "default.xlsx", new ArrayList<>());
            System.out.println(aBoolean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteUser(){
        Boolean aBoolean = userService.delUser(201750);
        System.out.println(aBoolean);
    }

    @Test
    public void testDeleteUsers(){
        ArrayList<Integer> integers = new ArrayList<>();
        Boolean aBoolean = userService.delUserList(integers);
        System.out.println(aBoolean);
    }

    @Test
    public void testUpdateUser(){
        User user = userService.getUserById(201750);
        user.setLevel(1);
        Boolean aBoolean = userService.changeUser(user);
        System.out.println(aBoolean);
    }

    @Test
    public void testGetUser(){
        User user = userService.getUserById(201750);
        System.out.println(user);
    }

    @Test
    public void testGetUsers(){
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageSize(10);
        pageRequest.setPageNum(1);
        User user = new User();
        PageResult page = userService.findPage(pageRequest, user);
        List<?> content = page.getContent();
        for (Object o : content) {
            System.out.println(o.toString());
        }
    }

    @Test
    public void testGetUsersByLevel(){
        List<User> userByLevel = userService.findUserByLevel(1);
        for (User user : userByLevel) {
            System.out.println(user);
        }
    }

    @Test
    public void testUpdateUserLevel(){
        Boolean aBoolean = userService.changeLevel(201750);
        System.out.println(aBoolean);
    }

}
