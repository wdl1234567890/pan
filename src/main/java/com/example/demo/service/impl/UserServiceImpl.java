package com.example.demo.service.impl;

import com.example.demo.domain.User;
import com.example.demo.domain.UserExample;
import com.example.demo.enums.StatusCode;
import com.example.demo.exception.PanException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    boolean isRepeat(User user){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria1 = userExample.createCriteria();
        criteria1.andMailEqualTo(user.getMail());
        UserExample.Criteria criteria2 = userExample.createCriteria();
        criteria2.andPhoneEqualTo(user.getPhone());
        userExample.or(criteria2);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size()!=0){
            return true;
        }else{
            return false;
        }
    }


    /**
     * 查询所有用户
     * @return List<User> 用户列表
     * @throws Exception
     */
    @Override
    public List<User> listAllUser(){
        List<User> list;
        try{
            list = userMapper.selectByExample(new UserExample());
        }catch (Exception e){
            throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
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
    public Boolean addUser(User user){
        if (isRepeat(user)) throw new PanException(StatusCode.MAIL_OR_PHONE_IS_EXISTED.code(),StatusCode.MAIL_OR_PHONE_IS_EXISTED.message());
        int insert = userMapper.insert(user);
        if(1!=insert) throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        return true;
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
                int insert = userMapper.insert(user);
                if(1!=insert) throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
            }
        }catch (Exception e){
            throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
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
    public Boolean delUser(int id){
        try{
            int delete = userMapper.deleteByPrimaryKey(id);
            if(1!=delete) throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        }catch (Exception e){
            throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        }
        return true;
    }

    /**
     * 批量删除用户
     * @param idls 用户id列表 List
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Transactional
    @Override
    public Boolean delUserList(List<Integer> idls) throws Exception {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andIdIn(idls);
        try{
            int delete = userMapper.deleteByExample(userExample);
            if(delete!=idls.size())throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        }catch (Exception e){
            throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        }
        return true;
    }

    /**
     * 修改当个用户信息
     * @param newUser 新的User类型的用户信息
     * @return Boolean 是否成功
     * @throws Exception
     */
    @Override
    public Boolean changeUser(User newUser){
        try{
            int update = userMapper.updateByPrimaryKey(newUser);
            if(1!=update)throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        }catch (Exception e){
            throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        }
        return true;
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
    public User getUserById(int id){
        User user;
        try {
            user = userMapper.selectByPrimaryKey(id);
            if(user==null) throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        }catch (Exception e){
            throw new PanException(StatusCode.DATABASE_ERROR.code(),StatusCode.DATABASE_ERROR.message());
        }
        return user;
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
