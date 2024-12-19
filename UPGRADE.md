# UPGRADE - Proceso de actualización entre versiones

*Para actualizar de una versión a otra es suficiente con actualizar el WAR a la última versión. El siguiente listado presenta aquellos cambios de versión en los que no es suficiente con actualizar y que requieren por parte del instalador tener más cosas en cuenta. Si el cambio de versión engloba varios cambios de versión del listado, estos han de ejecutarse en orden de más antiguo a más reciente.*

*De esta forma, si tuviéramos una instalación en una versión **A.B.C** y quisieramos actualizar a una versión posterior **X.Y.Z** para la cual existan versiones anteriores que incluyan cambios listados en este documento, se deberá realizar la actualización pasando por todas estas versiones antes de poder llegar a la versión deseada.*

*EJEMPLO: Queremos actualizar desde la versión 1.0.0 a la 3.0.0 y existe un cambio en la base de datos en la actualización de la versión 1.0.0 a la 2.0.0.*

*Se deberá realizar primero la actualización de la versión 1.0.0 a la 2.0.0 y luego desde la 2.0.0 a la 3.0.0*

## 1.3.0 a 1.3.1-SNAPSHOT
**A tener en cuenta**: Esta versión del publisher debe ir acompañada de la nueva versión de ckan preparada para la internacionalización. NO se debe desplegar el publisher sin antes haber desplegado la nueva versión del ckan (catálogo de datos abiertos)

* Se ha añadido un consumidor del topic JAXI_PUBLICATIONS al proyecto asociado a envíos desde jaxi ibestat. Tener en cuenta lo siguiente:
* Se debe desplegar primero el publicador en el entorno de IBESTAT.Para ello seguir los pasos del fichero README.md
*Se debe realizar la migración de todos los recursos de eDatos del IBESTAT con la ejecución de los dos jobs disponibles y configurables con propiedades:
    * Para los recursos del srm: propiedad metamac.srm.resend_resources_kafka_message.cron_expression 
    * Para statistical-resources: propiedad metamac.statistical_resources.resend_dataset_kafka_message.cron_expression
    * En el momento del despliegue, de IBESTAT hay que coordinar con el IBESTAT para que reenvien masivamente todos los datasets de JAXI mediante una nueva funcionalidad que han realizado. ATENCIÓN!! Antes de realizar este paso se deben desactivar las notificaciones a usuarios de external users. Para ello ejecutar el script que se encuentra en la siguiente ruta del proyecto external-users:
        * etc\changes-from-release\X.X.0\db\helper-reenvío-datasets-ckan\postgresql\20240715_disabled_notifications_by_operations_for_datasets.sql
    

## 1.2.1 a 1.3.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/1.2.1/db](etc/changes-from-release/1.2.1/db) 

## 1.0.0 a 1.0.1
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/1.0.0/db](etc/changes-from-release/1.0.0/db) 

* Se debe modificar el archivo de configuración del ckan.ini situado en el proyecto de sistemas "/entornos/arte/desarrollo/ckan/config-deploy/ckan/default/ckan.ini
Cambios a realizar:
** Cambiar propiedad "ckan.preview.loadable"  y dejarlar así: ckan.preview.loadable = none
** Cambiar propiedad "ckanext.xloader.formats"  y dejarlar así: ckanext.xloader.formats = application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
** Cambiar propiedad "ckan.datapusher.formats"  y dejarlar así: ckan.datapusher.formats = application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
La ruta para cada entorno sería a partir del path "CKAN_HOME/ckan/default/ckan.ini" para cada entorno.

* Se debe modificar el archivo de configuración del fichero "resource_formats.json" situado en el proyecto de sistemas "/entornos/arte/desarrollo/ckan/config-deploy/ckan/default/ckan.ini
 ** Asociar el formato jsonstat al formato json. Para ello cambiar la entrada de json para dejarla así:
["JSON", "JavaScript Object Notation", "application/json", ["json", "jsonstat"]],
La ruta para cada entorno sería a partir del path "CKAN_HOME/ckan/default/resource_formats.json" para cada entorno.

