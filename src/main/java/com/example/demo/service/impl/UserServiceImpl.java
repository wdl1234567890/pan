package com.example.demo.service.impl;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;

import java.io.File;
import java.util.List;

/**
 * @author GooRay
 * 创建于 2020/9/17
 */
public class UserServiceImpl implements UserService {


    /**
     * 查询所有用户
     * @return List<User> 用户列表
     * @throws Exception
     */
    @Override
    public List<User> listAllUser() throws Exception {
        return null;
    }

    /**
     * 添加单个用户
     * @param user user类型的用户信息
     * @return Boolean 是否插入成功
     * @throws Exception
     */
    @Override
    public Boolean addUser(User user) throws Exception {
        return null;
    }

    /**
     * 批量导入用户
     * @param userList file类型的userList
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean importUsers(File userList) throws Exception {
        return null;
    }

    /**
     * 删除单个用户
     * @param id 用户id
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean delUser(int id) throws Exception {
        return null;
    }

    /**
     * 批量删除用户
     * @param idls 用户id列表 List
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean delUserList(List idls) throws Exception {
        return null;
    }

    /**
     * 修改当个用户信息
     * @param newUser 新的User类型的用户信息
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean changeUser(User newUser) throws Exception {
        return null;
    }

    /**
     * 是否启用用户
     * @param id 列表类型的id List
     * @param open 是否启用
     * @return  Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean disabledUser(List id, Boolean open) throws Exception {
        return null;
    }

    /**
     * 修改密码
     * @param user User类型的用户数据
     * @param newPasswd String类型的新密码
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean changePasswd(User user, String newPasswd) throws Exception {
        return null;
    }

    /**
     * 登录
     * @param name String类型的用户名
     * @param pass String类型的密码
     * @return User类型的登录成功的用户信息
     * @throws Exception
     */
    @Override
    public User loginUser(String name, String pass) throws Exception {
        return null;
    }

    /**
     * 获取单个用户
     * @param id int类型的用户id
     * @return User类型的用户信息
     * @throws Exception
     */
    @Override
    public User getAccoutById(int id) throws Exception {
        return null;
    }

    /**
     * 是否通过模糊查询使用用户名查询
     * @param name 用户名
     * @param islike 是否采用模糊查询
     * @return List类型的User数据
     * @throws Exception
     */
    @Override
    public List<User> findUserByName(String name, Boolean islike) throws Exception {
        return null;
    }

    /**
     * 通过部门查询用户信息
     * @param department String类型的部门名
     * @return List类型的User数据
     * @throws Exception
     */
    @Override
    public List<User> findUserByDepartment(String department) throws Exception {
        return null;
    }
}
