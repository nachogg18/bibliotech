package com.bibliotech.initializer;

import com.bibliotech.entity.*;
import com.bibliotech.security.dao.request.SignUpRequest;
import com.bibliotech.security.dao.request.SignUpWithoutRequiredConfirmationRequest;
import com.bibliotech.security.entity.*;
import com.bibliotech.security.service.AuthenticationService;
import com.bibliotech.security.service.PrivilegeService;
import com.bibliotech.security.service.ResourceService;
import com.bibliotech.security.service.RoleService;
import com.bibliotech.service.*;
import com.bibliotech.utils.PrivilegeUtils;
import com.bibliotech.utils.ResourceNames;
import com.bibliotech.utils.ResourceUtils;
import com.bibliotech.utils.RoleUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Profile({"local","dockerlocal"})
@RequiredArgsConstructor
public class LocalDataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(LocalDataInitializer.class);
    
    private final Environment env;
    
    private final PrivilegeService privilegeService;
    
    private final RoleService roleService;
    
    private final PlataformaService plataformaService;
    
    private final LinkService linkService;
    
    private final AuthenticationService authenticationService;
    
    private final ResourceService resourceService;
    
    private final PublicacionService publicacionService;
    
    private final AutorService autorService;

    private final PrestamoService prestamoService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<Link> enabledLinks = List.of(createLink());

        createPlataformas(enabledLinks);

        Set<Resource> resources = createResources();
        
        Set<Privilege> privileges = createBasicPrivileges(resources);

        Role superAdminRole = createSuperAdminRole(privileges);

        Role userBasicRole = createUserRole(privileges);
        
        createBibliotecarioRole(privileges);
        
        createSuperAdminUser(superAdminRole);

        User userTest = createTestBasicUser(userBasicRole);

        Autor autor = createAutor();

        createPublicacion(List.of(autor));

        createTestPrestamo(userTest);


        
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
    // crea
    authenticationService.signupWithoutRequiredConfirmation(
        new SignUpWithoutRequiredConfirmationRequest(
            new SignUpRequest(
                    env.getRequiredProperty("superadmin.firstname"),
                    env.getRequiredProperty("superadmin.lastname"),
                    env.getRequiredProperty("superadmin.email"),
                    env.getRequiredProperty("superadmin.password")
            ),
            List.of(superAdminRole.getId())));
    }

    private User createTestBasicUser(Role basicRole) {
        // crea
        return authenticationService.signupWithoutRequiredConfirmation(
                new SignUpWithoutRequiredConfirmationRequest(
                        new SignUpRequest(
                                "usertest",
                                "usertest",
                                "usertest@email.com",
                                "user1234"
                        ),
                        List.of(basicRole.getId())));
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

    private void createTestPrestamo(User user) throws Exception {

        prestamoService.save(
                Prestamo.builder()
                        .fechaAlta(Instant.now())
                        .fechaFinEstimada(Instant.now().plus(Duration.ofDays(2)))
                        .fechaInicioEstimada(Instant.now().plus(Duration.ofDays(1)))
                        .multa(null)
                        .usuario(user)
                        .estado(
                                List.of(PrestamoEstado.builder()
                                        .estado(EstadoPrestamo.EN_ESPERA)
                                        .fechaFin(null)
                                        .build())
                        )
                        .build()
        );
    }


}

