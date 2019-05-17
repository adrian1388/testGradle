package com.test.data.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

//import com.carvajalonline.lookup.annotation.LookupNamed;

//import io.aexp.nodes.graphql.annotations.GraphQLIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
public class Sensor extends StampedModel<UUID> {
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
     * <strong>Status</strong>
     * 
     * <p>Indicates the state of the record, that is active or inactive.</p>
     */
    @Column(length = 256, nullable = false)
//    @LookupNamed("General.status")
    private String status = "active";

    /**
     * <strong>Identifier</strong>
     * 
     * <p>Unique sensor identifier (register) that the device sends in order to identify itself. Usually this is the Modbus identifier.</p>
     */
    @Column(length = 4, nullable = false)
    private String identifier;

    /**
     * <strong>Last Reading</strong>
     * 
     * <p>Last value that the sensor reported.</p>
     */
    @Column(nullable = true)
    private BigDecimal lastReading;

//    /**
//     * <strong>Last Reading Stamp</strong>
//     * 
//     * <p>Date and time that the sensor last reported a value.</p>
//     */
//    @Column(nullable = true)
////    @GraphQLIgnore
//    private Instant lastReadingStamp;

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
//    	if (timestamp != null) {
//    		this.lastReadingSt = LocalDateTime.ofInstant(Instant.parse(timestamp), ZoneOffset.UTC);
//    	}
//        
//    }

    /* Relationships */

//    /**
//     * <strong>Controller</strong>
//     * 
//     * <p>Controller that this sensor must report the readings to.</p>
//     */
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    private Controller controller;
//
    /**
     * <strong>Verifier</strong>
     * 
     * <p>Verifier that the task was performed on.</p>
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Verifier verifier;

    /**
     * <strong>Attribute</strong>
     * 
     * <p>Attribute that this sensor automatically fills out.</p>
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Attribute attribute;
}
