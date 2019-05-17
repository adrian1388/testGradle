package com.test.data.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

//import io.aexp.nodes.graphql.annotations.GraphQLIgnore;
import lombok.Data;

/**
 * Base class for all models in a CCI application, meaning that all JPA models must inherit this
 * or one of it's subclasses.
 * 
 * @author omar
 */
@MappedSuperclass
@Data
public abstract class AbstractModel<I> implements Persistable<I> {
    /* Field definition */

    //Primary Key
    /**
     * Obtains this record's primary key value.
     * 
     * @return Primary key value for this record, if this record has not been committed to the database, this will return null.
     */
    public abstract I getId();

    /**
     * Sets the primary key to a specific value.
     * 
     * <p>This is useful for UUID cases where the client sends us the primary keys (for offline support).</p>
     */
    public abstract void setId(I id);
    //End of Primary Key

    /**
     * <strong>Version</strong>
     * 
     * <p>Handles Optimistic Locking for records, this value gets added every time a record gets modified.</p>
     * 
     * <p>For details see: http://en.wikibooks.org/wiki/Java_Persistence/Locking#Optimistic_Locking</p>
     */
    @Column(nullable = false)
    @Version
//    @GraphQLIgnore
    protected Integer version;

    /* Life-cycle methods */

    /*
     * Set up the base entity callback methods so they are overridden by any subclass.<br />
     * We do this so we don't run into the problem specified in:<br />
     * http://www.coderanch.com/t/418776/java-EJB-SCBCD/certification/Multiple-Entity-callback-methods-entity
     */
    @PrePersist
    protected void prePersist() {
        preSave();
    }

    @PostPersist
    protected void postPersist() { /* Stub */ }

    @PostLoad
    protected void postLoad() { /* Stub */ }

    @PreUpdate
    protected void preUpdate() {
        preSave();
    }

    @PostUpdate
    protected void postUpdate() { /* Stub */ }

    @PreRemove
    protected void preRemove() { /* Stub */ }

    @PostRemove
    protected void postRemove() { /* Stub */ }

    protected void preSave() { /* Stub */ }

    @JsonIgnore
    public boolean isNew() {
        return version == null || version < 1;
    }
}
