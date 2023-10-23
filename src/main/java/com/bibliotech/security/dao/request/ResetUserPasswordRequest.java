package com.bibliotech.security.dao.request;

import jakarta.validation.constraints.Email;

public record ResetUserPasswordRequest(@Email String email) {
}
