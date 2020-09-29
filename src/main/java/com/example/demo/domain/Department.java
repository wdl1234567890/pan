package com.example.demo.domain;

import java.io.Serializable;

public class Department implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column department.id
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column department.name
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column department.lead
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    private String lead;


    private Integer users;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table department
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column department.id
     *
     * @return the value of department.id
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column department.id
     *
     * @param id the value for department.id
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column department.name
     *
     * @return the value of department.name
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column department.name
     *
     * @param name the value for department.name
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column department.lead
     *
     * @return the value of department.lead
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public String getLead() {
        return lead;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column department.lead
     *
     * @param lead the value for department.lead
     *
     * @mbggenerated Mon Sep 14 20:05:20 CST 2020
     */
    public void setLead(String lead) {
        this.lead = lead == null ? null : lead.trim();
    }


    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lead='" + lead + '\'' +
                ", users=" + users +
                '}';
    }
}