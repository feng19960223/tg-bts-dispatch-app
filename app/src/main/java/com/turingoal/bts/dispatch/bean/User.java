package com.turingoal.bts.dispatch.bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import lombok.Data;

/**
 * 用户
 */
@Data
@Entity
public class User {
    @Id
    public long entityId; // objectBox 的id需要为Long 类型
    public String id; // 用户
    public String username; // 用户名
    public String realname; // 真实姓名
    public String workGroupName; // 检修班组

    public User() {
    }
}
