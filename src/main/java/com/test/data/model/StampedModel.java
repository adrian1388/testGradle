package com.test.data.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

//import io.aexp.nodes.graphql.annotations.GraphQLIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Base class for all models that require to save the timestamp and user of when a record
 * was created and last updated. For an example of its usage.
 * 
 * <p>Notice that all timestamp properties are saved without time zone information in the database (NOT timestamptz)
 * since what is done is always store the time entered by the user in GMT and converted in the UI to the user's local time zone.
 * </p>
 * 
 * <p>
 * This gives us the following:
 * </p>
 * 
 * <p>
 * <table>
 * <tr>
 * <td><strong>Operation</strong></td>
 * <td><strong>User (Browser) in Miami time</strong></td>
 * <td>&nbsp;</td>
 * <td><strong>Application (Server) in GMT time</strong></td>
 * <td>&nbsp;</td>
 * <td><strong>Database (Server) in GMT time</strong></td>
 * </tr>
 * 
 * <tr>
 * <td width="80">Save</td>
 * <td>Jan/05/2011 10:00</td>
 * <td>-&gt;</td>
 * <td>Jan/05/2011 14:00</td>
 * <td>-&gt;</td>
 * <td>Jan/05/2011 14:00</td>
 * </tr>
 * 
 * <tr>
 * <td>Read</td>
 * <td>Jan/05/2011 12:00</td>
 * <td>&lt;-</td>
 * <td>Jan/05/2011 16:00</td>
 * <td>&lt;-</td>
 * <td>Jan/05/2011 16:00</td>
 * </tr>
 * </table>
 * 
 * </p>
 * 
 * <p>Here it is seen that it is the Browser showing the user the time locally, all programs calculate everything in GMT time.</p>
 * 
 * <p>
 * Time zone that the user is working on is obtained directly from the browser, we no longer have to keep the time zone in
 * Office.timeZone as it was done in previous versions.
 * </p>
 * 
 * @author omar
 */
@MappedSuperclass
@Data
@EqualsAndHashCode(
    callSuper = true
)
public abstract class StampedModel<I> extends AbstractModel<I> {
    /* Field definition */

    /**
     * <strong>Created on</strong>
     * 
     * <p>Obtain the date and time that the record was created on.</p>
     */
    @Column(nullable = false, name="createdOn")
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    @GraphQLIgnore
    private LocalDateTime creatdOn = LocalDateTime.now();

    /**
     * <strong>Updated on</strong>
     * 
     * <p>Obtain the date and time that the record was last updated on.</p>
     */
    @Column(nullable = false, name="updatedOn")
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    @GraphQLIgnore
    private LocalDateTime updatdOn = LocalDateTime.now();



    /**
     * <strong>Created On</strong>
     * 
     * <p>Used to retrieve this field's data from the cloud. This is not sent to the local database.</p>
     * 
     * TODO Done this way because I have not found a way to deserialize a date from AmericanExpress Nodes query.
     */
    @Transient
    private String createdOn;
    public void setCreatedOn(String timestamp) {
        this.creatdOn = LocalDateTime.parse(timestamp);
    }

    /**
     * <strong>Updated On</strong>
     * 
     * <p>Used to retrieve this field's data from the cloud. This is not sent to the local database.</p>
     * 
     * TODO Done this way because I have not found a way to deserialize a date from AmericanExpress Nodes query.
     */
    @Transient
    private String updatedOn;
    public void setUpdatedOn(String timestamp) {
        this.updatdOn = LocalDateTime.parse(timestamp);
    }
}