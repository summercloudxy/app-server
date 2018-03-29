package com.zgiot.app.server.module.equipments.pojo;

public class TBSystem {

    /**
     *主键
     */
    private Long id;

    /**
     *系统名
     */
    private String systemName;

    /**
     *父id
     */
    private Long parentSystemId;

    /**
     *level
     */
    private Integer level;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Long getParentSystemId() {
        return parentSystemId;
    }

    public void setParentSystemId(Long parentSystemId) {
        this.parentSystemId = parentSystemId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
