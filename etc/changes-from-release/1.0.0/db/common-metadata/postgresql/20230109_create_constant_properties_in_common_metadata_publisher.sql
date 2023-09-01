-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3460 - Publicador. Reenviar email de contacto por defecto
    -- "edatos.publisher.ckan.default.contact_email
-- ---------------------------------------------------------------------------------------------------

--ATENCIÓN!!!  RELLENAR FILL-ME

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
'edatos.publisher.ckan.default.contact_email',FILL-ME,false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


/*
 Para ISTAC de todos los entornos poner siempre
 insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
'edatos.publisher.ckan.default.contact_email','consultas.istac@gobiernodecanarias.org',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

 */


commit;
