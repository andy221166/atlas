package org.atlas.platform.persistence.mybatis.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.atlas.service.user.domain.User;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserMapper {

    List<User> findByIdIn(@Param("ids") List<Integer> ids);
    User findById(@Param("id") Integer id);
    User findByUsername(@Param("username") String username);
    User findByEmail(@Param("email") String email);
    int insert(@Param("user") User user);
}
