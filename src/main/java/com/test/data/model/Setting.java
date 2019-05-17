package com.test.data.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Setting data, which configures a specific aspect of the application.
 * 
 * <p>Records in this model should be added, modified and read with the {@link SettingManager} service.</p>
 * 
 * @author omar
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
public class Setting extends StampedModel<UUID> {
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
     * <strong>Name</strong>
     * 
     * <p>Name of setting for the business, office or user.</p>
     */
    @Column(length = 60, nullable = false)
	private String name;

    /**
     * <strong>Value</strong>
     * 
     * <p>Setting's value which configures a part of the application.</p>
     * 
     * <p>The content of this property varies depending on the {@link #name} set.</p>
     */
    @Column(length = 51200, nullable = false)
	private String value;

}