/* Tables */

/* Attribute */
CREATE TABLE Attribute (
    id                       UUID NOT NULL PRIMARY KEY,
    version                  INTEGER NOT NULL,
    createdOn                TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    updatedOn                TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,

    imageId                  UUID,
    status                   VARCHAR(256) NOT NULL,
    name                     VARCHAR(60) NOT NULL,
    type                     VARCHAR(256) NOT NULL,
    required                 BOOLEAN,
    DESCRIPTION              VARCHAR(1024),
    prefix                   VARCHAR(20),
    suffix                   VARCHAR(20),
    systemRecord             BOOLEAN NOT NULL,
    copiedFromId             UUID,
    options                  VARCHAR(1024)
);

/* Controller */
CREATE TABLE Controller (
    id                            UUID NOT NULL PRIMARY KEY,
    version                       INTEGER NOT NULL,
    createdOn                     TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    updatedOn                     TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,

    status                        VARCHAR(256) NOT NULL,
    alias                         VARCHAR(60) NOT NULL,
    ipAddress                     VARCHAR(50) NOT NULL
);

/* Location */
CREATE TABLE Location (
    id                            UUID NOT NULL PRIMARY KEY,
    version                       INTEGER NOT NULL,
    createdOn                     TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    updatedOn                     TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,

    dtype                         VARCHAR(256) NOT NULL,
    status                        VARCHAR(256) NOT NULL,
    path                          VARCHAR(256) NOT NULL,
    sequence                      INTEGER NOT NULL,

    parentLocationId              UUID
);

/* Sensor */
CREATE TABLE Sensor (
    id                            UUID NOT NULL PRIMARY KEY,
    version                       INTEGER NOT NULL,
    createdOn                     TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    updatedOn                     TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,

    status                        VARCHAR(256) NOT NULL,
    identifier                    VARCHAR(4) NOT NULL,
    lastReading                   NUMERIC(6,2),
    lastReadingStamp              TIMESTAMP(0) WITHOUT TIME ZONE,

    controllerId                  UUID NOT NULL,
    verifierId                    UUID NOT NULL,
    attributeId                   UUID NOT NULL
);

/* Verifier */
CREATE TABLE Verifier (
    id                            UUID NOT NULL PRIMARY KEY,
    version                       INTEGER NOT NULL,
    createdOn                     TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    updatedOn                     TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,

    tagId                         VARCHAR(256),
    photoId                       UUID,
    status                        VARCHAR(256) NOT NULL,
    paired                        BOOLEAN NOT NULL,
    name                          VARCHAR(60) NOT NULL,
    locationId                    UUID,
    locationInstructions          VARCHAR(1024)
);