package com.test.data.eclipselink;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.persistence.JoinColumn;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.helper.DatabaseTable;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.ManyToManyMapping;
import org.eclipse.persistence.mappings.ObjectReferenceMapping;
import org.eclipse.persistence.sessions.Session;

import com.carvajalonline.Utils;
//import com.carvajalonline.annotation.Audited;
import com.test.data.model.StampedModel;

/**
 * Customizes the EclipseLink session so that our apps work uniformly.
 * 
 * <p>In summary, here configure EclipseLink with the following:</p>
 * <ul>
 * <li>Classes annotated with {@link Audited} enable the the history policy (audit trail).</li>
 * <li>Configures the naming strategy for our foreign keys so that {@link JoinColumn} doesn't have to be used in all of the models.</li>
 * <li>Initializing the {@link AuditingListener} class which updates the updatedOn and createdOn fields in {@link StampedModel}s.</li>
 * <li>Sets the column definition to "uuid" for {@link UUID} primary keys. This is done for the EclipseLink schema generation.</li>
 * </ul>
 * 
 * @author omar
 */
public class GenericSession implements SessionCustomizer {
    private final static String FOREIGN_KEY_SUFFIX = "Id";

    @SuppressWarnings("rawtypes")
    public void customize(Session session) throws Exception {

        Map<Class, ClassDescriptor> descriptors = session.getDescriptors();
        for (Entry<Class, ClassDescriptor> entry : descriptors.entrySet()) {
            Class<?> modelClass = entry.getKey();
            ClassDescriptor descriptor = entry.getValue();

            for (DatabaseMapping mapping : descriptor.getMappings()) {
                if (mapping.isJPAId()) {
                    if (modelClass.getDeclaredField(mapping.getAttributeName()).getType() == UUID.class) {
                        //For primary keys that are UUID, set the column definition to uuid so that the EclipseLink schema is generated correctly in the database.
                        mapping.getField().setColumnDefinition("uuid");
                    }
                }

                /*
                 * Configure our naming strategy so we don't have to set @JoinColumns in our models.
                 * Inspiration for this came from: https://gist.github.com/ganeshs/c0deb77ffae33dee4555
                 */
                if (mapping instanceof ObjectReferenceMapping) {
                    //@OneToOne and @OneToMany
                    for (DatabaseField foreignKey : ((ObjectReferenceMapping) mapping).getForeignKeyFields()) {
                        foreignKey.setName(mapping.getAttributeName() + FOREIGN_KEY_SUFFIX);
                    }
                } else if (mapping instanceof ManyToManyMapping) {
                    //@ManyToMany
                    ManyToManyMapping manyToManyMapping = (ManyToManyMapping) mapping;
                    DatabaseTable joinTable = manyToManyMapping.getRelationTable();

                    String owningModel = descriptor.getJavaClass().getSimpleName();
                    String nonOwningModel = descriptor.getJavaClass().getSimpleName();

                    if (manyToManyMapping.isOwned()) {
                        //Current mapping is the owning side
                        nonOwningModel = manyToManyMapping.getReferenceClass().getSimpleName();

                        //Set the foreign keys, which only needs to be done on the owning side apparently
                        for (DatabaseField foreignKey : manyToManyMapping.getSourceRelationKeyFields()) {
                            foreignKey.setName(Utils.uncapitalize(owningModel) + FOREIGN_KEY_SUFFIX);
                        }

                        for (DatabaseField foreignKey : manyToManyMapping.getTargetRelationKeyFields()) {
                            foreignKey.setName(Utils.uncapitalize(nonOwningModel) + FOREIGN_KEY_SUFFIX);
                        }
                    } else {
                        //Current mapping is the non-owning side
                        owningModel = manyToManyMapping.getReferenceClass().getSimpleName();
                    }

                    joinTable.setName(owningModel + nonOwningModel);
                }
            }

            configureDescriptor(session, descriptor);

            //Configures audit trailing on all classes with the annotation @Audited.
            
            // TODO This has to be commented as workaround to the following eclipselink bug: https://bugs.eclipse.org/bugs/show_bug.cgi?id=526836
//            if (modelClass.getAnnotation(Audited.class) != null) {
//                //http://wiki.eclipse.org/EclipseLink/Examples/JPA/History
//
//                //Define the history policy
//                HistoryPolicy policy = new HistoryPolicy();
//                //Notice that the audit log tables are in a separate schema called "audit".
//                policy.addHistoryTableName("audited." + modelClass.getSimpleName());
//                policy.addStartFieldName("auditStart");
//                policy.addEndFieldName("auditEnd");
//
//                //Set it to the annotated class descriptor
//                descriptor.setHistoryPolicy(policy);
//            }
        }
    }

    /**
     * Stub in case a subclass needs to configure something for each class,
     * we're already looping through the entities in customize so it doesn't make sense to do it more than once.
     */
    protected void configureDescriptor(Session session, ClassDescriptor descriptor) {

    }
}