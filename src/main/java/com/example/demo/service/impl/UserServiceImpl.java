package com.example.demo.service.impl;

import com.example.demo.domain.User;
import com.example.demo.domain.UserExample;
import com.example.demo.mapper.UserMapper;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author GooRay
 * 创建于 2020/9/17
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询所有用户
     * @return List<User> 用户列表
     * @throws Exception
     */
    @Override
    public List<User> listAllUser() throws Exception {
        List<User> list;
        try{
            list = userMapper.selectByExample(new UserExample());
        }catch (Exception e){
            throw new Exception("查询失败");
        }
        return list;
    }

    /**
     * 添加单个用户
     * @param user user类型的用户信息
     * @return Boolean 是否插入成功
     * @throws Exception
     */
    @Override
    public Boolean addUser(User user) throws Exception {
        int insert = userMapper.insert(user);
        if(insert==1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 批量导入用户
     * @param inputStream
     * @param filename
     * @return
     * @throws Exception
     */
    @Transactional
    @Override
    public Boolean importUsers(InputStream inputStream,String filename) throws Exception {
        List<Map<String, Object>> list = ExcelUtil.ReadExcel(inputStream,filename);
        List<User> users = new ArrayList<>();
        for (Map<String, Object> map : list) {
            User user = new User();
            user.setName((String) map.get("name"));
            user.setPwd((String) map.get("pwd"));
            user.setDepartment((Integer) map.get("department"));
            user.setMail((String) map.get("mail"));
            user.setPhone((String) map.get("phone"));
            user.setLevel((Integer) map.get("level"));
            users.add(user);
        }
        try{
            for (User user : users) {
                userMapper.insert(user);
            }
        }catch (Exception e){
            throw new Exception("插入失败！");
        }
        return true;
    }

    /**
     * 删除单个用户
     * @param id 用户id
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean delUser(int id) throws Exception {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andIdEqualTo(id);
        int delete = userMapper.deleteByExample(userExample);
        if(delete==1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 批量删除用户
     * @param idls 用户id列表 List
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Transactional
    @Override
    public Boolean delUserList(List idls) throws Exception {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andIdIn(idls);
        int delete = userMapper.deleteByExample(userExample);
        if(delete==idls.size()){
            return true;
        }else{
            throw new Exception("删除失败");
        }
    }

    /**
     * 修改当个用户信息
     * @param newUser 新的User类型的用户信息
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean changeUser(User newUser) throws Exception {
        UserExample userExample = new UserExample();
        userMapper.updateByExample(newUser,userExample);
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
    public User getUserById(int id) throws Exception {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andIdEqualTo(id);
        List<User> users = userMapper.selectByExample(userExample);
        return users.get(0);
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
