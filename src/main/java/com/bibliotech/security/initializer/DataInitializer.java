package com.bibliotech.security.initializer;

import com.bibliotech.security.dao.request.CreateRoleRequest;
import com.bibliotech.security.dao.request.SignUpRequest;
import com.bibliotech.security.service.AuthenticationService;
import com.bibliotech.security.service.RoleService;
import com.bibliotech.utils.RoleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private RoleService roleService; // Aquí inyecta el servicio necesario

    @Autowired
    private AuthenticationService authenticationService; // Aquí inyecta el servicio necesario

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //crea default rol user
        roleService.create(new CreateRoleRequest(RoleUtils.DEFAULT_ROL_USER));

        //crea rol super admin
        roleService.create(new CreateRoleRequest(RoleUtils.ROL_SUPER_ADMIN));

        //crea
        authenticationService.signup(
                new SignUpRequest(
                        "SUPERADMIN",
                        "SUPERADMIN",
                        "email@superadmin",
                        "password",
                        RoleUtils.ROL_SUPER_ADMIN
                        )
        );
    }
}