package com.test.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.data.model.Verifier;

/**
 * Service that allows CRUD operations on the {@link Verifier} model.
 * 
 * <p>Here we also centralize all of the queries that can be performed on the model.</p>
 * 
 * @author hmosquera
 */
@Repository
public interface VerifierRepository extends JpaRepository<Verifier, UUID> {

}
