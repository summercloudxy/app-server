package com.zgiot.app.server.module.auth.mapper;

import com.zgiot.app.server.module.auth.pojo.User;
import com.zgiot.app.server.module.auth.pojo.UserDepartmentStation;
import com.zgiot.app.server.module.auth.pojo.UserDetail;
import com.zgiot.app.server.module.auth.pojo.UserLogin;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO s_user ( version , uuid, login_name , person_name, password , regist_date , update_date , login_date , logout_date , account_non_expired , credentials_non_expired , account_non_locked , enabled)\n" +
            " VALUES (#{version} , #{uuid} , #{loginName} , #{personName} , #{password} , #{registDate} , #{updateDate} , #{loginDate} , #{logoutDate} , #{accountNonExpired} , #{credentialsNonExpired} , #{accountNonLocked} , #{enabled})")
    @SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = long.class)
    void insertUser(User user);

    @Delete("DELETE FROM s_user WHERE id = #{userId}")
    void deleteUser(long userId);

    @Update("UPDATE s_user\n" +
            "        SET\n" +
            "            version=#{version},\n" +
            "            login_name=#{loginName},\n" +
            "            person_name=#{personName},\n" +
            "            `password`=#{password},\n" +
            "            regist_date=#{registDate},\n" +
            "            update_date=#{updateDate},\n" +
            "            login_date=#{loginDate},\n" +
            "            logout_date=#{logoutDate},\n" +
            "            account_non_expired=#{accountNonExpired},\n" +
            "            credentials_non_expired=#{credentialsNonExpired},\n" +
            "            account_non_locked=#{accountNonLocked},\n" +
            "            enabled=#{enabled}\n" +
            "        where id=#{id}")
    void updateUser(User user);

    @Select("SELECT * FROM `s_user` where id=#{userId} order by update_date desc")
    User getUser(long userId);


    @Select("SELECT * FROM `s_user` where uuid=#{uuid}")
    User getUserByUuid(String uuid);

    @Select("SELECT * FROM `s_user` order by update_date desc")
    List<User> findAllUsers();

    @Select("SELECT * FROM `s_user` u, `s_department_station_user` uds " +
            "WHERE uds.`user_id`=u.`id` AND uds.`department_id`=#{departmentId} order by u.update_date desc")
    List<User> findUsersByDepartment(int departmentId);

    @Select("SELECT count(1) FROM `s_user` u, `s_department_station_user` uds " +
            "WHERE uds.`user_id`=u.`id` AND uds.`department_id`=#{departmentId} ")
    int getUserCountByDepartment(int departmentId);

    @Select("SELECT * FROM `s_user` where login_name=#{loginName} ")
    User getUsersByLoginName(String loginName);

    @Select("SELECT * FROM `s_user` where person_name=#{personName} ")
    User getUsersByPersonName(String personName);

    @Insert("INSERT INTO s_user_detail (user_id , id_num , email , mobile , join_dt , code , remark) \n" +
            " VALUES (#{userId} , #{idNum} , #{email} , #{mobile} , #{joinDt} , #{code} , #{remark})")
    void insertUserDetail(UserDetail obj);

    @Update("UPDATE s_user_detail\n" +
            "  SET\n" +
            "  user_id=#{userId},\n" +
            " id_num=#{idNum},\n" +
            " email=#{email},\n" +
            " mobile=#{mobile},\n" +
            " join_dt=#{joinDt},\n" +
            " code=#{code},\n" +
            " remark=#{remark},\n" +
            " sex=#{sex},\n" +
            " birthday=#{birthday},\n" +
            " address=#{address},\n" +
            " work_address=#{workAddress},\n" +
            " graduated_from=#{graduatedFrom},\n" +
            " major=#{major},\n" +
            " emergency_contact_mobile=#{emergencyContactMobile},\n" +
            " fixed_tele=#{fixedTele} \n" +
            "  where user_id=#{userId} ")
    void updateUserDetail(UserDetail obj);

    @Delete("DELETE FROM s_user_detail where user_id=#{userId}")
    void deleteUserDetail(long userId);

    @Select("SELECT * FROM s_user_detail WHERE user_id=#{userId}")
    UserDetail getUserDetail(long userId);

    // user depart station
    @Insert("INSERT INTO s_department_station_user (department_id , station_id , user_id,workshop_post_id) \n" +
            " VALUES (#{departmentId} , #{stationId} , #{userId},#{workshop_post_id})")
    void insertUds(UserDepartmentStation obj);

    @Delete("DELETE FROM `s_department_station_user` WHERE user_id=#{userId}")
    void deleteUdsByUser(long userId);

    @Select("select * from s_department_station_user where user_id=#{userId}")
    List<UserDepartmentStation> getUDSByUserId(@Param("userId") long userId);

    @Select("select count(1) from s_user")
    int getUserSum();

    @Select("select * from tb_user_login where user_uuid=#{userUuid} and platclient_id=#{platClientId}")
    UserLogin getUserLogin(@Param("userUuid") String userUuid, @Param("platClientId") int platClientId);

    @Insert("insert into tb_user_login(user_uuid,platclient_id,token,update_date,ip_address,mac_address) " +
            "values(#{userUuid},#{platClientId},#{token},#{updateDate},#{ipAddress},#{macAddress}) ")
    void addUserLogin(UserLogin userLogin);

    @Update("update tb_user_login set token=#{token},update_date=#{updateDate} where user_uuid=#{userUuid}")
    void editUserLogin(UserLogin userLogin);

    @Delete("delete from tb_user_login where user_uuid=#{userUuid} and platclient_id=#{platClientId}")
    void deleteUserLogin(@Param("userUuid") String userUuid, @Param("platClientId") int platClientId);
}