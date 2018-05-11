package com.zgiot.app.server.module.equipments.pojo;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 用于菜单
     */
    private List nodeList = new ArrayList();

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

    public List getNodeList() {
        return nodeList;
    }

    public void setNodeList(List nodeList) {
        this.nodeList = nodeList;
    }
}
