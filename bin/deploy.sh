#!/usr/bin/env bash
mvn install -Dmaven.test.skip

echo "拷贝文件到远程服务器..."
scp target/fantasy-1.0.0.jar root@47.52.74.30:/work/fantasy
echo "拷贝文件到远程服务器完成"
