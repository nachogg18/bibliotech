package com.bibliotech.controller;


import com.bibliotech.entity.Publication;
import com.bibliotech.repository.PublicationRepository;
import com.bibliotech.repository.specifications.Filter;
import com.bibliotech.service.PublicationServiceImpl;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/publications")
public class PublicationController extends BaseControllerImpl<Publication, PublicationServiceImpl> {
    
    @Autowired
    public PublicationRepository publicationRepository;

    private static final Logger logger = LoggerFactory.getLogger(PublicationController.class);

    @GetMapping
    @RequestMapping(path = "/searchByParams")
    public ResponseEntity<Object> findPublicationWithName(@RequestBody(required = false) List<Filter> filterList) throws Exception {
        
        logger.info("request {}", Objects.toString(filterList, ""));
        
        try {
            return ResponseEntity.of(Optional.of(getQueryResult(filterList)));
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.badRequest().body("error en los parametros de busqueda");
        } catch (Exception exception) {
            logger.info("Exception:", exception);
            return ResponseEntity.internalServerError().body("Hubo un problema al realizar la operacion ");
        }
        

    }

  public List<Publication> getQueryResult(List<Filter> filterList) throws Exception {
        logger.info("filters: ", Objects.toString(filterList, ""));
        
        if(!Objects.isNull(filterList) && filterList.size()>0) {
            try {
                return this.service.findByParams(filterList);
            } catch ( InvalidDataAccessApiUsageException invalidDataAccessApiUsageException) {
                logger.info("exception:", invalidDataAccessApiUsageException.getMessage());
                throw invalidDataAccessApiUsageException;
            } catch (Exception exception) {
                logger.info("exception:", exception.toString());
                throw exception;
            }
            
        } else {
            return this.service.findAll();
        }
    }
}

