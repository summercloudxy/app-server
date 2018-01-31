package com.zgiot.app.server.service.impl.mapper;

import com.zgiot.app.server.service.pojo.HistdataWhitelistModel;
import com.zgiot.common.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface TMLMapper {

    @Select("SELECT * FROM `tb_metric` ")
    List<MetricModel> findAllMetrics();

    @Select("SELECT metrictype_code as metricCode,metrictype_name as metricName from tb_metrictype")
    List<MetricModel> findAllMetricTypes();

    @Select("SELECT * FROM `tb_thing` ")
    List<ThingModel> findAllThings();

    @Select("SELECT * FROM `tb_thing_properties`")
    List<ThingPropertyModel> findAllProperties();

    @Select("SELECT * FROM rel_historydata_whitelist")
    List<HistdataWhitelistModel> findAllHistdataWhitelist();

    @Select("SELECT metric_code FROM `rel_thing_metric_label` WHERE thing_code = #{thingCode}")
    Set<String> findMetricsOfThing(String thingCode);

    @Select("SELECT * FROM `tb_building`")
    List<BuildingModel> findAllBuildings();

    @Select("SELECT * FROM `tb_system`")
    List<SystemModel> findAllSystems();

    /**
     * 根据资产类别获取部件/设备类别
     * 
     * @param assetType
     * @return
     */
    @Select("SELECT DISTINCT(code),category_name FROM `tb_category` c,tb_thing t WHERE c.`code` = t.thing_type2_code and t.thing_type1_code = #{assetType};")
    List<CategoryModel> getCategoryListByAssetType(String assetType);

    /**
     * 获取全部设备/部件类别
     * 
     * @return
     */
    @Select("SELECT id,code,category_name FROM `tb_category`;")
    List<CategoryModel> getCategoryList();

    /**
     * 获取指定的资产、类别、设备上可能存在的信号类型
     * 
     * @param assetType
     * @param category
     * @return
     */
    List<CategoryModel> getMetricTypeByAssetAndCategory(@Param("assetType") String assetType,
            @Param("category") String category, @Param("thingCode") String thingCode);

    /**
     * 获取指定资产、类别、设备、信号类型下可能存在的信号
     * 
     * @param assetType
     * @param category
     * @param thingCode
     * @param metricType
     * @return
     */
    List<MetricModel> getMetricByAssetAndCategory(@Param("assetType") String assetType,
            @Param("category") String category, @Param("thingCode") String thingCode,
            @Param("metricType") String metricType, @Param("metricClass") String metricClass);

    List<ThingModel> getThingCodeByAssetAndCategory(@Param("assetType") String assetType,
            @Param("category") String category, @Param("metricCode") String metricCode,
            @Param("metricType") String metricType,@Param("thingStartCode")String thingStartCode);
}