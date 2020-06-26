package com.xunfos.sbpplayground.util

fun log(msg: String) = println("[${getCurrentThreadName()}] $msg")
fun getCurrentThreadName() = Thread.currentThread().name
