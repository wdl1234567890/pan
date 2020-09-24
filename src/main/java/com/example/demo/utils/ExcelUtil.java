package com.example.demo.utils;

import com.example.demo.enums.StatusCode;
import com.example.demo.exception.PanException;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author GooRay
 * 创建于 2020/9/16
 */
public class ExcelUtil {

    public static List<Map<String, Object>> ReadExcel(InputStream inputStream, String filename) throws Exception {
        // 创建返回列表
        List<Map<String, Object>> employees = new ArrayList<>();

        // 规定文件内容头部
        String[] headersStr = {"姓名","密码","邮箱","手机号码","部门编号","是否组长"};

        // 文件内容头部与数据库字段对应
        String[] headers = {"name","pwd","mail","phone","department","level"};


        // 判断文件后缀格式
        if(!filename.endsWith(".xlsx")){
            throw new PanException(StatusCode.FILE_FORMAT_ERROR.code(),StatusCode.FILE_FORMAT_ERROR.message());
        }

        // 读取EXCEL文件
        Workbook sheets;
        try{
            sheets = WorkbookFactory.create(inputStream);
        }catch (Exception e){
            throw new PanException(StatusCode.FILE_READ_ERROR.code(),StatusCode.FILE_READ_ERROR.message());
        }

        // 获取EXCEL第一页
        Sheet sheet = sheets.getSheetAt(0);

        // 最大行数
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();

        // 判断文件是否为空
        if(physicalNumberOfRows == 0){
            throw new PanException(StatusCode.FILE_IS_EMPTY.code(),StatusCode.FILE_IS_EMPTY.message());
        }

        // 判断行数是否为1
        if(physicalNumberOfRows<=1){
            throw new PanException(StatusCode.USER_IS_EMPTY.code(),StatusCode.USER_IS_EMPTY.message());
        }

        // 获取头部（第一行）
        Row firstRow = sheet.getRow(0);

        // 最大列数
        int physicalNumberOfCells = firstRow.getPhysicalNumberOfCells();

        // 判断头部长度是否与模板对应
        if(headers.length!=physicalNumberOfCells){
            throw new PanException(StatusCode.FILE_FORMAT_ERROR.code(),StatusCode.FILE_FORMAT_ERROR.message());
        }

        // 对头部进行校验
        for (int i=0;i<physicalNumberOfCells;i++){
            if(!headersStr[i].equals(firstRow.getCell(i).toString())){
                throw new PanException(StatusCode.FILE_FORMAT_ERROR.code(),StatusCode.FILE_FORMAT_ERROR.message());
            }
        }

        // 遍历每一行进行校验
        List list = new ArrayList();
        HashSet set = new HashSet();
        for ( int i=1;i<physicalNumberOfRows;i++){

            // 获取第当前行
            Row row = sheet.getRow(i);

            // 判断当前行的长度
            if(row.getPhysicalNumberOfCells()!=headers.length){
                throw new Exception("第"+(i+1)+"行，格式错误");
            }
            // 遍历当前行的每一列
            for(int j=0;j<headers.length;j++) {

                // 判断是否存在未填项
                if("".equals(row.getCell(j).toString())){
                    throw new PanException(StatusCode.CONTENT_HAVE_EMPTY.code(), "第"+(i+1)+"行，存在未填项");
                }

                // 校验电子邮箱合理性
                if(headers[j].equals("mail")){
                    String mail = row.getCell(j).toString();
                    list.add(mail);
                    set.add(mail);
                    if(list.size()!=set.size()){
                        throw new PanException(StatusCode.MAIL_IS_EXISTED.code(), "第"+(i+1)+"行，电子邮箱重复");
                    }
                }

                // 检验电话号码的合理性
                if(headers[j].equals("phone")){
                    String phone = new BigDecimal(row.getCell(j).toString()).toPlainString();
                    list.add(phone);
                    set.add(phone);
                    if(list.size()!=set.size()){
                        throw new PanException(StatusCode.PHONE_IS_EXISTED.code(), "第"+(i+1)+"行，电子邮箱重复");
                    }
                    if(phone.length()!=11){
                        throw new PanException(StatusCode.PHONE_FORMAT_ERROR.code(),"第"+(i+1)+"行，电话号码格式错误");
                    }
                }
            }
        }

        // 获取信息
        for( int i=1 ;i<physicalNumberOfRows; i++){
            Row row = sheet.getRow(i);

            // 创建存储数据的map
            Map<String, Object> map = new HashMap<>();

            for(int j=0;j<headers.length;j++){

                // 转换电话的格式
                if(headers[j].equals("phone")){
                    String phone = new BigDecimal(row.getCell(j).toString()).toPlainString();
                    map.put(headers[j],phone);
                }

                //转换组长格式
                else if(headers[j].equals("level")){
                    if("否".equals(row.getCell(j).toString())){
                        map.put(headers[j],0);
                    }else{
                        map.put(headers[j],1);
                    }
                }

                // 转换部门格式
                else if(headers[j].equals("department")){
                    map.put(headers[j],(int) Double.parseDouble(row.getCell(j).toString()));
                }else{
                    map.put(headers[j],row.getCell(j).toString());
                }
            }
            employees.add(map);
        }
        return employees;
    }

}