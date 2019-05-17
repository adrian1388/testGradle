package com.test.data.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

//import io.aexp.nodes.graphql.annotations.GraphQLIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A snapshot of the last attribute value for a verifier. 
 * This is done because the value can come from a sensor or manually from a V-Reader.
 * 
 * @author hmosquera
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
public class VerifierAttributeSnapshot extends StampedModel<UUID> {
    /* Field definition */

    /**
     * <strong>Primary Key</strong>
     * 
     * <p>Unique primary key that is randomly generated so that records can be created offline.</p>
     */
    @Id
    @Column
    private UUID id = UUID.randomUUID();

    /**
     * <strong>Last Reading</strong>
     * 
     * <p>Last value reported for the attribute at the verifier.</p>
     */
    @Column(nullable = false)
    private BigDecimal lastReading = new BigDecimal(0);

    /**
     * <strong>Last Reading Stamp</strong>
     * 
     * <p>Date and time that a value was last reported for the verifier/attribute.</p>
     */
//    @Column(nullable = true, name="lastReadingStamp")
//    @GraphQLIgnore
//    private Instant lastReadingSt;

    /**
     * <strong>Last Reading Stamp</strong>
     * 
     * <p>Used to retrieve this field's data from the cloud. This is not sent to the local database.</p>
     * 
     * TODO Done this way because I have not found a way to deserialize a date from AmericanExpress Nodes query.
     */
//    @Transient
//    private String lastReadingStamp;
//    public void setLastReadingStamp(String timestamp) {
//        this.lastReadingSt = Instant.parse(timestamp);
//    }
//
//    public Instant getLastReadingStamp() {
//        return lastReadingSt;
//    }

    /**
     * <strong>Lowest Reading</strong>
     * 
     * <p>Lowest reading for the verifier/attribute that has been reported since last reset.</p>
     */
    @Column(nullable = false)
    private BigDecimal lowestReading;

    /**
     * <strong>Highest Reading</strong>
     * 
     * <p>Highest reading for the verifier/attribute that has been reported since last reset.</p>
     */
    @Column(nullable = false)
    private BigDecimal highestReading;

    /* Relationships */

    /**
     * <strong>Verifier</strong>
     * 
     * <p>Verifier that the task was performed on.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @GraphQLIgnore
    private Verifier verifier;

    /**
     * <strong>Attribute</strong>
     * 
     * <p>Attribute that this value snapshot is of.</p>
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
//    @Transient
    private Attribute attribute;

//    @Column(insertable = true, updatable = false)
//    private UUID attributeId;
}
