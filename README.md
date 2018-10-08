# LogTool

一个用于嵌入式调试的串口log工具，通过字符串比对的方式，对从串口输出的信息进行分级，调试等级分为 Error, Warning, Info, Debug, Verbose 五级

从串口输出的数据，包含 "[Error]" 这样字符串的会被log工具识别

## log.h

```C
/**
 * File:        log.h
 * Desc:        Logging Tools
 * Author:      Letter
 * Date:        2018-09-02
 * Encoding:    UTF-8
 */
#ifndef __LOG_H__
#define __LOG_H__

#include "stdio.h"

/** 将某一log级别设置为6，可单独关闭该级别的log信息 */

#define     LOG_NONE            0
#define     LOG_ERROR           1
#define     LOG_WARNING         2
#define     LOG_INFO            3
#define     LOG_DEBUG           4
#define     LOG_VERBOSE         5
#define     LOG_ALL             5

/**
 * 可以在编译器的 target options 中定义调试级别
 * 小于等于 LOG_LEVEL 的 LOG 级别生效
 */
#ifndef     LOG_LEVEL
    #define LOG_LEVEL           3
#endif

/**
 * 在以下宏对log工具进行配置
 * 参考宏后面对应的注释
 */
#define     logPrint            printf          /** 设置格式化打印函数 */
#define     TIME_STAMP          0               /** 设置获取系统时间戳 */
#define     AUTO_TAG            0               /** 为1时，会自动将函数名作为TAG打印 */
#define     LOG_END             "\r\n"          /** 打印换行符结尾 */


#if AUTO_TAG != 1

#if LOG_ERROR <= LOG_LEVEL
    #define logError(format, ...) \
            logPrint("[%d] [Error] "format""LOG_END, TIME_STAMP, ##__VA_ARGS__)
#else
    #define logError(format, ...)
#endif /** LOG_ERROR <= LOG_LEVEL */

#if LOG_WARNING <= LOG_LEVEL
    #define logWarning(format, ...) \
            logPrint("[%d] [Warning] "format""LOG_END, TIME_STAMP, ##__VA_ARGS__)
#else
    #define logWarning(format, ...)
#endif /** LOG_WARNING <= LOG_LEVEL */

#if LOG_INFO <= LOG_LEVEL
    #define logInfo(format, ...) \
            logPrint("[%d] [Info] "format""LOG_END, TIME_STAMP, ##__VA_ARGS__)
#else
    #define logInfo(format, ...)
#endif /** LOG_INFO <= LOG_LEVEL */

#if LOG_DEBUG <= LOG_LEVEL
    #define logDebug(format, ...) \
            logPrint("[%d] [Debug] "format""LOG_END, TIME_STAMP, ##__VA_ARGS__)
    #define logDebugL(format, ...) \
            logPrint("[%d] [Debug] [file:%s, line:%d] "format""LOG_END, \
                     TIME_STAMP, __FILE__, __LINE__, ##__VA_ARGS__)
#else
    #define logDebug(format, ...)
    #define logDebugL(format, ...)
#endif /** LOG_DEBUG <= LOG_LEVEL */

#if LOG_VERBOSE <= LOG_LEVEL
    #define logVerbose(format, ...) \
            logPrint("[%d] [Verbose] "format""LOG_END, TIME_STAMP, ##__VA_ARGS__)
#else
    #define logVerbose(format, ...)
#endif /** LOG_VERBOSE <= LOG_LEVEL */

#else /** AUTO_TAG */

#if LOG_ERROR <= LOG_LEVEL
    #define logError(format, ...) \
            logPrint("[%d] [Error] %s: "format""LOG_END, \
                     TIME_STAMP, __FUNCTION__, ##__VA_ARGS__)
#else
    #define logError(format, ...)
#endif /** LOG_ERROR <= LOG_LEVEL */

#if LOG_WARNING <= LOG_LEVEL
    #define logWarning(format, ...) \
            logPrint("[%d] [Warning] %s: "format""LOG_END, \
                     TIME_STAMP, __FUNCTION__, ##__VA_ARGS__)
#else
    #define logWarning(format, ...)
#endif /** LOG_WARNING <= LOG_LEVEL */

#if LOG_INFO <= LOG_LEVEL
    #define logInfo(format, ...) \
            logPrint("[%d] [Info] %s: "format""LOG_END, \
                     TIME_STAMP, __FUNCTION__, ##__VA_ARGS__)
#else
    #define logInfo(format, ...)
#endif /** LOG_INFO <= LOG_LEVEL */

#if LOG_DEBUG <= LOG_LEVEL
    #define logDebug(format, ...) \
            logPrint("[%d] [Debug] %s: "format""LOG_END, \
                     TIME_STAMP, __FUNCTION__, ##__VA_ARGS__)
    #define logDebugL(format, ...) \
            logPrint("[%d] [Debug] [file:%s, line:%d] %s: "format""LOG_END, \
                     TIME_STAMP, __FILE__, __LINE__, __FUNCTION__, ##__VA_ARGS__)
#else
    #define logDebug(format, ...)
    #define logDebugL(format, ...)
#endif /** LOG_DEBUG <= LOG_LEVEL */

#if LOG_VERBOSE <= LOG_LEVEL
    #define logVerbose(format, ...) \
            logPrint("[%d] [Verbose] %s: "format""LOG_END, \
                     TIME_STAMP, __FUNCTION__, ##__VA_ARGS__)
#else
    #define logVerbose(format, ...)
#endif /** LOG_VERBOSE <= LOG_LEVEL */

#endif /** AUTO_TAG */

#endif /** __LOG_H__ */

```