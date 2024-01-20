package com.bibliotech.service;

import com.bibliotech.dto.*;
import com.bibliotech.entity.*;
import com.bibliotech.security.dao.request.SignUpWithoutRequiredConfirmationRequest;
import com.bibliotech.security.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
@Transactional
public class SeedServiceImpl implements SeedService{

    AutorService autorService;
    BibliotecaService bibliotecaService;
    CarreraService carreraService;
    CategoriaService categoriaService;
    CategoriaValorService valorService;
    EdicionService edicionService;
    EditorialService editorialService;
    FacultadService facultadService;
    PlataformaService plataformaService;
    TipoPublicacionService tipoPublicacionService;
    TipoMultaService tipoMultaService;
    PublicacionService publicacionService;
    UbicacionService ubicacionService;
    AuthenticationService authenticationService;
    EjemplarService ejemplarService;
    ParametroService parametroService;

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    @Autowired
    public SeedServiceImpl(
            ObjectMapper objectMapper,
            ResourceLoader resourceLoader,
            AutorService autorService,
            BibliotecaService bibliotecaService,
            CarreraService carreraService,
            CategoriaService categoriaService,
            CategoriaValorService valorService,
            EdicionService edicionService,
            EditorialService editorialService,
            FacultadService facultadService,
            PlataformaService plataformaService,
            TipoPublicacionService tipoPublicacionService,
            TipoMultaService tipoMultaService,
            PublicacionService publicacionService,
            UbicacionService ubicacionService,
            AuthenticationService authenticationService,
            EjemplarService ejemplarService,
            ParametroService parametroService
    ) {
        this.bibliotecaService = bibliotecaService;
        this.autorService = autorService;
        this.categoriaService = categoriaService;
        this.valorService = valorService;
        this.edicionService = edicionService;
        this.editorialService = editorialService;
        this.facultadService = facultadService;
        this.carreraService = carreraService;
        this.plataformaService = plataformaService;
        this.tipoMultaService = tipoMultaService;
        this.tipoPublicacionService = tipoPublicacionService;
        this.publicacionService = publicacionService;
        this.ubicacionService = ubicacionService;
        this.authenticationService = authenticationService;
        this.ejemplarService = ejemplarService;
        this.resourceLoader = resourceLoader;
        this.parametroService = parametroService;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    }


    public String cargarAutores(){
        try {

            Resource autoresJsonFile = resourceLoader.getResource("classpath:data/autores.json");
            Autor[] autores = objectMapper.readValue(autoresJsonFile.getInputStream(), Autor[].class);

            for (Autor autor : autores) {
                autorService.save(autor);
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar autores -> "+e.getMessage());
        }
        return "Autores cargados";
    }

    public String cargarBilbiotecas(){
        try {

            Resource bilbiotecasJsonFile = resourceLoader.getResource("classpath:data/bibliotecas.json");
            Biblioteca[] bibliotecas = objectMapper.readValue(bilbiotecasJsonFile.getInputStream(), Biblioteca[].class);

            for (Biblioteca biblioteca : bibliotecas) {
                bibliotecaService.saveBiblioteca(biblioteca);
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar las bibliotecas -> "+e.getMessage());
        }
        return "Bibliotecas cargadas";
    }

    public String cargarCarreras(){
        try {

            Resource carrerasJsonFile = resourceLoader.getResource("classpath:data/carreras.json");
            Carrera[] carreras = objectMapper.readValue(carrerasJsonFile.getInputStream(), Carrera[].class);

            for (Carrera carrera : carreras) {
                carreraService.save(carrera);
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar las carreras -> "+e.getMessage());
        }
        return "Carreras cargadas";
    }

    public String cargarCategorias(){
        try {

            Resource categoriasJsonFile = resourceLoader.getResource("classpath:data/categorias.json");
            CrearCategoriaDTO[] categorias = objectMapper.readValue(categoriasJsonFile.getInputStream(), CrearCategoriaDTO[].class);

            for (CrearCategoriaDTO categoria : categorias) {
                categoriaService.save(categoria);
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar las categorias -> "+e.getMessage());
        }
        return "Categorias cargadas";
    }

    public String cargarValores(){
        try {

            Resource valoresJsonFile = resourceLoader.getResource("classpath:data/valores.json");
            CrearValorDTO[] valores = objectMapper.readValue(valoresJsonFile.getInputStream(), CrearValorDTO[].class);

            for (CrearValorDTO valor : valores) {
                valorService.save(valor);
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar los valores -> "+e.getMessage());
        }
        return "Valores cargados";
    }

    public String cargarEdiciones(){
        try {

            Resource edicionesJsonFile = resourceLoader.getResource("classpath:data/ediciones.json");
            Edicion[] ediciones = objectMapper.readValue(edicionesJsonFile.getInputStream(), Edicion[].class);

            for (Edicion edicion : ediciones) {
                edicionService.save(edicion);
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar las ediciones -> "+e.getMessage());
        }
        return "Ediciones cargadas";
    }

    public String cargarEditoriales(){
        try {

            Resource editorialesJsonFile = resourceLoader.getResource("classpath:data/editoriales.json");
            Editorial[] editoriales = objectMapper.readValue(editorialesJsonFile.getInputStream(), Editorial[].class);

            for (Editorial editorial : editoriales) {
                editorialService.save(editorial);
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar las editoriales -> "+e.getMessage());
        }
        return "Editoriales cargadas";
    }

    public String cargarFacultades(){
        try {

            Resource facultadesJsonFile = resourceLoader.getResource("classpath:data/facultades.json");
            Facultad[] facultades = objectMapper.readValue(facultadesJsonFile.getInputStream(), Facultad[].class);

            for (Facultad facultad : facultades) {
                facultadService.save(facultad);
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar las facultades -> "+e.getMessage());
        }
        return "Facultades cargadas";
    }

    public String cargarPlataformas(){
        try {

            Resource plataformasJsonFile = resourceLoader.getResource("classpath:data/plataformas.json");
            Plataforma[] plataformas = objectMapper.readValue(plataformasJsonFile.getInputStream(), Plataforma[].class);

            for (Plataforma plataforma : plataformas) {
                plataformaService.save(plataforma);
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar las plataformas -> "+e.getMessage());
        }
        return "Plataformas cargadas";
    }

    public String cargarTiposPublicacion(){
        try {

            Resource tiposPublicacionJsonFile = resourceLoader.getResource("classpath:data/tipo_publicacion.json");
            TipoPublicacion[] tiposPublicacion = objectMapper.readValue(tiposPublicacionJsonFile.getInputStream(), TipoPublicacion[].class);
            for (TipoPublicacion tipo : tiposPublicacion) {
                tipoPublicacionService.save(tipo);
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar los tipos de publicacion -> "+e.getMessage());
        }
        return "Tipos publicaccion cargadas";
    }

    public String cargarTiposMulta(){
        try {

            Resource tiposMultaJsonFile = resourceLoader.getResource("classpath:data/tipo_multa.json");
            TipoMulta[] tiposMulta = objectMapper.readValue(tiposMultaJsonFile.getInputStream(), TipoMulta[].class);
            for (TipoMulta tipo : tiposMulta) {
                tipoMultaService.save(tipo);
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar los tipos de multa -> "+e.getMessage());
        }
        return "Tipos multa cargadas";
    }

    public String cargarPublicaciones(){
        try {
            Resource publicacionesJsonFile = resourceLoader.getResource("classpath:data/publicaciones.json");
            CreatePublicacionRequestDTO[] publicaciones = objectMapper.readValue(publicacionesJsonFile.getInputStream(), CreatePublicacionRequestDTO[].class);
            for (CreatePublicacionRequestDTO publicacion : publicaciones) {
                publicacionService.create(publicacion);
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar las publicaciones -> "+e.getMessage());
        }
        return "Publicaciones cargadas";
    }

    public String cargarEjemplares(){
        try {
            Resource ejemplaresJsonFile = resourceLoader.getResource("classpath:data/ejemplares.json");
            CrearEjemplarDTO[] ejemplares = objectMapper.readValue(ejemplaresJsonFile.getInputStream(), CrearEjemplarDTO[].class);
            for (CrearEjemplarDTO ejemplar : ejemplares) {
                ejemplarService.createEjemplar(ejemplar);
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar los ejemplares -> "+e.getMessage());
        }
        return "Ejemplares cargadas";
    }

    public String cargarUbicaciones(){
        try {
            Resource ubicacionesJsonFile = resourceLoader.getResource("classpath:data/ubicaciones.json");
            UbicacionDTO[] ubicaciones = objectMapper.readValue(ubicacionesJsonFile.getInputStream(), UbicacionDTO[].class);
            for (UbicacionDTO ubicacion : ubicaciones) {
                ubicacionService.save(ubicacion);
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar las ubicaciones -> "+e.getMessage());
        }
        return "Ubicaciones cargadas";
    }

    public String cargarUsuarios(){
        try {
            Resource usuariosJsonFile = resourceLoader.getResource("classpath:data/users.json");
            SignUpWithoutRequiredConfirmationRequest[] usuarios = objectMapper.readValue(usuariosJsonFile.getInputStream(), SignUpWithoutRequiredConfirmationRequest[].class);
            for (SignUpWithoutRequiredConfirmationRequest usuario : usuarios) {
                authenticationService.signupWithoutRequiredConfirmation(usuario);
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar los usuarios -> "+e.getMessage());
        }
        return "Usuarios cargados";
    }
    public String cargarParametros(){
        try {
            Resource parametrosJsonFile = resourceLoader.getResource("classpath:data/parametros.json");
            Parametro[] parametros = objectMapper.readValue(parametrosJsonFile.getInputStream(), Parametro[].class);
            for (Parametro parametro : parametros) {
                parametroService.guardarParametro(parametro);
            }
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar los parámetros -> "+e.getMessage());
        }
        return "Parámetros cargados";
    }

    public String cargaMasiva(){
        try {
            this.cargarAutores();
            this.cargarBilbiotecas();
            this.cargarUbicaciones();
            this.cargarCategorias();
            this.cargarValores();
            this.cargarEdiciones();
            this.cargarEditoriales();
            this.cargarFacultades();
            this.cargarCarreras();
            this.cargarPlataformas();
            this.cargarTiposMulta();
            this.cargarTiposPublicacion();
            this.cargarPublicaciones();
            this.cargarEjemplares();
            this.cargarUsuarios();
            this.cargarParametros();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al realizar la carga masiva -> "+e.getMessage());
        }
        return "Carga masiva realizada";
    }
}

