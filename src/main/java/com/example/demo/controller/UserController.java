/**  

* @Title: 

* @Description: (这里用一句话描述这个方法的作用)  

* @param    参数  

* @return    返回类型  

* @throws  

*/  
package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.dto.IdlsDTO;
import com.example.demo.service.UserService;
import com.example.demo.vo.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    public JsonData importUsers(@RequestBody MultipartFile multipartFile) throws Exception {
        System.out.println(multipartFile);
        System.out.println(multipartFile.getOriginalFilename());
        if(userService.importUsers(multipartFile.getInputStream(),multipartFile.getOriginalFilename())){
            return JsonData.buildSuccess("导入成功");
        }else{
            return JsonData.buildError();
        }
    }

    @RequestMapping(value = "user/{id}",method = RequestMethod.DELETE)
    public JsonData deleteUser(@PathVariable("id") Integer id){
        if(userService.delUser(id)) {
            return JsonData.buildSuccess("删除成功");
        }
        return JsonData.buildError();
    }

    @DeleteMapping("users")
//    @RequestMapping(value = "users",method = RequestMethod.DELETE)
    public JsonData deleteUsers(@RequestBody IdlsDTO dto){
        List<Integer> list = new ArrayList<>();
        Integer[] idls = dto.getIdls();
        for (Integer idl : idls) {
            list.add(idl);
        }
        if(userService.delUserList(list)){
                return JsonData.buildSuccess("删除成功");
            }else{
                return JsonData.buildError();
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

