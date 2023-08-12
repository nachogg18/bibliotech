package com.bibliotech.controller;


import com.bibliotech.entity.Publication;
import com.bibliotech.repository.PublicationRepository;
import com.bibliotech.repository.specifications.Filter;
import com.bibliotech.repository.specifications.Publication_;
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
    public ResponseEntity<Object> findPublicationWithName(@RequestBody List<Filter> filterList) throws Exception {
        
        logger.info("request {}", Objects.toString(filterList, ""));
        try {
            validateFieldsFromFilterList(filterList);
            
            return ResponseEntity.of(Optional.of(getQueryResult(filterList)));
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

  private List<Publication> getQueryResult(List<Filter> filterList) throws Exception {
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

    private void validateFieldsFromFilterList(List<Filter> filterList) throws ValidationException {

       List<Map<String, Optional<Map<String, String>>>> filterResultMap = filterList.stream()
                .map(filterToValidate ->
                        Map.of(filterToValidate.getField(),
                                Publication_
                                        .getFieldNameAndTypeByFieldName(filterToValidate.getField()))).toList();
       

       if (filterResultMap.size() != filterList.size() ) {
           throw new ValidationException("some_of_criteria_filters_are_invalid");
       }
       
       /*
       TODO: comprobar si cada campo coincide con el tipo del metamodelo
        */
       
    }


}

