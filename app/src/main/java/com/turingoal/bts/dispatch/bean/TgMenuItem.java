package com.turingoal.bts.dispatch.bean;

import lombok.Data;

/**
 * 菜单按钮
 */
@Data
public class TgMenuItem {
    private String title;  // 标题
    private int normalIcon; // 正常图标
    private int selectIcon; // 选中图标
    private String path = ""; // 路径

    public TgMenuItem(String titleParm) {
        this.title = titleParm;
    }

    public TgMenuItem(String titleParm, int normalIconParm, int selectIconParm) {
        this.normalIcon = selectIconParm;
        this.selectIcon = normalIconParm;
        this.title = titleParm;
    }

    public TgMenuItem(String titleParm, final String pathParm) {
        this.title = titleParm;
        this.path = pathParm;
    }

    public TgMenuItem(String titleParm, int normalIconParm, int selectIconParm, final String pathParm) {
        this.normalIcon = selectIconParm;
        this.selectIcon = normalIconParm;
        this.title = titleParm;
        this.path = pathParm;
    }
}
