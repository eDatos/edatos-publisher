-- --------------
-- DB Connection
-- --------------

insert into TB_DATA_CONFIGURATIONS (ID, VERSION, SYSTEM_PROPERTY, CONF_KEY, CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 1, 1, 'metamac.portal.rest.external.authentication.captcha.url',
        'FILL_ME', 1);
UPDATE TB_SEQUENCES
SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1
WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID, VERSION, SYSTEM_PROPERTY, CONF_KEY, CONF_VALUE)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 1, 1,
        'metamac.portal.rest.external.authentication.captcha.provider', 'FILL_ME');
UPDATE TB_SEQUENCES
SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1
WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;

-- Ejemplos para el entorno de tests
-----------------------------------------
-- insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,1,'metamac.portal.rest.external.authentication.captcha.url','https://estadisticas.arte-consultores.com/external-users-external/api/captcha', 1);
-- UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

-- insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,1,'metamac.portal.rest.external.authentication.captcha.provider','simple');
-- UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

-- commit;

-- Ejemplos para el entorno de demo
-----------------------------------------
-- insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE, EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,1,'metamac.portal.rest.external.authentication.captcha.url','https://estadisticas.arte-consultores.com/external-users-external/api/captcha', 1);
-- UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

-- insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,1,'metamac.portal.rest.external.authentication.captcha.provider','simple');
-- UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

-- commit;
