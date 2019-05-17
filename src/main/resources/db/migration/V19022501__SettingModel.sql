
/* Setting */
CREATE TABLE Setting (
    id                       UUID NOT NULL PRIMARY KEY,
    version                  INTEGER NOT NULL,
    createdOn                TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    updatedOn                TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,

    name                     VARCHAR(60) NOT NULL,
    value                    VARCHAR(51200) NOT NULL
);
/* End of Setting */
