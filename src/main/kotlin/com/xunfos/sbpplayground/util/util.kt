package com.xunfos.sbpplayground.util

import kotlin.system.measureTimeMillis

fun trace(msg: Any) = println("[${getCurrentThreadName()}] $msg")
fun getCurrentThreadName() = Thread.currentThread().name

inline fun withExecutionTime(block: () -> Unit) = measureTimeMillis {
    block()
}.run { println("Total Execution time: ${this}ms") }
