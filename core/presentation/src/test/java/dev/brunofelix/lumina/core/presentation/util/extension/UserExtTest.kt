package dev.brunofelix.lumina.core.presentation.util.extension

import dev.brunofelix.lumina.core.domain.model.User
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class UserExtTest : DescribeSpec({

    describe("displayName") {
        it("should use the trimmed name") {
            User(id = "1", name = "  Nova Star ", email = "nova@lumina.dev").displayName shouldBe "Nova Star"
        }

        it("should fall back to the email local part when the name is blank") {
            User(id = "1", name = " ", email = "nova.star@lumina.dev").displayName shouldBe "nova.star"
        }
    }

    describe("firstName") {
        it("should take the first word of the name") {
            User(id = "1", name = "Nova Star", email = "nova@lumina.dev").firstName shouldBe "Nova"
        }

        it("should keep a single-word name") {
            User(id = "1", name = "Nova", email = "nova@lumina.dev").firstName shouldBe "Nova"
        }

        it("should fall back to the email local part when the name is blank") {
            User(id = "1", name = "", email = "nova@lumina.dev").firstName shouldBe "nova"
        }
    }
})
