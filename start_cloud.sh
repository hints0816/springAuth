#!/bin/sh
nohup java -jar authserver-0.0.1-SNAPSHOT.jar &
nohup java -jar client-0.0.1-SNAPSHOT.jar &
nohup java -jar resource-0.0.1-SNAPSHOT.jar &
