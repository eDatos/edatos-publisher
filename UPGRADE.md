# UPGRADE - Proceso de actualización entre versiones

*Para actualizar de una versión a otra es suficiente con actualizar el WAR a la última versión. El siguiente listado presenta aquellos cambios de versión en los que no es suficiente con actualizar y que requieren por parte del instalador tener más cosas en cuenta. Si el cambio de versión engloba varios cambios de versión del listado, estos han de ejecutarse en orden de más antiguo a más reciente.*

*De esta forma, si tuviéramos una instalación en una versión **A.B.C** y quisieramos actualizar a una versión posterior **X.Y.Z** para la cual existan versiones anteriores que incluyan cambios listados en este documento, se deberá realizar la actualización pasando por todas estas versiones antes de poder llegar a la versión deseada.*

*EJEMPLO: Queremos actualizar desde la versión 1.0.0 a la 3.0.0 y existe un cambio en la base de datos en la actualización de la versión 1.0.0 a la 2.0.0.*

*Se deberá realizar primero la actualización de la versión 1.0.0 a la 2.0.0 y luego desde la 2.0.0 a la 3.0.0*


## 1.0.0 a 1.0.1-SNAPSHOT
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/1.0.0/db](etc/changes-from-release/1.0.0/db) 

* Se debe modificar el archivo de configuración del ckan.ini situado en el proyecto de sistemas "/entornos/arte/desarrollo/ckan/config-deploy/ckan/default/ckan.ini
Cambios a realizar:
** Cambiar propiedad "ckan.preview.loadable"  y dejarlar así: ckan.preview.loadable = none
** Cambiar propiedad "ckan.datapusher.formats"  y dejarlar así: ckan.datapusher.formats = application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
La ruta para cada entorno sería a partir del path "CKAN_HOME/ckan/default/ckan.ini" para cada entorno.
