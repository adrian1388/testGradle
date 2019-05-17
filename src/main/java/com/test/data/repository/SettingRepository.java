package com.test.data.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//import com.digivisor.data.live.model.Controller;
import com.test.data.model.Sensor;
import com.test.data.model.Setting;

//import io.leangen.graphql.annotations.GraphQLQuery;

/**
 * Service that allows CRUD operations on the {@link Sensor} model.
 * 
 * <p>Here we also centralize all of the queries that can be performed on the model.</p>
 * 
 * @author hmosquera
 */
@Repository
public interface SettingRepository extends JpaRepository<Setting, UUID> {

    @Query(
        "SELECT s "
        + "FROM Setting s "
        + "WHERE "
            + "    s.name = :name "
    )
    public Optional<Setting> findByName(
        @Param("name")
        String name
    );

}
