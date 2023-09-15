package com.bibliotech.security.dao.request;


public record GetRoleRequest(Long id,
                             String name,
                             Boolean enabled) {
}
