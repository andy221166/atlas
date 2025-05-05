package org.atlas.domain.user.usecase.admin;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.atlas.domain.user.entity.UserEntity;
import org.atlas.domain.user.repository.UserRepository;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.domain.user.usecase.admin.AdminListUserUseCaseHandler.ListUserInput;
import org.atlas.domain.user.usecase.admin.AdminListUserUseCaseHandler.UserOutput;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.framework.paging.PagingResult.Pagination;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class AdminListUserUseCaseHandler implements
    UseCaseHandler<ListUserInput, PagingResult<UserOutput>> {

  private final UserRepository userRepository;

  @Override
  public PagingResult<UserOutput> handle(ListUserInput input) throws Exception {
    PagingResult<UserEntity> userEntityPage = findSingleUser(input)
        .map(user -> PagingResult.of(List.of(user),
            Pagination.of(1, input.getPagingRequest())))
        .orElseGet(() -> {
          if (isFilterPresent(input)) {
            return PagingResult.empty();
          }
          return userRepository.findAll(input.getPagingRequest());
        });

    return ObjectMapperUtil.getInstance()
        .mapPage(userEntityPage, UserOutput.class);
  }

  private Optional<UserEntity> findSingleUser(ListUserInput input) {
    if (input.getId() != null) {
      return userRepository.findById(input.getId());
    } else if (StringUtils.isNotBlank(input.getUsername())) {
      return userRepository.findByUsername(input.getUsername());
    } else if (StringUtils.isNotBlank(input.getEmail())) {
      return userRepository.findByEmail(input.getEmail());
    } else if (StringUtils.isNotBlank(input.getPhoneNumber())) {
      return userRepository.findByPhoneNumber(input.getPhoneNumber());
    }
    return Optional.empty();
  }

  private boolean isFilterPresent(ListUserInput input) {
    return input.getId() != null ||
        StringUtils.isNotBlank(input.getUsername()) ||
        StringUtils.isNotBlank(input.getEmail()) ||
        StringUtils.isNotBlank(input.getPhoneNumber());
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ListUserInput {

    private Integer id;

    private String username;

    private String email;

    private String phoneNumber;

    @Valid
    private PagingRequest pagingRequest;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UserOutput {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;
  }
}