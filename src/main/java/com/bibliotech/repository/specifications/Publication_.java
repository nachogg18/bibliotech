package com.bibliotech.repository.specifications;

import com.bibliotech.entity.Publication;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;
import java.time.Instant;
import java.util.List;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Publication.class)
public class Publication_ {

    public static volatile SingularAttribute<Publication, String> id;
    public static volatile SingularAttribute<Publication, String> name;
    public static volatile SingularAttribute<Publication, Instant> startDate;
    public static volatile SingularAttribute<Publication, Instant> endDate;
    public static volatile SingularAttribute<Publication, Instant> publicationDate;
    public static volatile SingularAttribute<Publication, String> isbn;
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String PUBLICATION_DATE = "publicationDate";
    public static final String ISBN = "isbn";

    public static List<String> getFieldsFilter() {
        return List.of(NAME, START_DATE, END_DATE, PUBLICATION_DATE, ISBN);
    }
}
