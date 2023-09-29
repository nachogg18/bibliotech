package com.bibliotech.utils;


import java.util.List;

public class RoleUtils {
    public final static String VALID_ROLE_NAME_FORMAT = "[A-Z]{4,50}";
    public final static String ROLE_ADMIN = "ADMIN";
    public final static String ROLE_SUPER_ADMIN = "SUPERADMIN";
    public final static String DEFAULT_ROLE_USER = "USER";

    public final static String BIBLIOTECARIO_ROLE = "BIBLIOTECARIO";


    public static List<String> GetRolesWithAdminProfile() {
        return List.of(
                ROLE_ADMIN,
                ROLE_SUPER_ADMIN
        );
    }



}
