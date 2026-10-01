package dev.brunofelix.lumina.core.presentation.mock

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank

class FakeUserTest : DescribeSpec({

    describe("FakeUser") {
        it("should expose the first word of the name as the first name") {
            FakeUser.firstName shouldBe "Bruno"
        }

        it("should provide non-blank credentials to pre-fill the sign in form") {
            FakeUser.EMAIL.shouldNotBeBlank()
            FakeUser.PASSWORD.shouldNotBeBlank()
        }
    }
})
