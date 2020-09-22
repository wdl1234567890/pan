/**  

* @Title: 

* @Description: (这里用一句话描述这个方法的作用)  

* @param    参数  

* @return    返回类型  

* @throws  

*/  
package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import com.example.demo.vo.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 白开水
 *
 */
@RestController
@RequestMapping("/api/v1/pri/admin/")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "user",method = RequestMethod.POST)
    public JsonData addUser(User user) {
        try {
            Boolean aBoolean = userService.addUser(user);
            if(aBoolean){
                return JsonData.buildSuccess(aBoolean);
            }else{
                return JsonData.buildError();
            }
        }catch (Exception e){
            return JsonData.buildError(20000,e.getMessage());
        }
    }
    @RequestMapping(value = "importUsers",method = RequestMethod.POST)
    public JsonData importUsers(MultipartFile multipartFile){
        try{
            if(userService.importUsers(multipartFile.getInputStream(),multipartFile.getOriginalFilename())){
                return JsonData.buildSuccess("导入成功");
            }else{
                return JsonData.buildError(20000,"导入失败");
            }
        }catch (Exception e){
            return JsonData.buildError(20000,e.getMessage());
        }
    }

    @RequestMapping(value = "user/{id}",method = RequestMethod.DELETE)
    public JsonData deleteUser(@PathVariable("id") Integer id){
        try{
            if(userService.delUser(id)){
                return JsonData.buildSuccess("删除成功");
            }else{
                return JsonData.buildError(20000,"删除失败");
            }
        }catch (Exception e){
            return JsonData.buildError(20000,e.getMessage());
        }
    }

    @RequestMapping(value = "users",method = RequestMethod.DELETE)
    public JsonData deleteUsers(List<Integer> idls){
        try{
            if(userService.delUserList(idls)){
                return JsonData.buildSuccess("删除成功");
            }else{
                return JsonData.buildError(20000,"删除失败");
            }
        }catch (Exception e){
            return JsonData.buildError(20000,e.getMessage());
        }
    }

    @RequestMapping(value = "user",method = RequestMethod.PUT)
    public JsonData updateUser(User user){
        try{
            if(userService.changeUser(user)){
                return JsonData.buildSuccess("修改成功");
            }else{
                return JsonData.buildError(20000,"修改失败");
            }
        }catch (Exception e){
            return JsonData.buildError(20000,e.getMessage());
        }
    }

    @RequestMapping(value = "users",method = RequestMethod.GET)
    public JsonData findUsers(){
        try{
            List<User> list;
            if((list=userService.listAllUser())!=null){
                return JsonData.buildSuccess(list);
            }else{
                return JsonData.buildError(20000,"查询失败");
            }
        }catch (Exception e){
            return JsonData.buildError(20000,e.getMessage());
        }
    }

}

