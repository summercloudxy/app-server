package com.zgiot.app.server.module.equipments.pojo;

public class CoalStorageDepot {

    /**
     * 主键
     */
    private Long id;

    /**
     *仓名称
     */
    private String name;

    /**
     *仓编号
     */
    private String number;

    /**
     *直径
     */
    private String diameter;

    /**
     *满仓位
     */
    private String level;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
