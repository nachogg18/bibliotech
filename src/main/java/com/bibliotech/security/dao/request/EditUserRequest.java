package com.bibliotech.security.dao.request;

import jakarta.validation.constraints.Email;
import java.util.List;

public record EditUserRequest(String firstName,
                              String lastName,
                              String password,
                              @Email
                              String email,
                              List<Long> roleIds,
                              Boolean enabled) {
}
