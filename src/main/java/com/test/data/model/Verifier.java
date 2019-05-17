package com.test.data.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
//import javax.persistence.OneToOne;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.eclipse.persistence.queries.ReadAllQuery;

//import com.carvajalonline.annotation.FileStorageId;
//import com.carvajalonline.lookup.annotation.LookupNamed;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a verifier (NFC tag) which in turn represents a physical object to which one or more task(s) must be performed.
 * 
 * @author hmosquera
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
public class Verifier extends StampedModel<UUID> {
	/**
     * Navigating through entity to add ordering as a path expression (attribute.name ASC).
     * OrderBy(attribute.name ASC) is not a permitted option and it brings Runtime Exception.
     * 
     * from: https://www.eclipse.org/forums/index.php?t=msg&th=306481&goto=1711582&#msg_1711582
     */
    public static class Customizer implements DescriptorCustomizer {
        @Override
        public void customize(ClassDescriptor descriptor) throws Exception {
            OneToManyMapping snapshotsMapping = (OneToManyMapping) descriptor.getMappingForAttributeName("snapshots");
            ReadAllQuery snapshotsQuery = (ReadAllQuery) snapshotsMapping.getSelectionQuery();
            snapshotsQuery.addOrdering(snapshotsQuery.getExpressionBuilder().get("attribute").get("name").toUpperCase().ascending());
        }
    }

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
     * <strong>Paired</strong>
     * 
     * <p>Indicates if the record id has been paired with the physical tag (verifier).</p>
     */
    @Column(nullable = false)
    private Boolean paired = false;
    
    /**
     * <strong>Tag Id</strong>
     * 
     * <p>This is the unique NFC tag identification which is contained in the phisycal tag.</p>
     */
    @Column(length = 256, nullable = true)
    private String tagId;

    /**
     * <strong>Name</strong>
     * 
     * <p>Name of that verifier as shown to the user. Used for referencing purposes.</p>
     */
    @Column(length = 60, nullable = false)
    private String name;

    /**
     * <strong>Location Instructions</strong>
     * 
     * <p>Location of the physical object, in case it is hard to find and the user may require some instruction in locating it.</p>
     */
    @Column(length = 1024, nullable = true)
    private String locationInstructions;

    /**
     * <strong>Photo Identification</strong>
     * 
     * <p>Identification of the file in the storage service such as Amazon S3.</p>
     * 
     * <p>This is used to later retrieve the file.</p>
     */
//    @Column(nullable = true)
//    @FileStorageId
//    private UUID photoId;

    /* Relationships */

    /**
     * <strong>Location</strong>
     * 
     * <p>Location / area where this verifier is physically located.</p>
     */
//    @OneToOne(fetch = FetchType.LAZY, optional = false)
//    private Location location;

    /**
     * <strong>Snapshots</strong>
     * 
     * <p>Snapshots that have been taken for this verifier.</p>
     */
    @OneToMany(mappedBy = "verifier", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<VerifierAttributeSnapshot> snapshots;
}
