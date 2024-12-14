package org.atlas.service.user.application.handler.command;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.exception.AppError;
import org.atlas.commons.exception.BusinessException;
import org.atlas.commons.util.crypt.PasswordUtil;
import org.atlas.platform.cqrs.handler.CommandHandler;
import org.atlas.service.user.contract.auth.AuthService;
import org.atlas.service.user.contract.command.SignUpCommand;
import org.atlas.service.user.contract.repository.UserRepository;
import org.atlas.service.user.domain.Customer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SignUpCommandHandler implements CommandHandler<SignUpCommand, Void> {

  private static final BigDecimal INITIAL_CREDIT = BigDecimal.valueOf(1000);

  private final UserRepository userRepository;
  private final AuthService authService;

  @Override
  @Transactional
  public Void handle(SignUpCommand command) throws Exception {
    if (userRepository.findByUsername(command.getUsername()).isPresent()) {
      throw new BusinessException(AppError.USERNAME_ALREADY_REGISTERED);
    }
    if (userRepository.findByEmail(command.getEmail()).isPresent()) {
      throw new BusinessException(AppError.EMAIL_ALREADY_REGISTERED);
    }

    Customer customer = newCustomer(command);
    userRepository.insert(customer);

    authService.signUp(command);

    log.info("User {} registered successfully", command.getEmail());
    return null;
  }

  private Customer newCustomer(SignUpCommand command) {
    Customer customer = new Customer();
    customer.setUsername(command.getUsername());
    customer.setPassword(PasswordUtil.encode(command.getPassword()));
    customer.setFirstName(command.getFirstName());
    customer.setLastName(command.getLastName());
    customer.setEmail(command.getEmail());
    customer.setPhoneNumber(command.getPhoneNumber());
    customer.setCredit(INITIAL_CREDIT);
    return customer;
  }
}
