package com.xunfos.sbpplayground.controller

import com.xunfos.sbpplayground.util.trace
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeoutOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

// @RestController
// @RequestMapping("/work")
class SlowController {

    val rng = Random(System.currentTimeMillis())

    @GetMapping("/value")
    suspend fun getValue(): Int = coroutineScope {
        delay(rng.nextLong(50, 500))
        rng.nextInt(1, 32000)
    }

    @GetMapping("/count")
    suspend fun countValuesForTime(
        @RequestParam("millis") millis: Long
    ): Int = coroutineScope {
        trace("Starting count function with $millis millis")
        var result = 0
        withTimeoutOrNull(millis) {
            while (isActive) {
                result++
                trace("Iterated value. Current Total $result")
                delay(100)
            }
        }
        trace("Starting count function with $millis millis. Final Result $result")
        result
    }

    @GetMapping("/wait")
    suspend fun wait(
        @RequestParam("millis") millis: Long
    ) {
        trace("Starting work function with $millis millis")
        Thread.sleep(millis)
        trace("Finishing work function with $millis millis")
    }
}
