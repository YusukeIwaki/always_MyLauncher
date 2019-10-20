#!/bin/sh

set -eu

if [ -n "$DEBUG_KEYSTORE_BASE64" ]; then
  mkdir -p /root/.android/
  echo $DEBUG_KEYSTORE_BASE64 | base64 -d > /root/.android/debug.keystore
fi

sh -c "$*"
