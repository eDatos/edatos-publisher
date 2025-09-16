-- ---------------------------------------------------------------------------------------------------
-- EDATOS-4447 - Publicador. e-Catalogo debe permitir alimentarse desde las tablas publicadas en Alfresco/JAXI
    -- "edatos.publisher.ckan.default.territory_description
    -- "edatos.publisher.ckan.spatial.uri"
    
    --Requerimiento al enviar datasets al ckan de jaxi ibestat: Se solicita añadir al título el territorio asociado. Se añadirá un parámetro constante con la descripción “Illes Balears” Siempre tendrá este valor.
-- ---------------------------------------------------------------------------------------------------

--------1) edatos.publisher.ckan.default.territory_description

--ATENCIÓN!!!  RELLENAR FILL_ME SÓLO PARA AQUELLOS ENTORNOS DONDE SE REQUIERA QUE SE AÑADA AL TÍTULO EN CKAN.

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
'edatos.publisher.ckan.default.territory_description',FILL_ME,false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

--------2) edatos.publisher.ckan.default.creator

--ATENCIÓN!!!  RELLENAR FILL_ME SÓLO PARA AQUELLOS ENTORNOS DONDE SE REQUIERA QUE SE AÑADA AL TÍTULO EN CKAN.

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
'edatos.publisher.ckan.default.creator',FILL_ME,false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';



/* ATENCIÓN DEJAR VACÍO PARA ISTAC E IESTADIS (EN CASO DE QUE SE DESPLIEGUE EN ESTE ENTORNO)
--ISTAC
 insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
'edatos.publisher.ckan.default.territory_description','',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

 */


/* IBESTAT. CONSTANTE QUE PONDRÁ SIEMPRE ILLES BALEARS

 insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
'edatos.publisher.ckan.default.territory_description','Illes Balears',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
'edatos.publisher.ckan.default.creator','Instituto de Estadística de las Islas Baleares (IBESTAT)',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


 */

---------------
---------------

-- Modificación solo para el ISTAC -> sólo para cuando se haga el despliegue
--------2) edatos.publisher.ckan.spatial.uri   Poner en mayúscula la primera letra de la palabra "canarias" si no falla con la nueva versión de ckan.
-- update tb_data_configurations
-- set conf_value = 'http://datos.gob.es/recurso/sector-publico/territorio/Autonomia/Canarias'
-- where conf_key = 'edatos.publisher.ckan.spatial.uri';

---------------
---------------

commit;

