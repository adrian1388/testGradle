package com.test.data.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.test.data.model.Attribute;
import com.test.data.model.Verifier;
import com.test.data.model.VerifierAttributeSnapshot;

/**
 * Service that allows CRUD operations on the {@link VerifierAttributeSnapshotRepository} model.
 * 
 * <p>Here we also centralize all of the queries that can be performed on the model.</p>
 * 
 * @author hmosquera
 */
@Repository
public interface VerifierAttributeSnapshotRepository extends JpaRepository<VerifierAttributeSnapshot, UUID> {

    @Query(
        " SELECT vas "
        + " FROM VerifierAttributeSnapshot vas "
        + "WHERE vas.verifier = :verifier "
        + "  AND vas.attribute = :attribute"
    )
    public Optional<VerifierAttributeSnapshot> findByVerifierAndAttribute(
        @Param("verifier")
        Verifier verifier,
        @Param("attribute")
        Attribute attribute
    );

}
