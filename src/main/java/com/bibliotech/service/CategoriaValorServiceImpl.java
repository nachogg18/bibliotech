package com.bibliotech.service;

import com.bibliotech.dto.CrearValorDTO;
import com.bibliotech.dto.MostrarCategoriaValorDTO;
import com.bibliotech.entity.Categoria;
import com.bibliotech.entity.CategoriaValor;
import com.bibliotech.repository.CategoriaValorRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class CategoriaValorServiceImpl implements CategoriaValorService {

    private final CategoriaValorRepository categoriaValorRepository;
    private final CategoriaService categoriaService;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoriaValor> findAll() {
        return categoriaValorRepository.findByFechaBajaNull();
    }

    @Override
    public Optional<CategoriaValor> findById(Long id) {
        return categoriaValorRepository.findById(id);
    }

    @Override
    public MostrarCategoriaValorDTO save(CrearValorDTO valorDTO) {
        Categoria categoria = categoriaService.findOne(valorDTO.getIdCategoria())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));
        CategoriaValor valor = new CategoriaValor();
        valor.setNombre(valorDTO.getNombre());
        valor.setCategoria(categoria);
        valor = categoriaValorRepository.save(valor);
        MostrarCategoriaValorDTO retorna = modelMapper.map(valor, MostrarCategoriaValorDTO.class);
        log.info(valorDTO.toString());
        log.info(categoria.toString());
        log.info(valor.toString());
        log.info(retorna.toString());
        return retorna;
    }

    @Override
    public MostrarCategoriaValorDTO edit(CrearValorDTO valorDTO, Long id) {
        if (categoriaValorRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        CategoriaValor valor = modelMapper.map(valorDTO, CategoriaValor.class);
        valor.setId(id);
        valor = categoriaValorRepository.save(valor);
        return modelMapper.map(valor, MostrarCategoriaValorDTO.class);
    }

    @Override
    public Optional<MostrarCategoriaValorDTO> delete(Long id) {
        Optional<CategoriaValor> categoriaValorOptional = categoriaValorRepository.findById(id);
        Optional<MostrarCategoriaValorDTO> valorDTOOptional = Optional.empty();
        if (categoriaValorOptional.isPresent()) {
            CategoriaValor categoriaValor = categoriaValorOptional.get();
            if (categoriaValor.getFechaBaja() == null) {
                categoriaValor.setFechaBaja(Instant.now());
                categoriaValor = categoriaValorRepository.save(categoriaValor);
                valorDTOOptional = Optional.of(modelMapper.map(categoriaValor, MostrarCategoriaValorDTO.class));
            }
        }
        return valorDTOOptional;
    }
}
