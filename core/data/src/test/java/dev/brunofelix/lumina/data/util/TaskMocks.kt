package dev.brunofelix.lumina.data.util

import com.google.android.gms.tasks.Task
import io.mockk.every
import io.mockk.mockk

/**
 * Builds an already completed [Task], which `kotlinx.coroutines.tasks.await` resolves synchronously.
 */
fun <T> successfulTask(value: T): Task<T> = mockk {
    every { isComplete } returns true
    every { isCanceled } returns false
    every { exception } returns null
    every { result } returns value
}

fun <T> failedTask(error: Exception): Task<T> = mockk {
    every { isComplete } returns true
    every { isCanceled } returns false
    every { exception } returns error
}
