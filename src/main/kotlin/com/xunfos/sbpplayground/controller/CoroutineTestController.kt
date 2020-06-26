package com.xunfos.sbpplayground.controller

import com.xunfos.sbpplayground.util.log
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
@RestController
class CoroutineTestController {

    @GetMapping("healthcheck")
    fun blockedHealthCheck(): String {
        log("Health Check")
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
        log("Starting Execution of Suspend Work Get")

        withContext(Dispatchers.IO) { doSuspendWork() }

        log("Finishing Execution of Suspend Work Get")
    }

    @GetMapping("/stress_work")
    suspend fun stressWork() = coroutineScope {
        log("Starting Execution of Stress Test")

        doStressWork()

        log("Finishing Execution of Stress Test")
    }

    @GetMapping("/stress_work_blocking")
    suspend fun stressWorkBlocking() = coroutineScope {
        log("Starting Execution of Stress Test BLOCKING")

        doStressWorkBlocking()

        log("Finishing Execution of Stress Test BLOCKING ")
    }

}

private fun doBlockingWork() {
    log("Before blocking for 10s")
    Thread.sleep(10_000)
    log("Finished blocking for 10s")
}

private suspend fun doSuspendWork() {
    log("Before suspending for 10s")
    delay(10000)
    log("Finished suspending for 10s")
}
private suspend fun doStressWork() {
    coroutineScope {
        log("Before stress work")

        repeat(10_000) {
            launch {
                delay(10_000)
                log("Finished Job #$it")
            }
        }
    }
    log("After finishing stress work")
}

private suspend fun doStressWorkBlocking() {
    coroutineScope {
        log("before stress work BLOCKING")

        repeat(10_000) {
            launch {
                Thread.sleep(10_000)
                log("Finished Blocking Job #$it")
            }
        }
    }
    log("After finishing stress work BLOCKING")
}
