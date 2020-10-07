/**  

* @Title: 

* @Description: (这里用一句话描述这个方法的作用)  

* @param    参数  

* @return    返回类型  

* @throws  

*/  
package com.example.demo.controller;

import com.example.demo.domain.Department;
import com.example.demo.domain.PageResult;
import com.example.demo.domain.User;
import com.example.demo.dto.IdlsDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.UserService;
import com.example.demo.utils.ExcelUtil;
import com.example.demo.utils.PageRequest;
import com.example.demo.vo.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 白开水
 *
 */
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/pri/admin/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(value = "user",method = RequestMethod.POST)
    public JsonData addUser(@RequestBody User user) {
        userService.addUser(user);
        return JsonData.buildSuccess(userService.getUserById(user.getId()));
    }

    @RequestMapping("downLoad/{departmentId}")
    public ResponseEntity<byte[]> download(@PathVariable("departmentId")Integer departmentId ,HttpSession session) throws Exception {

        //获取下载路径
//        String path = ResourceUtils.getURL("classpath:").getPath();

        String path = new ApplicationHome(getClass()).getSource().getParentFile().getPath();
        String finalPath = path + File.separator + "hello.xlsx";

        //获取部门信息
        Department department = departmentService.findDepartmentById(departmentId);
        ExcelUtil.DownLoadExcel(new FileOutputStream(finalPath),department);
        InputStream is = new FileInputStream(finalPath);
        //available()获取输入流所读文件的最大字节数
        byte[] b = new byte[is.available()];
        is.read(b);

        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition","attachment;filename=default.xlsx");
        headers.add("Content-Type","text/html");
        HttpStatus ok = HttpStatus.OK;

        ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(b,headers,ok);
        is.close();
        return entity;
    }

    @RequestMapping(value = "importUsers",method = RequestMethod.POST)
    public JsonData importUsers(@RequestBody MultipartFile multipartFile) throws Exception {
        //获取部门信息
        List<Department> departments = departmentService.listAllDepartment();
        if(userService.importUsers(multipartFile.getInputStream(),multipartFile.getOriginalFilename(),departments)){
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

//    @RequestMapping(value = "users",method = RequestMethod.GET)
//    public JsonData findUsers(){
//        List<User> users = userService.listAllUser();
//        return JsonData.buildSuccess(users);
//    }

    @RequestMapping(value = "user/{id}",method = RequestMethod.GET)
    public JsonData getUser(@PathVariable("id") Integer id){
        User user = userService.getUserById(id);
        return JsonData.buildSuccess(user);
    }

    @PostMapping("users")
    public JsonData getUsers(@RequestBody UserDTO userDTO){
        PageResult page = userService.findPage(userDTO.getPageRequest(),userDTO.getUser());
        return JsonData.buildSuccess(page);
    }

    @GetMapping("/users/{level}")
    public JsonData getUsersByLevel(@PathVariable("level")Integer level){
        List<User> users = userService.findUserByLevel(level);
        return JsonData.buildSuccess(users);
    }

}

