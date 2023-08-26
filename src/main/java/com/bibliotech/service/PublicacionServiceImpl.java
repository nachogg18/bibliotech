package com.bibliotech.service;

import com.bibliotech.dto.*;
import com.bibliotech.entity.*;
import com.bibliotech.mapper.PublicacionRequestMapper;
import com.bibliotech.repository.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
public class PublicacionServiceImpl implements PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final CategoriaPublicacionRepository categoriaPublicacionRepository;
    private final AutorRepository autorRepository;
    private final EditorialRepository editorialRepository;

    public PublicacionServiceImpl(PublicacionRepository publicacionRepository, CategoriaPublicacionRepository categoriaPublicacionRepository, AutorRepository autorRepository, EditorialRepository editorialRepository) {
        this.publicacionRepository = publicacionRepository;
        this.categoriaPublicacionRepository = categoriaPublicacionRepository;
        this.autorRepository = autorRepository;
        this.editorialRepository = editorialRepository;
    }

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

        Optional<Publicacion> optPublicacion = publicacionRepository.findById(id);

        if(optPublicacion.isEmpty())
            return null; // TODO: devolver error

        Publicacion publicacion = optPublicacion.get();

        detallePublicacionDTO.setAutores(publicacion.getAutores().stream().map(autor -> autor.getApellido().toUpperCase() + ", " + autor.getNombre()).toList());
        detallePublicacionDTO.setDescripcion(publicacion.getDescripcion());
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
            detalleEjemplarDTO.setValoracion(e.getValoracionPromedio());
            detalleEjemplarDTO.setDisponibilidad(e.getEjemplarEstadoList().stream().filter(ee -> ee.getFechaFin() == null)
                    .map(ee -> Objects.equals(ee.getEstadoEjemplar().toString(), "DISPONIBLE")).toList().get(0));
            detalleEjemplarDTOList.add(detalleEjemplarDTO);
        });
        detallePublicacionDTO.setEjemplares(detalleEjemplarDTOList);

        return detallePublicacionDTO;
    }

}
