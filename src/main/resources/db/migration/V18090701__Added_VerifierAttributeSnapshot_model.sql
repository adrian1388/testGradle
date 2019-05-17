/* VerifierAttributeSnapshot */
CREATE TABLE VerifierAttributeSnapshot (
    id                            UUID NOT NULL PRIMARY KEY,
    version                       INTEGER NOT NULL,
    createdOn                     TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    updatedOn                     TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,

    lastReading                   NUMERIC(6,2) NOT NULL,
    lastReadingStamp              TIMESTAMP(0) WITHOUT TIME ZONE,
    lowestReading                 NUMERIC(6,2),
    highestReading                NUMERIC(6,2),

    verifierId                    UUID,
    attributeId                   UUID
);