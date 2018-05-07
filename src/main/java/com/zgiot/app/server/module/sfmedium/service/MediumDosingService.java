package com.zgiot.app.server.module.sfmedium.service;

import com.zgiot.app.server.module.sfmedium.entity.po.MediumDosingConfigDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MediumDosingService {


    /**
     * 查询所有的加介系统
     *
     * @return
     */
    List<MediumDosingConfigDO> getMediumDosingGroup();


    /**
     * 根据加介系统查询对应的分选系统
     *
     * @param systemid
     * @return
     */
    List<MediumDosingConfigDO> getSeparatingSystemById(String systemid);

    /**
     * 根据加介系统查询对应的分选系统
     *
     * @param systemid
     * @return
     */
    List<MediumDosingConfigDO> getSeparatingSystem(String systemid, String system);

    /**
     * 查询所有的加介系统
     *
     * @returngetSeparatingSystem
     */
    List<MediumDosingConfigDO> getAllMediumDosing();

    /**
     * 根据加介池查询加介配置
     *
     * @param mediumpoolCode
     * @return
     */
    List<MediumDosingConfigDO> getMediumDosingConfigByMediumPoolCode(String mediumpoolCode);

    /**
     * 根据加介泵查询一二期切换阀
     *
     * @param mediumdosingpumpCode
     * @return
     */
    List<MediumDosingConfigDO> getChangeValue(String mediumdosingpumpCode);

    /**
     * 根据加介泵和切换阀门查询加介阀和合介桶
     *
     * @param mediumdosingpumpCode
     * @return
     */
    List<MediumDosingConfigDO> getMediumdosingvalueAndCombinedbucketCode(@Param("mediumdosingpumpCode") String mediumdosingpumpCode, @Param("changevalueCode") String changevalueCode);


    /**
     * 根据合介桶code查询加介配置
     *
     * @param combinedbucketCode
     * @return
     */
    MediumDosingConfigDO getMediumDosingConfigDO(String combinedbucketCode);

}
                                                  