package com.bibliotech.security.initializer;

import com.bibliotech.entity.Autor;
import com.bibliotech.entity.Link;
import com.bibliotech.entity.Plataforma;
import com.bibliotech.entity.Publicacion;
import com.bibliotech.security.dao.request.SignUpRequest;
import com.bibliotech.security.dao.response.JwtAuthenticationResponse;
import com.bibliotech.security.entity.Action;
import com.bibliotech.security.entity.Privilege;
import com.bibliotech.security.entity.Resource;
import com.bibliotech.security.entity.Role;
import com.bibliotech.security.service.AuthenticationService;
import com.bibliotech.security.service.PrivilegeService;
import com.bibliotech.security.service.ResourceService;
import com.bibliotech.security.service.RoleService;
import com.bibliotech.service.AutorService;
import com.bibliotech.service.LinkService;
import com.bibliotech.service.PlataformaService;
import com.bibliotech.service.PublicacionService;
import com.bibliotech.utils.PrivilegeUtils;
import com.bibliotech.utils.ResourceNames;
import com.bibliotech.utils.ResourceUtils;
import com.bibliotech.utils.RoleUtils;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"local","dockerlocal"})
public class LocalDataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(LocalDataInitializer.class);
    
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

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private PublicacionService publicacionService;

    @Autowired
    private AutorService autorService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<Link> enabledLinks = List.of(createLink());

        createPlataformas(enabledLinks);

        Set<Resource> resources = createResources();
        
        Set<Privilege> privileges = createBasicPrivileges(resources);

        Role superAdminRole = createSuperAdminRole(privileges);

        createUserRole(privileges);
        
        createBibliotecarioRole(privileges);
        
        createSuperAdminUser(superAdminRole);

        Autor autor = createAutor();

        createPublicacion(List.of(autor));


        
    }

    private Set<Resource> createResources() {
        return ResourceUtils.getResourceNames().stream()
                .map( resourceName -> resourceService.saveResource(
                        Resource.builder()
                                .name(resourceName)
                                .build()
                )).collect(Collectors.toSet());
    }
    
    private Set<Privilege> createBasicPrivileges(Set<Resource> resources) {
        logger.info("creating basic privileges");

        return resources.stream()
                .flatMap(resource -> {
                    logger.info(resource.getName());
                    return Arrays.stream(Action.values())
                            .map(action -> Privilege.builder()
                                    .resource(resource)
                                    .name(String.format("%s_%s", action.name(), resource.getName()))
                                    .actions(Set.of(action))
                                    .build());
                })
                .peek(privilege -> {
                    logger.info(String.format("Create privilege: %s, %s",privilege.getName(), privilege));
                    privilegeService.savePrivilege(privilege);
                })
                .collect(Collectors.toSet());
        
    }
    private Role createSuperAdminRole(Set<Privilege> privileges) {
        //get admin privilege
        
        // crea default rol super admin user
        return roleService.create(
                Role.builder()
                        .startDate(Instant.now())
                        .endDate(null)
                        .name(RoleUtils.ROLE_SUPER_ADMIN)
                        .privileges(privileges)
                        .build()
        );


    }

    private Role createUserRole(Set<Privilege> privileges) {

        // obtiene privilegios de lectura para todas las entidades
        var privilegiosLectura = privileges.stream()
                .filter(privilege -> privilege.getName().contains(PrivilegeUtils.DEFAULT_PRIVILEGE)
        );



    return roleService.create(
        Role.builder()
            .startDate(Instant.now())
            .endDate(null)
            .name(RoleUtils.DEFAULT_ROLE_USER)
            .privileges(privilegiosLectura.collect(Collectors.toSet()))
            .build());
    }

    private Role createBibliotecarioRole(Set<Privilege> privileges) {

        var recursosAdministradasPorBibliotecario = Set.of(
                ResourceNames.PRESTAMO,
                ResourceNames.EDICION,
                ResourceNames.EJEMPLAR,
                ResourceNames.PLATAFORMA,
                ResourceNames.EDITORIAL);

        // obtiene privilegios de lectura para todas las entidades
        var privilegiosBibliotecario = recursosAdministradasPorBibliotecario.stream()
                .flatMap(
                        resourceName ->
                        privileges.stream().filter(privilege -> privilege.getResource().getName().equals(resourceName.name()))
                );



        return roleService.create(
                Role.builder()
                        .startDate(Instant.now())
                        .endDate(null)
                        .name(RoleUtils.BIBLIOTECARIO_ROLE)
                        .privileges(privilegiosBibliotecario.collect(Collectors.toSet()))
                        .build());
    }

    
    private void createSuperAdminUser(Role superAdminRole) {
        //crea
        JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.signup(
                new SignUpRequest(
                        "SUPERADMIN",
                        "SUPERADMIN",
                        "email@superadmin.com",
                        "password1234",
                        List.of(superAdminRole.getId())
                )
        );

    logger.info(
        String.format(
            "\ntoken: %s, \nrefresh_token: %s",
            jwtAuthenticationResponse.getToken(), jwtAuthenticationResponse.getRefreshToken()));
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

    private void createPublicacion(List<Autor> autores) {

        publicacionService.save(
                Publicacion.builder()
                        .fechaAlta(Instant.now())
                        .titulo("Cien años de soledad")
                        .autores(autores)
                        .isbn("9788497592208")
                        .anio(2003)
                        .build()
        );
    }

    private Autor createAutor() {

        return autorService.save(
                Autor.builder()
                        .fechaAlta(Instant.now())
                        .apellido("García Márquez")
                        .nombre("Gabriel")
                        .build()
        );
    }


}

