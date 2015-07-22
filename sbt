#!/bin/bash

FLAG=${1:-}

if [ "$1" == "--remove-old-launcher" ]; then
  RM_OLD_BIN=1
  shift
fi

root=$(
  cd $(dirname $(readlink $0 || echo $0))/..
  /bin/pwd
)

sbtjar=sbt-launch.jar

download () {
  ALREADY_TRIED=${1:-0}

  if [ ! -f $sbtjar ]; then
    echo 'downloading '$sbtjar 1>&2
    curl -L -O http://dl.bintray.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/0.13.7/$sbtjar
  fi

  test -f $sbtjar || exit 1
  sbtjar_md5=$(openssl md5 < $sbtjar|cut -f2 -d'='|awk '{print $1}')
  if [ "${sbtjar_md5}" != 7341059aa30c953021d6af41c89d2cac ]; then
    if [ "$RM_OLD_BIN" == "1" ] && [ "$ALREADY_TRIED" != "1" ]; then
      echo "Detected mismatch of binary's MD5 hash.  Re-downloading" >&2
      rm "$sbtjar"
      download 1
    else
      echo 'bad sbtjar!' 1>&2
      exit 1
    fi
  fi
}

download


test -f ~/.sbtconfig && . ~/.sbtconfig
java -ea                          \
  $SBT_OPTS                       \
  $JAVA_OPTS                      \
  -Djava.net.preferIPv4Stack=true \
  -Dfile.encoding=UTF-8           \
  -XX:+AggressiveOpts             \
  -XX:+UseParNewGC                \
  -XX:+UseConcMarkSweepGC         \
  -XX:+CMSParallelRemarkEnabled   \
  -XX:+CMSClassUnloadingEnabled   \
  -XX:MaxPermSize=1024m           \
  -XX:SurvivorRatio=128           \
  -XX:MaxTenuringThreshold=0      \
  -Xss8M                          \
  -Xms512M                        \
  -Xmx1G                          \
  -server                         \
  -jar $sbtjar "$@"
