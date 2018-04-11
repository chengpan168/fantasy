#!/bin/bash

echo '获取fantasy进程 id'

javaId=`ps -ef |grep fantasy | grep -v grep | awk '{print $2}'`
echo '进程id:' $javaId

kill -9 $javaId
echo 'kill进程成功: ' $javaId


seconds_left=3
echo "请等待${seconds_left}秒……"
    while [ $seconds_left -gt 0 ];do
      echo $seconds_left
      sleep 1
      seconds_left=$(($seconds_left - 1))
      echo -ne "\r     \r" #清除本行文字
done

#JAVA_OPTS="-Xms1g  -Xmx1g  -Xmn1g "
#JAVA_OPTS=""$JAVA_OPTS" -XX:MetaspaceSize=256m  -XX:MaxMetaspaceSize=256m  -XX:MaxDirectMemorySize=1g "
#JAVA_OPTS=""$JAVA_OPTS" -XX:SurvivorRatio=10  -XX:+UseConcMarkSweepGC  -XX:CMSMaxAbortablePrecleanTime=5000  -XX:+CMSClassUnloadingEnabled "
#JAVA_OPTS=""$JAVA_OPTS" -XX:CMSInitiatingOccupancyFraction=80  -XX:+UseCMSInitiatingOccupancyOnly  -XX:+ExplicitGCInvokesConcurrent "
#JAVA_OPTS=""$JAVA_OPTS" -Dsun.rmi.dgc.server.gcInterval=2592000000  -Dsun.rmi.dgc.client.gcInterval=2592000000  -XX:ParallelGCThreads=4 "
#JAVA_OPTS=""$JAVA_OPTS" -Xloggc:/work/gc/logs/api/gc.log  -XX:+PrintGCDetails  -XX:+PrintGCDateStamps  -XX:+HeapDumpOnOutOfMemoryError  -XX:HeapDumpPath=/work/gc/logs/api/java.hprof "
#JAVA_OPTS=""$JAVA_OPTS" -Djava.awt.headless=true  -Dsun.net.client.defaultConnectTimeout=10000  -Dsun.net.client.defaultReadTimeout=30000  -Dfile.encoding=UTF-8"
#JAVA_OPTS=""$JAVA_OPTS" -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=6999"
#JAVA_OPTS=""$JAVA_OPTS" -javaagent:/work/tingyun/api/tingyun-agent-java.jar"
#JAVA_OPTS=""$JAVA_OPTS" -javaagent:/work/jar/apm/api/ArmsAgent/arms-bootstrap-1.7.0-SNAPSHOT.jar -Darms.licenseKey=hwo03vqgs8@175ab3e3d7da9a4 -Darms.appId=hwo03vqgs8@04cc16fcc4176e0 "

JAVA_OPTS=""$JAVA_OPTS" -Dwebdriver.chrome.driver=./chromedriver"


nohup java $JAVA_OPTS -jar fantasy-1.0.0.jar  &
