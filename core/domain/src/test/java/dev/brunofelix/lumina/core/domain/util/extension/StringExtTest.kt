package dev.brunofelix.lumina.core.domain.util.extension

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class StringExtTest : DescribeSpec({

    describe("isValidEmail") {
        it("should accept well-formed addresses") {
            listOf("nova@lumina.dev", "nova.star+e2e@mail.example.com", "n_1%x@a-b.io").forEach { email ->
                withClue(email) { email.isValidEmail() shouldBe true }
            }
        }

        it("should reject malformed addresses") {
            listOf("", "nova", "nova@lumina", "nova@.dev", "nova lumina@dev.com", " nova@lumina.dev").forEach { email ->
                withClue("'$email'") { email.isValidEmail() shouldBe false }
            }
        }
    }
})
