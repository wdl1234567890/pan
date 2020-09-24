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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 白开水
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/pri/admin/")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "user",method = RequestMethod.POST)
    public JsonData addUser(@RequestBody User user) {
        userService.addUser(user);
        return JsonData.buildSuccess(userService.getUserById(user.getId()));
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
        if(userService.delUser(id)) {
            return JsonData.buildSuccess("删除成功");
        }
        return JsonData.buildError();
    }

    @RequestMapping(value = "users",method = RequestMethod.DELETE)
    public JsonData deleteUsers(@RequestBody List<Integer> idls){
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
    public JsonData updateUser(@RequestBody User user){
            userService.changeUser(user);
            return JsonData.buildSuccess("修改成功");
    }

    @RequestMapping(value = "users",method = RequestMethod.GET)
    public JsonData findUsers(){
        List<User> users = userService.listAllUser();
        return JsonData.buildSuccess(users);
    }

}

