package com.zgiot.app.server.module.sfmedium.mapper;


import com.zgiot.app.server.module.sfmedium.entity.po.MediumDosingConfigDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MediumDosingMapper {


    /**
     * 查询所有的加介系统
     *
     * @returngetSeparatingSystem
     */
    @Select("select * from tb_medium_dosing_config  ")
    List<MediumDosingConfigDO> getAllMediumDosing();

    /**
     * 查询所有的加介系统
     *
     * @returngetSeparatingSystem
     */
    @Select("select * from tb_medium_dosing_config GROUP BY mediumdosingsystem_id ")
    List<MediumDosingConfigDO> getMediumDosingGroup();

    /**
     * 根据加介系统查询对应的分选系统
     *
     * @param systemid
     * @return
     */
    @Select("SELECT * from tb_medium_dosing_config WHERE mediumdosingsystem_id =#{systemid}")
    List<MediumDosingConfigDO> getSeparatingSystemById(@Param("systemid") String systemid);


    /**
     * 根据加介系统查询对应的分选系统
     *
     * @param systemid
     * @return
     */
    @Select("SELECT * from tb_medium_dosing_config WHERE mediumdosingsystem_id =#{systemid} and system=#{system}")
    List<MediumDosingConfigDO> getSeparatingSystem(@Param("systemid") String systemid, @Param("system") String system);

    /**
     * 根据加介池查询加介配置
     *
     * @param mediumpoolCode
     * @return
     */
    @Select("SELECT * from tb_medium_dosing_config where mediumpool_code =#{mediumpoolCode} ")
    List<MediumDosingConfigDO> getMediumDosingConfigByMediumPoolCode(@Param("mediumpoolCode") String mediumpoolCode);

    /**
     * 根据加介泵查询一二期切换阀
     *
     * @param mediumdosingpumpCode
     * @return
     */
    @Select(" select * from tb_medium_dosing_config WHERE mediumdosingpump_code =#{mediumdosingpumpCode} GROUP BY changevalue_code")
    List<MediumDosingConfigDO> getChangeValue(@Param("mediumdosingpumpCode") String mediumdosingpumpCode);

    /**
     * 根据加介泵和切换阀门查询加介阀和合介桶
     *
     * @param mediumdosingpumpCode
     * @return
     */
    @Select(" select * from tb_medium_dosing_config WHERE mediumdosingpump_code =#{mediumdosingpumpCode} and changevalue_code =#{changevalueCode} ")
    List<MediumDosingConfigDO> getMediumdosingvalueAndCombinedbucketCode(@Param("mediumdosingpumpCode") String mediumdosingpumpCode, @Param("changevalueCode") String changevalueCode);

    /**
     * 根据合介桶code查询加介配置
     *
     * @param combinedbucketCode
     * @return
     */
    @Select("select * from tb_medium_dosing_config where combinedbucket_code =#{combinedbucketCode}")
    MediumDosingConfigDO getMediumDosingConfigDO(@Param("combinedbucketCode") String combinedbucketCode);


}
                                                  