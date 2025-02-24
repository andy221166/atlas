package org.atlas.service.user.adapter.persistence.mybatis.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.atlas.platform.commons.paging.PageRequest;
import org.atlas.service.user.domain.entity.UserEntity;
import org.atlas.service.user.port.outbound.repository.FindUserCriteria;

@Mapper
public interface UserMapper {

  List<UserEntity> findByIdIn(@Param("ids") List<Integer> ids);

  List<UserEntity> findByCriteria(FindUserCriteria criteria, PageRequest pageRequest);

  UserEntity findById(@Param("id") Integer id);

  UserEntity findByUsername(@Param("username") String username);

  UserEntity findByEmail(@Param("email") String email);

  UserEntity findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

  long countByCriteria(FindUserCriteria criteria);

  int insert(@Param("user") UserEntity userEntity);
}
