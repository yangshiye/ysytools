package com.ysy.basetools;

import com.ysy.basetools.export.Column;

public class User {

    @Column(title = "用户id",sortVal = 1)
    private Long id;
    @Column(title = "姓名",sortVal = 2)
    private String name;
    @Column(title = "性别",cellConvert = SexEnum.class,sortVal = 3)
    private Integer sex;

    public User(Long id, String name, Integer sex) {
        this.id = id;
        this.name = name;
        this.sex = sex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                '}';
    }
}
