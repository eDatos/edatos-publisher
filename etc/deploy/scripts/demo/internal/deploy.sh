#!/bin/bash

HOME_PATH=edatos-publisher
TRANSFER_PATH=$HOME_PATH/tmp
SCRIPTS_PATH=$HOME_PATH/scripts
ENV_CONF=$HOME_PATH/env
DEPLOY_TARGET_PATH=/servers/edatos-internal/tomcats/edatos-internal01/webapps
DATA_RELATIVE_PATH_FILE=WEB-INF/classes/config/data-location.properties
LOGBACK_RELATIVE_PATH_FILE=WEB-INF/classes/logback.xml

RESTART=1

if [ "$1" == "--no-restart" ]; then
    RESTART=0
fi

scp -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" -r etc/deploy/config/demo/internal/* deploy@estadisticas.arte.internal:$ENV_CONF
scp -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" -r etc/deploy/utils/utilities.sh deploy@estadisticas.arte.internal:$SCRIPTS_PATH
scp -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" -r edatos-publisher-internal-web/target/edatos-publisher-internal*.war deploy@estadisticas.arte.internal:$TRANSFER_PATH/edatos-publisher-internal.war
ssh -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" deploy@estadisticas.arte.internal <<EOF

    chmod a+x $SCRIPTS_PATH/utilities.sh;
    . $SCRIPTS_PATH/utilities.sh

    ###
    # EDATOS PUBLISHER INTERNAL
    ###

    if [ $RESTART -eq 1 ]; then
        sudo service edatos-internal01 stop
        checkPROC "edatos-internal"
    fi

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/edatos-publisher-internal
    sudo mv $TRANSFER_PATH/edatos-publisher-internal.war $DEPLOY_TARGET_PATH/edatos-publisher-internal.war
    sudo unzip $DEPLOY_TARGET_PATH/edatos-publisher-internal.war -d $DEPLOY_TARGET_PATH/edatos-publisher-internal
    sudo rm -rf $DEPLOY_TARGET_PATH/edatos-publisher-internal.war

    # Restore Configuration
    sudo mv $ENV_CONF/logback.xml $DEPLOY_TARGET_PATH/edatos-publisher-internal/$LOGBACK_RELATIVE_PATH_FILE
    sudo rm -f $DEPLOY_TARGET_PATH/edatos-publisher-internal/WEB-INF/classes/config/application-env.yml
    sudo mv $ENV_CONF/data-location.properties $DEPLOY_TARGET_PATH/edatos-publisher-internal/$DATA_RELATIVE_PATH_FILE

    if [ $RESTART -eq 1 ]; then
        sudo chown -R edatos-internal:edatos-internal /servers/edatos-internal
        sudo service edatos-internal01 start
    fi

    sudo rm -rf $SCRIPTS_PATH/*

    echo "Finished deploy"

EOF
