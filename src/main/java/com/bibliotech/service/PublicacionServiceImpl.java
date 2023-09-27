package com.bibliotech.service;

import com.bibliotech.dto.*;
import com.bibliotech.entity.*;
import com.bibliotech.mapper.ListToPageDTOMapper;
import com.bibliotech.mapper.PublicacionRequestMapper;
import com.bibliotech.repository.*;
import com.bibliotech.utils.PageUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Log4j2
public class PublicacionServiceImpl implements PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final CategoriaPublicacionRepository categoriaPublicacionRepository;
    private final AutorRepository autorRepository;
    private final EditorialRepository editorialRepository;
    private final PageUtil pageUtil;
    private final ListToPageDTOMapper<Publicacion, PublicacionPaginadaDTO> listToPageDTOMapper;

    public PublicacionServiceImpl(PublicacionRepository publicacionRepository, CategoriaPublicacionRepository categoriaPublicacionRepository, AutorRepository autorRepository, EditorialRepository editorialRepository, PageUtil pageUtil, ListToPageDTOMapper<Publicacion, PublicacionPaginadaDTO> listToPageDTOMapper) {
        this.publicacionRepository = publicacionRepository;
        this.categoriaPublicacionRepository = categoriaPublicacionRepository;
        this.autorRepository = autorRepository;
        this.editorialRepository = editorialRepository;
        this.pageUtil = pageUtil;
        this.listToPageDTOMapper = listToPageDTOMapper;
    }

    @Override
    public List<Publicacion> findAll() {
        return publicacionRepository.findAll();
    }

    public Optional<Publicacion> findById(Long id) {
        return publicacionRepository.findById(id);
    };

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

        detallePublicacionDTO.setAutores(publicacion.getAutores().stream().map(autor -> autor.getApellido().toUpperCase() + ", " + autor.getNombre()).toList());
        detallePublicacionDTO.setEdicion(publicacion.getEdicion().getNombre());
        detallePublicacionDTO.setTitulo(publicacion.getTitulo());
        detallePublicacionDTO.setEditoriales(publicacion.getEditoriales().stream().map(Editorial::getNombre).toList());

        List<DetalleCategoriaDTO> detalleCategoriaDTOList = new ArrayList<>();

        publicacion.getCategoriaPublicacionList().forEach(cp -> {
            DetalleCategoriaDTO detalleCategoriaDTO = new DetalleCategoriaDTO();
            detalleCategoriaDTO.setNombre(cp.getCategoria().getNombre());
            detalleCategoriaDTO.setValores(cp.getCategoriaValorList().stream().map(CategoriaValor::getNombre).toList());
            detalleCategoriaDTOList.add(detalleCategoriaDTO);
        });

        detallePublicacionDTO.setCategorias(detalleCategoriaDTOList);

        List<DetalleEjemplarDTO> detalleEjemplarDTOList = new ArrayList<>();

        publicacion.getEjemplares().forEach(e -> {
            DetalleEjemplarDTO detalleEjemplarDTO = new DetalleEjemplarDTO();
            detalleEjemplarDTO.setId(e.getId());
            detalleEjemplarDTO.setValoracion(e.getComentarios().stream().mapToDouble(Comentario::getCalificacion).sum() / (long) e.getComentarios().size());
            detalleEjemplarDTO.setDisponibilidad(e.getEjemplarEstadoList().stream().filter(ee -> ee.getFechaFin() == null)
                    .map(ee -> Objects.equals(ee.getEstadoEjemplar().toString(), "DISPONIBLE")).toList().get(0));
            detalleEjemplarDTOList.add(detalleEjemplarDTO);
        });
        detallePublicacionDTO.setEjemplares(detalleEjemplarDTOList);

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



}
