package com.example.demo.service;

import com.example.demo.domain.User;

import java.io.File;
import java.util.List;

/**
 * @author GooRay
 * 创建于 2020/9/17
 */
public interface UserService {
    List<User> listAllUser() throws Exception;

    Boolean addUser(User user) throws Exception;

    Boolean importUsers(File userList) throws Exception;

    Boolean delUser(int id) throws Exception;

    Boolean delUserList(List idls) throws Exception;

    Boolean changeUser(User newUser) throws Exception;

    Boolean disabledUser(List id, Boolean open) throws Exception;

    Boolean changePasswd(User user, String newPasswd) throws Exception;

    User loginUser(String name, String pass) throws Exception;

    User getAccoutById(int id) throws Exception;

    List<User> findUserByName(String name, Boolean islike) throws Exception;

    List<User> findUserByDepartment(String department) throws Exception;
}
