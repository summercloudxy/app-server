package com.zgiot.app.server.module.sfstop.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.core.annotation.Order;

@ApiModel("分页列表")
public class PageListRsp<T> {
    @Order(1)
    @ApiModelProperty("当前页")
    private int pageNum;

    @Order(2)
    @ApiModelProperty("每页的数量")
    private int pageSize;

    @Order(3)
    @ApiModelProperty("当前页的数量")
    private int size;

    @Order(4)
    @ApiModelProperty("总记录数")
    private long total;

    @Order(5)
    @ApiModelProperty("结果集")
    private T list;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public T getList() {
        return list;
    }

    public void setList(T list) {
        this.list = list;
    }
}
