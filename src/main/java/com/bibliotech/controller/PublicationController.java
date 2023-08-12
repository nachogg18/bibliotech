package com.bibliotech.controller;


import com.bibliotech.dto.PublicationSearchRequestDTO;
import com.bibliotech.entity.Publication;
import com.bibliotech.mapper.SearchRequestMapper;
import com.bibliotech.repository.PublicationRepository;
import com.bibliotech.repository.specifications.EspecificationFilter;
import com.bibliotech.service.PublicationServiceImpl;
import jakarta.validation.ValidationException;
import java.util.*;
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
    public ResponseEntity<Object> findPublicationWithName(@RequestBody PublicationSearchRequestDTO request)  {
        
        try {
            logger.info("request {}", Objects.toString(request, "empty request"));
            
            request.validateRequest();
            
            var especificationFilterList = SearchRequestMapper.toEspecificationFilter(request);

            logger.info("especificationFilterList: {}", Objects.toString(especificationFilterList, ""));
            
            validateFieldsFromFilterList(especificationFilterList);

            logger.info("especificationFilterList validated: {}", Objects.toString(especificationFilterList, ""));
            
            return ResponseEntity.of(Optional.of(getQueryResult(especificationFilterList)));
        } 
        catch (ValidationException exception) {
            return ResponseEntity.badRequest().body("error en los parametros de busqueda");
        }
        catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.badRequest().body("error en los parametros de busqueda");
        } catch (Exception exception) {
            logger.info("Exception:", exception);
            return ResponseEntity.internalServerError().body("Hubo un problema al realizar la operacion ");
        }
        

    }

    private List<Publication> getQueryResult(List<EspecificationFilter> especificationFilterList) throws Exception {
        logger.info("filters: ", Objects.toString(especificationFilterList, ""));

        try {
            return this.service.findByParams(especificationFilterList);
        } catch ( InvalidDataAccessApiUsageException invalidDataAccessApiUsageException) {
            logger.info("exception:", invalidDataAccessApiUsageException.getMessage());
            throw invalidDataAccessApiUsageException;
        } catch (Exception exception) {
            logger.info("exception:", exception.toString());
            throw exception;
        }
    }

    private void validateFieldsFromFilterList(List<EspecificationFilter> especificationFilterList) throws ValidationException {
       if (especificationFilterList.size() == 0) {
           String msg = "request_is";
           logger.info(msg);
           throw new ValidationException(msg);
       }
       
       /*
       TODO: comprobar si cada campo coincide con el tipo del metamodelo
        */
       
    }


}

