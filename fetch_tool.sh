#!/bin/bash
source /etc/profile
set -e
SELF_DIR="$(cd "$(dirname "$0")" && pwd)"
cd ${SELF_DIR}

function printHelp() {
    echo -e "Fetch Tool aar into current project.\n"
    echo "Usage:"
    echo "script [OPTIONS] [ARGS...]"
    echo "    -p               specify tool project path"
}

function genTimestamp(){
    date +"%Y-%m-%d_%H-%M"
}

function getCurrentVersion(){
    echo $(git describe --tags)
}

destModule="app"
toolSdkPro=
while getopts ":hp:" opt; do
    case ${opt} in
    h)
        printHelp
        exit 0
        ;;

    p)
        toolSdkPro=${OPTARG}
        ;;

    \?)
        echo "Invalid option: -${OPTARG}"
        exit 1
        ;;
    esac
done

if [[ "${toolSdkPro}" == "" ]];then
    echo "Please specify the path of tool project "
    exit -1
fi

if [[ ! -d "${toolSdkPro}" ]]; then
    echo "tool project path is not specified."
    exit 1
fi

cd ${toolSdkPro}
./gradlew  tool:clean tool:assembleRelease

timestamp=$(genTimestamp)
version=$(getCurrentVersion)

function fetch(){
    srcModule=$1
    dstModule=$2

    # 复制aar包
    aarPath=./${srcModule}/build/outputs/aar/${srcModule}-release.aar
    dstName="${srcModule}-${version}-${timestamp}"
    cp "${aarPath}" ${SELF_DIR}/${dstModule}/libs/${dstName}".aar"
    # 修改依赖
    oriCompile="(.*${srcModule}-.*)"
    destCompile="(name: \'${dstName}\', ext: \'aar\')"
    sed -i "s/${oriCompile}/${destCompile}/g" ${SELF_DIR}/${dstModule}/build.gradle

    echo "Fetched ${dstName}"
}

# -exec ：找到后执行命令
# rm -rf {} ：命令就是删除文件
# \; ：这是格式要求的，没有具体含义
find ${SELF_DIR} -type f -name "tool-*.aar" -exec rm -rf {} \;
fetch "tool" ${destModule}