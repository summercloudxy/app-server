package com.zgiot.app.server.module.auth.mapper;

import com.zgiot.app.server.module.auth.pojo.Authority;
import com.zgiot.app.server.module.auth.pojo.AuthorityDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AuthorityMapper {
    @Insert("INSERT INTO s_authority (id , name , alias , remark , parent_id , code) \n" +
            " VALUES (#{id} , #{name} , #{alias} , #{remark} , #{parentId} , #{code},#{enable})")
    void insert(Authority obj);

    @Update("UPDATE s_authority\n" +
            "SET\n" +
            "id=#{id},\n" +
            "name=#{name},\n" +
            "alias=#{alias},\n" +
            "remark=#{remark},\n" +
            "parent_id=#{parentId},\n" +
            "code=#{code},\n" +
            "enable=#{enable}\n" +
            "WHERE id=#{id}")
    void update(Authority obj);

    @Delete("DELETE FROM `s_authority` WHERE id=#{id}")
    void delete(int id);

    @Select("SELECT * FROM s_authority WHERE id=#{id}")
    Authority getAuthority(int id);

    // detail
    @Insert("INSERT INTO s_authority_detail (authority_id , resource_type , resource_desc , resource_position , ciconcls , resource_level) \n" +
            " VALUES (#{authorityId} , #{resourceType} , #{resourceDesc} , #{resourcePosition} , #{ciconcls} , #{resourceLevel})")
    void insertDetail(AuthorityDetail obj);

    @Update("UPDATE s_authority_detail\n" +
            "SET\n" +
            "  authority_id=#{authorityId},\n" +
            "  resource_type=#{resourceType},\n" +
            "  resource_desc=#{resourceDesc},\n" +
            "  resource_position=#{resourcePosition},\n" +
            "  ciconcls=#{ciconcls},\n" +
            "  resource_level=#{resourceLevel}\n" +
            "WHERE id=#{id}")
    void updateDetail(AuthorityDetail obj);

    @Delete("DELETE FROM s_authority_detail WHERE authority_id=#{authId}")
    void deleteDetail(int authId);

    @Select("SELECT * FROM s_authority_detail WHERE authority_id=#{authorityId}")
    AuthorityDetail getAuthorityDetail(int authorityId);

    @Select("SELECT * FROM s_authority_detail")
    List<AuthorityDetail> findAllAuthorityDetail();

    //// 关系

    /**
     * 不支持authority递归，配置时需要枝节点与叶节点同时保存
     *
     * @param userId
     * @return
     */
    @Select("SELECT a.* FROM `s_user_role` ur, `rel_role_authority_group` rag, `rel_authoritygroup_authority` aga , `s_authority` a\n" +
            "WHERE ur.`role_id`=rag.`role_id` AND rag.`authority_group_id`=aga.`authority_group_id`  AND a.`id`=aga.`authority_id` AND ur.`user_id`=#{userId}\n" +
            "  UNION\n" +
            "SELECT a.* FROM `s_user_role` ur, `s_role_authority` ra, `s_authority` a\n" +
            "WHERE ur.`role_id`=ra.`role_id` AND ra.`authority_id`=a.`id` AND ur.`user_id`=#{userId} ")
    List<Authority> findAuthorityViaRoleOrAuthGroupByUser(long userId);

    /**
     * 不支持authority递归，配置时需要枝节点与叶节点同时保存
     *
     * @param userUuid
     * @return
     */
    @Select("SELECT a.* FROM `s_user_role` ur, `rel_role_authority_group` rag, `rel_authoritygroup_authority` aga , `s_authority` a,s_user u WHERE ur.`role_id`=rag.`role_id` " +
            "AND rag.`authority_group_id`=aga.`authority_group_id`  AND a.`id`=aga.`authority_id` and u.id=ur.user_id and u.uuid =#{userUuid} " +
            "  UNION " +
            "SELECT a.* FROM `s_user_role` ur, `s_role_authority` ra, `s_authority` a,s_user u WHERE " +
            "ur.`role_id`=ra.`role_id` AND ra.`authority_id`=a.`id` and u.id=ur.user_id AND u.uuid=#{userUuid}")
    List<Authority> findAuthorityViaRoleOrAuthGroupByUserUuid(String userUuid);

    @Select("select * from s_authority where authority_type_id=#{authorityTypeId}")
    List<Authority> getAuthorityByAuthorityTypeId(@Param("authorityTypeId") int authorityTypeId);

    @Select("select * from s_authority a,(select authority_id from rel_authoritygroup_authority where authority_group_id=#{authGroupId}) b where a.id=b.authority_id")
    List<Authority> getAuthorityByAuthGroupId(@Param("authGroupId") int authGroupId);

    @Select("select a.* from s_authority a where a.platclient_id=#{platclientId}")
    List<Authority> getAuthorityList(@Param("platclientId") int platclientId);

    @Select("select * from s_role_authority a,s_authority b where a.role_id=#{roleId} and b.id=a.authority_id ")
    List<Authority> getRoleAuthority(@Param("roleId") int roleId);

    List<Authority> getAuthorityByAuthorityCode(@Param("authorityCodeList") List<String> authorityCodeList);

    @Select("select * from s_authority")
    List<Authority> getAllAuthority();
}
