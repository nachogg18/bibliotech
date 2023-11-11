package com.bibliotech.security.service.impl;

import com.bibliotech.security.dao.request.EditUserRequest;
import com.bibliotech.security.dao.response.FindUserDto;
import com.bibliotech.security.entity.Role;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.repository.UserRepository;
import com.bibliotech.security.service.RoleService;
import com.bibliotech.security.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.stream.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    private final UserRepository userRepository;

    private final RoleService roleService;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll(Sort.by(Sort.Order.desc("startDate")));
    }

    @Override
    public List<FindUserDto> getUsers() {
        return userRepository.findAll()
                .stream().map(user -> FindUserDto.toDto(user)).toList();
    }

    @Override
    public User save(User user) {
        user.setLastUpdatedDate(Instant.now());
        user = userRepository.save(user);
        return user;
    }

    @Override
    public User edit(Long userId,EditUserRequest request) {


        return Streams.of(request)
                .map(
                editUserRequest -> {
                    //obtengo el user a partir del user id
                    User user = userRepository.findById(userId).orElseThrow(() -> new ValidationException(String.format("user id %s no encontrado", userId)));

                    var userUpdated = updateFirstName(user, request.firstName());

                    userUpdated = updateLastName(userUpdated, request.lastName());

                    userUpdated = updatePassword(userUpdated, request.password());

                    userUpdated = updateEmail(userUpdated, request.email());

                    userUpdated = updateRoles(userUpdated, request.roleIds());

                    userUpdated = updateEndDate(userUpdated, request.enabled());

                    userUpdated.setLastUpdatedDate(Instant.now());

                    return userRepository.save(userUpdated);

                }

        ).findAny().get();
    }

    @Override
    public Optional<User> getActiveUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .filter(User::isEnabled);
    }

    @Override
    public Optional<User> getUserToConfirmByEmail(String email) {
        return userRepository.findByEmail(email)
                .filter(User::isConfirmationRequired);
    }

    private User updatePassword(User user, String requestPassword) {
        if (StringUtils.isNotBlank(requestPassword)) {
            user.setPassword(requestPassword);
            return user;
        }
        return user;
    }


    private User updateFirstName(User user, String requestFirstName) {
        if (StringUtils.isNotBlank(requestFirstName)) {
            user.setFirstName(requestFirstName);
            return user;
        }
        return user;
    }

    private User updateLastName(User user, String requestLastName) {
        if (StringUtils.isNotBlank(requestLastName)) {
            user.setLastName(requestLastName);
            return user;
        }
        return user;
    }

    private User updateEmail(User user, String requestEmail) {
        if (StringUtils.isNotBlank(requestEmail)) {
            user.setEmail(requestEmail);
            return user;
        }
        return user;
    }

    private User updateRoles(User user, List<Long> roleIds) {
        //verifico si la lista es null
        if (Objects.nonNull(roleIds)) {

            //verifica que los roles para los roleIds existan
            List<Long> nonExistentRoles = new ArrayList<>();

            var roleMap = roleIds.stream().collect(Collectors.toMap(
                    roleIdToAdd -> roleIdToAdd,
                    roleIdToAdd -> roleService.findById(roleIdToAdd))
            ).entrySet();

            roleMap.stream().peek(
                    longOptionalEntry -> {
                        if (longOptionalEntry.getValue().isEmpty()) {
                            nonExistentRoles.add(longOptionalEntry.getKey());
                        }
                    }
            );

            if (!nonExistentRoles.isEmpty()) {
                throw new ValidationException(
                        String.format("roles no existentes : %s", nonExistentRoles.stream().map(roleId -> roleId.toString()).reduce(
                                "roleIs:", (partialResult, element) -> partialResult + element)
                        ));
            }

            //por cada rol remuevo al usuario del rol
            user.getRoles().stream().map(
                    role -> roleService.removeUserToRol(role.getId() ,user)
            );

            //limpio la lista de roles
            user.setRoles(new ArrayList<>());

            //por cada nuevo rol asigno el usuario en el rol y el rol en el usuario
            roleMap.stream().forEach(
                    entry -> {
                        logger.info(entry.toString());
                        Role role = roleService.assignUserToRol(entry.getKey(), user);
                        user.getRoles().add(role);
                    });
        }

        return user;
    }

    private User updateEndDate(User user, Boolean enabled) {
        if (Objects.nonNull(enabled) && enabled) {
            user.setEndDate(Instant.now());
            return user;
        } else {
            user.setEndDate(null);
        }
        return user;
    }

}