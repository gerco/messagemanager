#!/bin/sh

jpackage --type dmg \
    --input build/fatjar \
    --main-jar messagemanager-4.0-SNAPSHOT.jar \
    --main-class nl.queuemanager.app.Main \
    --java-options -Xmx512m \
    --java-options -DSolace_JMS_Browser_Timeout_In_MS=1000 \
    --name "Message Manager" \
    --app-version 4.0.0 \
    --vendor "Gerco Dries" \
    --copyright "(c) 2008-2022 Gerco Dries" \
    --description "A Compelling Replacement for the JMS Test Client" \
    --dest build/install \
    --mac-package-identifier MSGM \
    --mac-package-name "Message Manager" \
    --verbose
