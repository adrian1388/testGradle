package com.test.data.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.FetchType;
import javax.persistence.Id;
//import javax.persistence.OneToOne;

//import com.carvajalonline.annotation.FileStorageId;
//import com.carvajalonline.lookup.annotation.LookupNamed;

//import io.aexp.nodes.graphql.annotations.GraphQLIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a single datum that must be asked or shown to the user.
 * 
 * @author hmosquera
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
public class Attribute extends StampedModel<UUID> {
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
     * <strong>Name</strong>
     * 
     * <p>Name of that attribute as shown to the user. Used for referencing purposes.</p>
     */
    @Column(length = 60, nullable = false)
    private String name;

    /**
     * <strong>Type</strong>
     * 
     * <p>Type of data that must be entered by the user.</p>
     */
    @Column(length = 256, nullable = false)
//    @LookupNamed("Attribute.type")
    private String type;

    /**
     * <strong>Required</strong>
     * 
     * <p>Indicates if the attribute is required or not.</p>
     */
    @Column(nullable = true)
    private Boolean required = true;

    /**
     * <strong>Description</strong>
     * 
     * <p>Describes the attribute to the user so they have a clear understanding of what needs to be inspected/done.</p>
     */
    @Column(length = 1024, nullable = true)
    private String description;

    /**
     * <strong>Prefix</strong>
     * 
     * <p>Text that must be shown before the field that the user must fill out.</p>
     */
    @Column(length = 20, nullable = true)
    private String prefix;

    /**
     * <strong>Suffix</strong>
     * 
     * <p>Text that must be shown after the field that the user must fill out.</p>
     */
    @Column(length = 20, nullable = true)
    private String suffix;

    /**
     * <strong>Image Identification</strong>
     * 
     * <p>Identification of the file in the storage service such as Amazon S3.</p>
     * 
     * <p>This is used to later retrieve the file.</p>
     */
//    @Column(nullable = true)
//    @FileStorageId
//    private UUID imageId;

    /**
     * <strong>Options</strong>
     * 
     * <p>Options for this attribute delimited by a pipe when the attribute type is list.</p>
     */
    @Column(length = 1024, nullable = true)
    private String options;

    /**
     * <strong>Copied From</strong>
     * 
     * <p>Original system record that this record was copied from</p>
     */
//    @OneToOne(fetch = FetchType.LAZY, optional = true)
//    @GraphQLIgnore
//    private Attribute copiedFrom;

    /**
     * <strong>System Record</strong>
     * 
     * <p>Indicates if this is a system record or not. If system record is true, then only a super user can modify the record.</p>
     * 
     * <p>A regular user will make a copy of the record under their own business.</p>
     */
//    @Column(nullable = false)
//    private Boolean systemRecord = false;

}
