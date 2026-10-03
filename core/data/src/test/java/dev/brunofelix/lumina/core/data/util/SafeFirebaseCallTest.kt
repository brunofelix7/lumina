package dev.brunofelix.lumina.core.data.util

import dev.brunofelix.lumina.core.domain.util.exception.AuthException
import dev.brunofelix.lumina.core.domain.util.exception.RemoteException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.cancellation.CancellationException

class SafeFirebaseCallTest : DescribeSpec({

    describe("safeFirebaseCall") {
        it("should wrap the returned value in a success") {
            runTest {
                safeFirebaseCall { "value" } shouldBeSuccess "value"
            }
        }

        it("should map a thrown exception to a domain failure") {
            runTest {
                val result = safeFirebaseCall<String> { throw IllegalStateException("boom") }

                result.exceptionOrNull().shouldBeInstanceOf<RemoteException.Unknown>()
            }
        }

        it("should keep domain exceptions thrown by the call") {
            runTest {
                val error = AuthException.EmptyName()

                val result = safeFirebaseCall<String> { throw error }

                result.exceptionOrNull() shouldBe error
            }
        }

        it("should rethrow coroutine cancellation") {
            runTest {
                shouldThrow<CancellationException> {
                    safeFirebaseCall<String> { throw CancellationException("cancelled") }
                }
            }
        }
    }
})
