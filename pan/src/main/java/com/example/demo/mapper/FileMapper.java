package com.example.demo.mapper;

import com.example.demo.domain.File;
import com.example.demo.domain.FileExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FileMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file
     *
     * @mbggenerated Mon Sep 14 16:32:40 CST 2020
     */
    int countByExample(FileExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file
     *
     * @mbggenerated Mon Sep 14 16:32:40 CST 2020
     */
    int deleteByExample(FileExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file
     *
     * @mbggenerated Mon Sep 14 16:32:40 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file
     *
     * @mbggenerated Mon Sep 14 16:32:40 CST 2020
     */
    int insert(File record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file
     *
     * @mbggenerated Mon Sep 14 16:32:40 CST 2020
     */
    int insertSelective(File record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file
     *
     * @mbggenerated Mon Sep 14 16:32:40 CST 2020
     */
    List<File> selectByExample(FileExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file
     *
     * @mbggenerated Mon Sep 14 16:32:40 CST 2020
     */
    File selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file
     *
     * @mbggenerated Mon Sep 14 16:32:40 CST 2020
     */
    int updateByExampleSelective(@Param("record") File record, @Param("example") FileExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file
     *
     * @mbggenerated Mon Sep 14 16:32:40 CST 2020
     */
    int updateByExample(@Param("record") File record, @Param("example") FileExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file
     *
     * @mbggenerated Mon Sep 14 16:32:40 CST 2020
     */
    int updateByPrimaryKeySelective(File record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file
     *
     * @mbggenerated Mon Sep 14 16:32:40 CST 2020
     */
    int updateByPrimaryKey(File record);
}