package com.inetcar.around;

/**
 * 封装grid的名字和图标
 */
public class GridViewContainer {

    private String name;
    private Integer icon;

    public GridViewContainer(String name, Integer icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }
}
