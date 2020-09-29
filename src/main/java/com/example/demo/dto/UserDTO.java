package com.example.demo.dto;

import com.example.demo.domain.User;
import com.example.demo.utils.PageRequest;

/**
 * @author GooRay
 * 创建于 2020/9/26
 */
public class UserDTO {

    private PageRequest pageRequest;

    private User user;

    public UserDTO() {
    }

    public UserDTO(PageRequest pageRequest, User user) {
        this.pageRequest = pageRequest;
        this.user = user;
    }

    public PageRequest getPageRequest() {
        return pageRequest;
    }

    public void setPageRequest(PageRequest pageRequest) {
        this.pageRequest = pageRequest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "pageRequest=" + pageRequest +
                ", user=" + user +
                '}';
    }
}
