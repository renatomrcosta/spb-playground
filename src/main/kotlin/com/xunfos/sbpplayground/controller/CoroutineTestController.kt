package com.xunfos.sbpplayground.controller

import com.xunfos.sbpplayground.util.trace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Contrived work example to test out how Worker Pools are managed using Reactor + Coroutines
 */
// @RestController
class CoroutineTestController {

    @GetMapping("healthcheck")
    fun blockedHealthCheck(): String {
        trace("Health Check")
        return "OK"
    }

    @GetMapping("long_wait_blocking")
    fun longWaitBlocking() {
        doBlockingWork()
    }

    @GetMapping("long_wait_run_blocking")
    suspend fun longWaitRunBlocking() = runBlocking {
        doSuspendWork()
    }

    @GetMapping("/long_wait")
    suspend fun longWait() = coroutineScope {
        doSuspendWork()
    }

    @GetMapping("/long_wait_context")
    suspend fun longWaitContext() = coroutineScope {
        trace("Starting Execution of Suspend Work Get")

        withContext(Dispatchers.IO) { doSuspendWork() }

        trace("Finishing Execution of Suspend Work Get")
    }

    @GetMapping("/stress_work")
    suspend fun stressWork() = coroutineScope {
        trace("Starting Execution of Stress Test")

        doStressWork()

        trace("Finishing Execution of Stress Test")
    }

    @GetMapping("/stress_work_blocking")
    suspend fun stressWorkBlocking() = coroutineScope {
        trace("Starting Execution of Stress Test BLOCKING")

        doStressWorkBlocking()

        trace("Finishing Execution of Stress Test BLOCKING ")
    }

}

private fun doBlockingWork() {
    trace("Before blocking for 10s")
    Thread.sleep(10_000)
    trace("Finished blocking for 10s")
}

private suspend fun doSuspendWork() {
    trace("Before suspending for 10s")
    delay(10000)
    trace("Finished suspending for 10s")
}
private suspend fun doStressWork() {
    coroutineScope {
        trace("Before stress work")

        repeat(10_000) {
            launch {
                delay(10_000)
                trace("Finished Job #$it")
            }
        }
    }
    trace("After finishing stress work")
}

private suspend fun doStressWorkBlocking() {
    coroutineScope {
        trace("before stress work BLOCKING")

        repeat(10_000) {
            launch {
                Thread.sleep(10_000)
                trace("Finished Blocking Job #$it")
            }
        }
    }
    trace("After finishing stress work BLOCKING")
}
