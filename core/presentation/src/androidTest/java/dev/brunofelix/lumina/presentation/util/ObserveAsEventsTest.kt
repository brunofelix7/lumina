package dev.brunofelix.lumina.presentation.util

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CopyOnWriteArrayList

@RunWith(AndroidJUnit4::class)
class ObserveAsEventsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shouldDeliverEmittedEventsInOrder() {
        val events = Channel<String>(Channel.UNLIMITED)
        val receivedEvents = CopyOnWriteArrayList<String>()

        composeTestRule.setContent {
            ObserveAsEvents(events.receiveAsFlow()) { receivedEvents.add(it) }
        }
        events.trySend("first")
        events.trySend("second")

        composeTestRule.waitUntil(timeoutMillis = EVENT_TIMEOUT_MILLIS) { receivedEvents.size == 2 }
        receivedEvents.toList() shouldBe listOf("first", "second")
    }

    private companion object {
        const val EVENT_TIMEOUT_MILLIS = 5_000L
    }
}
