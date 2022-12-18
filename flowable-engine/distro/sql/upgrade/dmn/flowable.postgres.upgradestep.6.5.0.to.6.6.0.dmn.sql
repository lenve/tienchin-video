
UPDATE act_dmn_databasechangeloglock SET LOCKED = TRUE, LOCKEDBY = '192.168.10.1 (192.168.10.1)', LOCKGRANTED = '2020-10-06 16:04:33.804' WHERE ID = 1 AND LOCKED = FALSE;

DROP INDEX ACT_IDX_DEC_TBL_UNIQ;

ALTER TABLE ACT_DMN_DECISION_TABLE RENAME TO ACT_DMN_DECISION;

CREATE UNIQUE INDEX ACT_IDX_DMN_DEC_UNIQ ON ACT_DMN_DECISION(KEY_, VERSION_, TENANT_ID_);

INSERT INTO act_dmn_databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('7', 'flowable', 'org/flowable/dmn/db/liquibase/flowable-dmn-db-changelog.xml', NOW(), 6, '7:4b6469565b1b00b428ffca7eab1ef253', 'dropIndex indexName=ACT_IDX_DEC_TBL_UNIQ, tableName=ACT_DMN_DECISION_TABLE; renameTable newTableName=ACT_DMN_DECISION, oldTableName=ACT_DMN_DECISION_TABLE; createIndex indexName=ACT_IDX_DMN_DEC_UNIQ, tableName=ACT_DMN_DECISION', '', 'EXECUTED', NULL, NULL, '3.5.3', '1993073833');

ALTER TABLE ACT_DMN_DECISION ADD DECISION_TYPE_ VARCHAR(255);

INSERT INTO act_dmn_databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('8', 'flowable', 'org/flowable/dmn/db/liquibase/flowable-dmn-db-changelog.xml', NOW(), 7, '7:f83b7b3228be2c4bbb554d6de45307d7', 'addColumn tableName=ACT_DMN_DECISION', '', 'EXECUTED', NULL, NULL, '3.5.3', '1993073833');

UPDATE act_dmn_databasechangeloglock SET LOCKED = FALSE, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

