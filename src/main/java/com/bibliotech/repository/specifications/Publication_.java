package com.bibliotech.repository.specifications;

import com.bibliotech.entity.Publication;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Publication.class)
public class Publication_ {

    public static volatile SingularAttribute<Publication, String> id;
    public static volatile SingularAttribute<Publication, String> name;
    public static final String NAME = "name";
    public static final String ID = "id";
}
