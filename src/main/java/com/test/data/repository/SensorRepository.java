package com.test.data.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//import com.digivisor.data.live.model.Controller;
import com.test.data.model.Sensor;

//import io.leangen.graphql.annotations.GraphQLQuery;

/**
 * Service that allows CRUD operations on the {@link Sensor} model.
 * 
 * <p>Here we also centralize all of the queries that can be performed on the model.</p>
 * 
 * @author hmosquera
 */
@Repository
public interface SensorRepository extends JpaRepository<Sensor, UUID> {

    @Query(
        " SELECT s "
        + " FROM Sensor s "
        + "WHERE s.identifier = :identifier "
    )
    public Optional<Sensor> findByIdentifier(
        @Param("identifier")
        String identifier
    );

}
