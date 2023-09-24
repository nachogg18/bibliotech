package com.bibliotech.utils;

import java.util.List;

public class RoleUtils {
    public final static String VALID_ROL_NAME_FORMAT = "[A-Z]{4,50}";
    public final static String DEFAULT_ROL_USER = "USER";
    public final static String ROL_ADMIN = "ADMIN";
    public final static String ROL_SUPER_ADMIN = "SUPERADMIN";


    public static List<String> GetRolesWithAdminProfile() {
        return List.of(
                ROL_ADMIN,
                ROL_SUPER_ADMIN
        );
    }

}
