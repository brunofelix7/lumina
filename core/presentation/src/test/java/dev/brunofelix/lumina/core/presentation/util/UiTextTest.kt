package dev.brunofelix.lumina.core.presentation.util

import android.content.Context
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk

class UiTextTest : DescribeSpec({

    val resId = 42

    describe("asString") {
        it("should return the raw value of a DynamicString") {
            UiText.DynamicString("Hello").asString(mockk()) shouldBe "Hello"
        }

        it("should resolve a StringResource with its arguments") {
            val context = mockk<Context> {
                every { getString(resId, 6) } returns "Password must be at least 6 characters"
            }

            UiText.StringResource(resId, 6).asString(context) shouldBe "Password must be at least 6 characters"
        }
    }

    describe("StringResource equality") {
        it("should be equal when the resource id and arguments match") {
            val first = UiText.StringResource(resId, 6)
            val second = UiText.StringResource(resId, 6)

            first shouldBe second
            first.hashCode() shouldBe second.hashCode()
        }

        it("should differ when the arguments differ") {
            UiText.StringResource(resId, 6) shouldNotBe UiText.StringResource(resId, 8)
        }

        it("should differ when the resource id differs") {
            UiText.StringResource(resId) shouldNotBe UiText.StringResource(resId + 1)
        }
    }
})
