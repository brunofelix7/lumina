package dev.brunofelix.lumina.core.data.remote.mapper

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import dev.brunofelix.lumina.core.data.remote.dto.UserDto
import dev.brunofelix.lumina.core.domain.model.User
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class UserMapperTest : DescribeSpec({

    describe("FirebaseUser.toDomain") {
        it("should map every profile field") {
            val photoUri = mockk<Uri> { every { this@mockk.toString() } returns "https://lumina.dev/nova.png" }
            val firebaseUser = mockk<FirebaseUser> {
                every { uid } returns "uid-1"
                every { displayName } returns "Nova Star"
                every { email } returns "nova@lumina.dev"
                every { photoUrl } returns photoUri
            }

            firebaseUser.toDomain() shouldBe User(
                id = "uid-1",
                name = "Nova Star",
                email = "nova@lumina.dev",
                photoUrl = "https://lumina.dev/nova.png"
            )
        }

        it("should fall back to empty values when the profile is incomplete") {
            val firebaseUser = mockk<FirebaseUser> {
                every { uid } returns "uid-2"
                every { displayName } returns null
                every { email } returns null
                every { photoUrl } returns null
            }

            firebaseUser.toDomain() shouldBe User(id = "uid-2", name = "", email = "", photoUrl = null)
        }
    }

    describe("User.toDto") {
        it("should map the profile fields and leave createdAt for the server") {
            val user = User(
                id = "uid-1",
                name = "Nova Star",
                email = "nova@lumina.dev",
                photoUrl = "https://lumina.dev/nova.png"
            )

            user.toDto() shouldBe UserDto(
                name = "Nova Star",
                email = "nova@lumina.dev",
                photoUrl = "https://lumina.dev/nova.png",
                createdAt = null
            )
        }
    }
})
