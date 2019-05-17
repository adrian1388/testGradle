package com.test.data.eclipselink;

import java.util.UUID;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Enables support for {@link UUID} columns in the JPA models.
 * 
 * <p>Without this class, EclipseLink will now know how to handle these field types.</p>
 * 
 * <p>The idea to do this came from: <a href="https://stackoverflow.com/questions/11284359/persisting-uuid-in-postgresql-using-jpa/17323965#17323965">https://stackoverflow.com/questions/11284359/persisting-uuid-in-postgresql-using-jpa/17323965#17323965</a></p>
 * 
 * <p>If you have an issue querying null UUID values (WHERE businesId = 'null' for example), this is due to how the PostgreSQL JDBC driver handles the parameter.
 * It can be fixed by modifying the connection string, adding "?stringtype=unspecified" to the end of it fixes the issue.
 * 
 * This solution from: <a href="https://stackoverflow.com/a/47693875/3669247">https://stackoverflow.com/a/47693875/3669247</a>
 * </p>
 * 
 * @author omar
 */
@Converter(autoApply = true)
public class UuidConverter implements AttributeConverter<UUID, UUID> {
    @Override
    public UUID convertToDatabaseColumn(UUID attribute) {
        return attribute;
    }

    @Override
    public UUID convertToEntityAttribute(UUID attribute) {
        return attribute;
    }
}