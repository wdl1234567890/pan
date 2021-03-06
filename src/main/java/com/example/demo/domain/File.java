package com.example.demo.domain;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.validation.BindException;

import com.example.demo.enums.StatusCode;
import com.example.demo.exception.PanException;
import com.example.demo.validate.group.Group;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class File implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file.id
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
	
	@NotNull(message = "id不能为空", groups = Group.UpdateDirOrFile.class)
    @Min(message = "id必须是大于等于1的整数", value = 1, groups = Group.UpdateDirOrFile.class)
	private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file.parent_id
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
	@NotNull(message = "parentId不能为空", groups = {Group.CreateDir.class, Group.CreateFile.class})
	@Min(message = "parentId必须是大于等于0的整数", value = 0, groups = {Group.CreateDir.class, Group.CreateFile.class})
	private Integer parentId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file.name
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
	@NotBlank(message = "name不能为空", groups = {Group.CreateDir.class, Group.CreateFile.class, Group.UpdateDirOrFile.class})
    @Size(message="name允许的长度为1~20", min=1, max=20, groups = {Group.CreateDir.class, Group.CreateFile.class, Group.UpdateDirOrFile.class})
	private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file.type
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    private Integer type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file.creator_id
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    private Integer creatorId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file.update_day
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    private Date updateDay;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file.create_day
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    private Date createDay;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file.object_name
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    
    @NotBlank(message = "objectName不能为空", groups = Group.CreateFile.class)
    @Size(message="objectName允许的长度为1~100", min=1, max=100, groups = Group.CreateFile.class)
    private String objectName;
    

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table file
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file.id
     *
     * @return the value of file.id
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file.id
     *
     * @param id the value for file.id
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file.parent_id
     *
     * @return the value of file.parent_id
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file.parent_id
     *
     * @param parentId the value for file.parent_id
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file.name
     *
     * @return the value of file.name
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file.name
     *
     * @param name the value for file.name
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public void setName(String name){
    	
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file.type
     *
     * @return the value of file.type
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file.type
     *
     * @param type the value for file.type
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file.creator_id
     *
     * @return the value of file.creator_id
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    @JsonIgnore
    public Integer getCreatorId() {
        return creatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file.creator_id
     *
     * @param creatorId the value for file.creator_id
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    @JsonProperty
    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file.update_day
     *
     * @return the value of file.update_day
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    
    public Date getUpdateDay() {
        return updateDay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file.update_day
     *
     * @param updateDay the value for file.update_day
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public void setUpdateDay(Date updateDay) {
        this.updateDay = updateDay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file.create_day
     *
     * @return the value of file.create_day
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public Date getCreateDay() {
        return createDay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file.create_day
     *
     * @param createDay the value for file.create_day
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public void setCreateDay(Date createDay) {
        this.createDay = createDay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file.object_name
     *
     * @return the value of file.object_name
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    @JsonIgnore
    public String getObjectName() {
        return objectName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file.object_name
     *
     * @param objectName the value for file.object_name
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    @JsonProperty
    public void setObjectName(String objectName) {
    	
        this.objectName = objectName == null ? null : objectName.trim();
    }
    

	@Override
	public String toString() {
		return "File [id=" + id + ", parentId=" + parentId + ", name=" + name + ", type=" + type + ", creatorId="
				+ creatorId + ", updateDay=" + updateDay + ", createDay=" + createDay + ", objectName=" + objectName
				+ "]";
	}
    
    
}