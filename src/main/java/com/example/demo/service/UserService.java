package com.example.demo.service;

import com.example.demo.domain.Department;
import com.example.demo.domain.PageResult;
import com.example.demo.domain.User;
import com.example.demo.utils.PageRequest;

import java.io.InputStream;
import java.util.List;

/**
 * @author GooRay
 * 创建于 2020/9/17
 */
public interface UserService {
    List<User> listAllUser();

    Boolean addUser(User user);

    Boolean importUsers(InputStream inputStream, String filename, List<Department> departments);

    Boolean delUser(int id);

    Boolean delUserList(List<Integer> idls);

    Boolean changeUser(User newUser);

    Boolean disabledUser(List id, Boolean open) throws Exception;

    Boolean changePasswd(User user, String newPasswd) throws Exception;

    User loginUser(String name, String pass) throws Exception;

    User getUserById(int id);

    List<User> findUserByName(String name, Boolean islike) throws Exception;

    List<User> findUserByDepartment(String department) throws Exception;

    PageResult findPage(PageRequest pageRequest,User user);

    List<User> findUserByLevel(Integer level);
}
