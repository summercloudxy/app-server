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

    @Select("select * from tb_thing tb,rel_thingtag_thing re where re.thing_tag_code=#{thingTag} and tb.thing_code=re.thing_code")
    List<ThingModel> fingThingByTag(@Param("thingTag") String thingTag);

    @Select("SELECT * FROM `tb_thing_properties`")
    List<ThingPropertyModel> findAllProperties();

    @Select("SELECT * FROM rel_historydata_whitelist")
    List<HistdataWhitelistModel> findAllHistdataWhitelist();

    @Select("SELECT * FROM rel_historydata_whitelist where minute_summary=1 ")
    List<HistdataWhitelistModel> findAllHistdataMinWhitelist();

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
                                                    @Param("metricType") String metricType, @Param("thingStartCode") String thingStartCode);

    @Select("select * from tb_metric a," +
            "(select metric_code from rel_thing_metric_label where thing_code=#{thingCode}) b where  a.metric_code=b.metric_code and a.metric_type1_code=#{metricType}")
    List<MetricModel> findMetricByThingCode(@Param("thingCode") String thingCode, @Param("metricType") String metricType);

    @Select("select * from tb_metric a," +
            "(select metric_code from rel_thing_metric_label where thing_code=#{thingCode}) b where  a.metric_code=b.metric_code")
    List<MetricModel> findMetric(@Param("thingCode") String thingCode);

    @Select("select thing_code from rel_thing_metric_label where thing_code like #{thingCode} group by thing_code")
    List<String> findRelateThing(@Param("thingCode") String thingCode);


    @Select("SELECT * FROM `tb_thing_properties` where thing_code=#{thingCode} and prop_key=#{propKey}")
    ThingPropertyModel findThingProperties(@Param("thingCode") String thingCode, @Param("propKey") String propKey);


}