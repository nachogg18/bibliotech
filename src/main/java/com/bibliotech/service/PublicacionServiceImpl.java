package com.bibliotech.service;

import com.bibliotech.dto.*;
import com.bibliotech.entity.*;
import com.bibliotech.mapper.ListToPageDTOMapper;
import com.bibliotech.mapper.PublicacionRequestMapper;
import com.bibliotech.repository.*;
import com.bibliotech.repository.specifications.PublicacionSpecifications;
import com.bibliotech.utils.PageUtil;
import jakarta.validation.ValidationException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Log4j2
public class PublicacionServiceImpl implements PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final TipoPublicacionRepository tipoPublicacionRepository;
    private final LinkRepository linkRepository;
    private final EditorialRepository editorialRepository;
    private final EdicionRepository edicionRepository;
    private final CategoriaPublicacionRepository categoriaPublicacionRepository;
    private final CategoriaService categoriaService;
    private final CategoriaValorService categoriaValorService;
    private final AutorRepository autorRepository;
    private final EditorialService editorialService;
    private final TipoPublicacionService tipoPublicacionService;
    private final PlataformaService plataformaService;
    private final EdicionService edicionService;
    private final PageUtil pageUtil;
    private final ListToPageDTOMapper<Publicacion, PublicacionPaginadaDTO> listToPageDTOMapper;

    @Override
    public List<PublicacionResponseDTO> findAll() {
        //return publicacionRepository.findAll();
        List<Publicacion> publicaciones = publicacionRepository.findAll();

//        List<String> nombresEditoriales = publicaciones.stream().flatMap(publicacion -> publicacion.getEditoriales().stream()
//                        .map(Editorial::getNombre))
//                .collect(Collectors.toList());

        return publicaciones.stream().map(publicacion -> {
           PublicacionResponseDTO res = PublicacionResponseDTO
                   .builder()
                   .anioPublicacion(publicacion.getAnio())
                   .tituloPublicacion(publicacion.getTitulo())
                   .nombreEdicion(publicacion.getEdicion().getNombre())
                   .id(publicacion.getId())
                   .nombreAutores(publicaciones.stream().flatMap(p -> p.getAutores().stream()
                                   .map(Autor::getNombre))
                           .collect(Collectors.toList()))
                   .nombreEditorial(publicacion.getEditoriales().get(0).getNombre())
                   .build();
           return res;
            })
                .collect(Collectors.toList());
    }

    public Optional<Publicacion> findById(Long id) {
        return publicacionRepository.findById(id);
    }

    ;

    @Override
    public List<PublicacionResponseDTO> findAllPublicacionDTO(String parametro, String contenido, List<BusquedaPublicacionCategoriaDTO> busquedaPublicacionList) {

        List<Long> valoresIdBusqueda = new ArrayList<>();
        busquedaPublicacionList.forEach(bpl ->
                bpl.getValores().forEach(v -> {
                    if (v.isSeleccionadoValor())
                        valoresIdBusqueda.add(v.getIdValor());
                }));

        List<Long> categoriaPublicacionIdList = categoriaPublicacionRepository.findByCategoriaValorListIdIn(valoresIdBusqueda).stream().map(Base::getId).toList();

        List<Publicacion> publicaciones = new ArrayList<>();
        if (contenido == null) {
            publicaciones = publicacionRepository.findByCategoriaPublicacionListIdIn(categoriaPublicacionIdList);
        } else {
            if (Objects.equals(parametro, "Autor")) {
                List<Autor> autorList = autorRepository.findByApellidoContainingIgnoreCaseOrNombreContainingIgnoreCase(contenido, contenido);
                publicaciones = publicacionRepository.findByCategoriaPublicacionListIdInAndAutoresIdIn(categoriaPublicacionIdList, autorList.stream().map(Base::getId).toList());
            } else if (Objects.equals(parametro, "Titulo")) {
                publicaciones = publicacionRepository.findByCategoriaPublicacionListIdInAndTituloContainingIgnoreCase(categoriaPublicacionIdList, contenido);
            } else if (Objects.equals(parametro, "Editorial")) {
                List<Editorial> editorialList = editorialRepository.findByNombreContainingIgnoreCase(contenido);
                publicaciones = publicacionRepository.findByCategoriaPublicacionListIdInAndAutoresIdIn(categoriaPublicacionIdList, editorialList.stream().map(Base::getId).toList());
            }
        }

        return PublicacionRequestMapper.toResponseDTO(publicaciones);
    }

    @Override
    public DetallePublicacionDTO getDetallePublicacion(Long id) {
        DetallePublicacionDTO detallePublicacionDTO = new DetallePublicacionDTO();

        Publicacion publicacion = publicacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));

        detallePublicacionDTO.setId(publicacion.getId());
        detallePublicacionDTO.setIsbnPublicacion(publicacion.getIsbn());
        detallePublicacionDTO.setTituloPublicacion(publicacion.getTitulo());
        detallePublicacionDTO.setNroPaginas(publicacion.getNroPaginas());
        detallePublicacionDTO.setAnioPublicacion(publicacion.getAnio());
        detallePublicacionDTO.setLink(Objects.nonNull(publicacion.getLink()) ? publicacion.getLink() : null);
        detallePublicacionDTO.setEditoriales(Objects.nonNull(publicacion.getEditoriales()) ? publicacion.getEditoriales() : null);

        detallePublicacionDTO.setPlataforma(Objects.nonNull(publicacion.getLink().getPlataforma()) ? publicacion.getLink().getPlataforma() : null);

        List<DetalleCategoriaDTO> detalleCategoriaDTOList = new ArrayList<>();

        detallePublicacionDTO.setEdicion(publicacion.getEdicion());

        if(Objects.nonNull(publicacion.getAutores())) {
            List<AutorDTO> autores = publicacion.getAutores().stream().map(autor -> {
                AutorDTO nuevoAutor = new AutorDTO();
                nuevoAutor.setNombre(autor.getNombre() + autor.getApellido());
                nuevoAutor.setBiografia(autor.getBiografia());
                nuevoAutor.setId(autor.getId());
                nuevoAutor.setFechaNacimiento(autor.getFechaNacimiento());
                nuevoAutor.setNacionalidad(autor.getNacionalidad());
                return nuevoAutor;
            }).collect(Collectors.toList());

            detallePublicacionDTO.setAutores(autores);
        }

        if(Objects.nonNull(publicacion.getCategoriaPublicacionList())) {
            publicacion.getCategoriaPublicacionList().forEach(cp -> {
                DetalleCategoriaDTO detalleCategoriaDTO = new DetalleCategoriaDTO();
                detalleCategoriaDTO.setCategoria(new CategoriaDetalleDTO(cp.getCategoria().getId(),cp.getCategoria().getNombre()));
                detalleCategoriaDTO.setValores(cp.getCategoriaValorList()/*.stream().map(CategoriaValor::getNombre).toList()*/);
                detalleCategoriaDTOList.add(detalleCategoriaDTO);
            });
        }
        detallePublicacionDTO.setCategorias(detalleCategoriaDTOList);

        return detallePublicacionDTO;
    }

    @Override
    public PageDTO<PublicacionPaginadaDTO> findAllPublicacionPaginatedDTO(int page) {
        if (page <= 0)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "page not found");

        Page<Publicacion> categoryPage = publicacionRepository.findAllByFechaBajaIsNull(pageUtil.pageRequest(page));

        if (categoryPage.getTotalPages() != 0 && page > categoryPage.getTotalPages())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "page not found");

        List<PublicacionPaginadaDTO> publicacionesDTOList = categoryPage.getContent().stream()
                .map(publicacion -> {
                            PublicacionPaginadaDTO publicacionDTO = new PublicacionPaginadaDTO();
                            publicacionDTO.setTitulo(publicacion.getTitulo());
                            publicacionDTO.setAnio(publicacion.getAnio());
                            publicacionDTO.setEdicion(publicacion.getEdicion().getNombre());
                            publicacionDTO.setEditoriales(publicacion.getEditoriales().stream().map(Editorial::getNombre).toList());
                            publicacionDTO.setAutores(publicacion.getAutores().stream().map(a -> a.getApellido().toUpperCase() + ", " + a.getNombre()).toList());
                            return publicacionDTO;
                        }
                )
                .collect(Collectors.toList());

        return listToPageDTOMapper.toPageDTO(page, categoryPage, publicacionesDTOList);
    }

    @Override
    public PublicacionResponseDTO create(CreatePublicacionRequestDTO request) {

        Publicacion publicacion = publicacionRepository.save(
                Publicacion.builder()
                        .anio(request.getAnioPublicacion())
                        .isbn(request.getIsbnPublicacion())
                        .titulo(request.getTituloPublicacion())
                        .nroPaginas(request.getNroPaginas())
                        .autores(
                                request.getIdsAutores().stream()
                                        .map(
                                                idAutor ->
                                                        autorRepository
                                                                .findById(idAutor)
                                                                .orElseThrow(
                                                                        () ->
                                                                                new ValidationException(
                                                                                        String.format("no existe autor con id: %s", idAutor))))
                                        .toList())

                        .edicion(
                                edicionService.findById(request.getIdEdicion()).orElseThrow(
                                        () ->
                                                new ValidationException(
                                                        String.format("no existe edicion con id: %s", request.getIdEdicion())))
                        )
                        .link(
                                Link.builder()
                                        .url(request.getLink().getUrl())
                                        .plataforma(
                                                plataformaService.findById(request.getLink().getPlataformaId())
                                                        .orElseThrow(() -> new ValidationException(String.format("no existe plataforma con id: %s", request.getLink().getPlataformaId())))
                                        )
                                        .estadoLink(EstadoLink.valueOf(request.getLink().getEstado()))
                                        .build()
                        )
                        .categoriaPublicacionList(
                                request.getCategorias().stream().map(
                                        dto -> CategoriaPublicacion
                                                .builder()
                                                .categoria(
                                                        categoriaService.findOne(dto.idCategoria())
                                                                .orElseThrow(() -> new ValidationException(String.format("no existe categoria con id: %s", dto.idCategoria())))
                                                )
                                                .categoriaValorList(
                                                        dto.idValores().stream().map(
                                                                v -> categoriaValorService.findById(v)
                                                                        .orElseThrow(() -> new ValidationException(String.format("no existe valor con id: %s", v)))
                                                        ).toList()
                                                )
                                                .build()
                                ).toList()
                        )
                        .tipoPublicacion(
                                tipoPublicacionService.findByIdAndFechaBajaNull(request.getIdTipoPublicacion())
                                        .orElseThrow(() -> new ValidationException(String.format("no existe tipo publicacion con id: %s", request.getIdTipoPublicacion())))

                        )
                        .editoriales(
                                request.getIdsEditoriales().stream().map(
                                        id -> editorialService.findById(id)
                                                .orElseThrow(() -> new ValidationException(String.format("no existe editorial con id: %s", id)))
                                ).toList()
                        )
                        .fechaAlta(Instant.now())
                        .fechaBaja(null)
                        .build());

        PublicacionResponseDTO responseDTO = new PublicacionResponseDTO();
        responseDTO.setTituloPublicacion(publicacion.getTitulo());
        responseDTO.setAnioPublicacion(publicacion.getAnio());
        responseDTO.setNombreEdicion(publicacion.getEdicion().getNombre());
        responseDTO.setNombreEditorial(publicacion.getEditoriales().get(0).getNombre());
        responseDTO.setId(publicacion.getId());
        responseDTO.setNombreAutores(
                publicacion.getAutores().stream().map(a -> a.getApellido() + ", " + a.getNombre()).toList()
        );
        return responseDTO;
    }


    public List<Publicacion> findByParams(FindPublicacionesByParamsDTO request) {
        Specification<Publicacion> spec = PublicacionSpecifications.empty();

        String titulo = request.titulo();
        if (StringUtils.isNotBlank(titulo)) {
            spec = spec.and(PublicacionSpecifications.hasTituloLike(titulo));
        }

        String isbn = request.isbn();
        if (StringUtils.isNotBlank(isbn)) {
            spec = spec.and(PublicacionSpecifications.hasIsbn(isbn));
        }

        String anio = request.anio();
        if (StringUtils.isNotBlank(anio)) {
            spec = spec.and(PublicacionSpecifications.hasAnio(anio));
        }

        String autor = request.autor();
        if (StringUtils.isNotBlank(autor)) {
            spec = spec.and(PublicacionSpecifications.hasAutorWithFirstOrLastName(autor));
        }

        return publicacionRepository.findAll(spec);
    }

    @Override
    public Publicacion save(Publicacion publicacion) {
        return publicacionRepository.save(publicacion);
    }

    @Override
    public ModificarPublicacionResponse updatePublicacion(ModificarPublicacionDTO req, Long id) {
        Publicacion publicacionExistente = publicacionRepository.getById(id);
        Publicacion nuevaPublicacion = mapDtoToEntity(req, publicacionExistente);

        publicacionRepository.save(nuevaPublicacion);

        ModificarPublicacionResponse res = ModificarPublicacionResponse
                .builder()
                .id(nuevaPublicacion.getId())
                .isbn(nuevaPublicacion.getIsbn())
                .titulo(nuevaPublicacion.getTitulo())
                .build();
        return res;
    }

    private Publicacion mapDtoToEntity(ModificarPublicacionDTO req, Publicacion publicacion) {
        if (req.getAnio() != null) publicacion.setAnio(req.getAnio());
        if (req.getIsbn() != null) publicacion.setIsbn(req.getIsbn());
        if (req.getTitulo() != null) publicacion.setTitulo(req.getTitulo());
        if (req.getNroPaginas() != null) publicacion.setNroPaginas(req.getNroPaginas());
        if (req.getIsbn() != null) publicacion.setIsbn(req.getIsbn());

        if(req.getIdsAutores() != null) {
            publicacion.setAutores(autorRepository.findByIdIn(req.getIdsAutores().toArray(new Long[0])));
        }

        if(req.getIdsEditoriales() != null) {
            publicacion.setEditoriales(editorialRepository.findByIdIn(req.getIdsEditoriales().toArray(new Long[0])));
        }

        if(req.getIdsCategorias() != null) {
            publicacion.setCategoriaPublicacionList(categoriaPublicacionRepository.findByIdIn(req.getIdsCategorias().toArray(new Long[0])));
        }

        if(req.getLink()!=null){
            Link linkNuevo = Link
                    .builder()
                    .url(req.getLink().url())
                    .plataforma(
                    plataformaService.findById(req.getLink().plataformaId())
                            .orElseThrow(() -> new ValidationException(String.format("no existe plataforma con id: %s", req.getLink().plataformaId())))
                    )
                    .estadoLink(EstadoLink.valueOf(req.getLink().estado()))
                    .build();
            publicacion.setLink(linkNuevo);
        }

        if(req.getIdEdicion()!=null) publicacion.setEdicion(edicionRepository.findById(req.getIdEdicion()).get());
        if(req.getIdTipoPublicacion()!=null) publicacion.setTipoPublicacion(tipoPublicacionRepository.findById(req.getIdTipoPublicacion()).get());

        return publicacion;
    }

    @Override
    public List<ComentarioDTO> getAllComentarios(Long id){
        List<Comentario> comentarios = publicacionRepository.findComentariosByPublicacionId(id);
        List<ComentarioDTO> comentarioDTOS = comentarios.stream().map( comentario -> {
            ComentarioDTO dto = new ComentarioDTO();
            dto.setId(comentario.getId());
            dto.setComentario(comentario.getComentario());

            Instant fecha = comentario.getFechaAlta();
            ZoneId zonaArgentina = ZoneId.of("America/Argentina/Buenos_Aires");
            // Crear un formateador de fecha y hora
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            // Formatear el Instant en la zona horaria de Argentina
            String formattedDateTime = fecha.atZone(zonaArgentina).format(formatter);

            dto.setFecha(formattedDateTime);
            dto.setCalificacion(comentario.getCalificacion());
            dto.setAltaUsuario(comentario.getAltaUsuario().getFirstName() + ' ' + comentario.getAltaUsuario().getLastName());
            return dto;
        }).toList();

        return comentarioDTOS;
    };

}
