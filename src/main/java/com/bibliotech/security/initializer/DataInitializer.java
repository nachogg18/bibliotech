package com.bibliotech.security.initializer;

import com.bibliotech.entity.Link;
import com.bibliotech.entity.Plataforma;
import com.bibliotech.security.dao.request.CreateRoleRequest;
import com.bibliotech.security.dao.request.SignUpRequest;
import com.bibliotech.security.entity.Privilege;
import com.bibliotech.security.service.AuthenticationService;
import com.bibliotech.security.service.PrivilegeService;
import com.bibliotech.security.service.RoleService;
import com.bibliotech.service.LinkService;
import com.bibliotech.service.PlataformaService;
import com.bibliotech.utils.PrivilegeUtils;
import com.bibliotech.utils.RoleUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PlataformaService plataformaService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private AuthenticationService authenticationService;




    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<Link> enabledLinks = List.of(createLink());

        createPlataformas(enabledLinks);
        
        createBasicPrivileges();
        
        createBasicRoles();
        
        createSuperAdminUser();
        
    }
    
    private void createBasicPrivileges() {
        privilegeService.savePrivilege(new Privilege(PrivilegeUtils.DEFAULT_PRIVILEGE));
        privilegeService.savePrivilege(new Privilege(PrivilegeUtils.PRIVILEGE_WRITE));
        privilegeService.savePrivilege(new Privilege(PrivilegeUtils.PRIVILEGE_EDIT));
        privilegeService.savePrivilege(new Privilege(PrivilegeUtils.PRIVILEGE_ADMIN));
    }

    private void createBasicRoles() {
        //get admin privilege
        Optional<Privilege> adminPrivilege = privilegeService.getPrivilegeByName(PrivilegeUtils.PRIVILEGE_ADMIN);
        
        if (adminPrivilege.isEmpty()) {
            throw new RuntimeException("error getting admin privilege");
        }
        
        // crea default rol user
        roleService.create(new CreateRoleRequest(RoleUtils.DEFAULT_ROL_USER, List.of()));

        //crea rol super admin
        roleService.create(new CreateRoleRequest(
                RoleUtils.ROL_SUPER_ADMIN, List.of(adminPrivilege.get().getId())));
    }
    
    private void createSuperAdminUser() {
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


    private Link createLink() {
        return linkService.save( Link.builder()
                                .url("hola").build());
    }
    private void createPlataformas(List<Link> enabledLinks) {

        plataformaService.save(
                Plataforma.builder()
                        .fechaAlta(Instant.now())
                        .links(enabledLinks)
                        .nombre("plataforma")
                        .build()
        );
    }
    
    
}
