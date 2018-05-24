package com.zgiot.app.server.module.reportforms.output.pojo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@ApiModel("影响时间请求对象")
public class InfluenceTimeReq {


   @ApiModelProperty("备注对象")
   private InfluenceTimeRemarks influenceTimeRemarks;

   @ApiModelProperty("影响时间集合对象")
   private List<InfluenceTime> influenceTimeList;

   @ApiModelProperty(value = "当班开始时间",required = true)
   private Date dutyStartTime;

   public InfluenceTimeRemarks getInfluenceTimeRemarks() {
      return influenceTimeRemarks;
   }

   public void setInfluenceTimeRemarks(InfluenceTimeRemarks influenceTimeRemarks) {
      this.influenceTimeRemarks = influenceTimeRemarks;
   }

   public List<InfluenceTime> getInfluenceTimeList() {
      return influenceTimeList;
   }

   public void setInfluenceTimeList(List<InfluenceTime> influenceTimeList) {
      this.influenceTimeList = influenceTimeList;
   }

   public Date getDutyStartTime() {
      return dutyStartTime;
   }

   public void setDutyStartTime(Date dutyStartTime) {
      this.dutyStartTime = dutyStartTime;
   }
}
                                                  