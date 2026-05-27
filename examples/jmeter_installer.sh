#!/bin/sh

_PWD=$(pwd)
_JMETER_VERSION="5.6.3"
_JMETER_DOWNLOAD_URL="https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-$_JMETER_VERSION.tgz"
_SCHEMA_ASSERTION_VERSION="1.3.1"
_SCHEMA_ASSERTION_PLUGIN_NAME="ApacheJmeter_Schema_Assertion-${_SCHEMA_ASSERTION_VERSION}.jar"
_SCHEMA_ASSERTION_DOWNLOAD_URL="https://github.com/yeshan333/ApacheJmeter_Schema_Assertion/releases/download/v${_SCHEMA_ASSERTION_VERSION}/${_SCHEMA_ASSERTION_PLUGIN_NAME}"

echo "\nDownloading JMeter from" $_JMETER_DOWNLOAD_URL
curl -L $_JMETER_DOWNLOAD_URL -k >apache-jmeter-$_JMETER_VERSION.tgz

echo "\nUnzipping .tgz binary..."
tar zxvf $_PWD/apache-jmeter-$_JMETER_VERSION.tgz

echo "\nDownloading Schema Assertion plugin from" $_SCHEMA_ASSERTION_DOWNLOAD_URL
curl -L $_SCHEMA_ASSERTION_DOWNLOAD_URL -o $_PWD/apache-jmeter-$_JMETER_VERSION/lib/ext/$_SCHEMA_ASSERTION_PLUGIN_NAME

echo "\nRemove binary tgz ..."
rm -rf apache-jmeter-$_JMETER_VERSION.tgz

sleep 2
echo "\nDone!"
