package dev.brunofelix.lumina.domain.util

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ResourceTest : DescribeSpec({

    describe("fold") {
        it("should run onSuccess with the data of a Success") {
            val result = Resource.Success(21).fold(
                onSuccess = { it * 2 },
                onFailure = { -1 }
            )

            result shouldBe 42
        }

        it("should run onFailure with the throwable of an Error") {
            val error = IllegalStateException("boom")

            val result = Resource.Error(error).fold(
                onSuccess = { "success" },
                onFailure = { it.message }
            )

            result shouldBe "boom"
        }
    }

    describe("toResource") {
        it("should map a successful Result to Success") {
            Result.success("deck").toResource() shouldBe Resource.Success("deck")
        }

        it("should map a failed Result to Error keeping the throwable") {
            val error = IllegalArgumentException("invalid")

            Result.failure<String>(error).toResource() shouldBe Resource.Error(error)
        }
    }
})
