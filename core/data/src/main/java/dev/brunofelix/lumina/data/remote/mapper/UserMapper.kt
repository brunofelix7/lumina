package dev.brunofelix.lumina.data.remote.mapper

import com.google.firebase.auth.FirebaseUser
import dev.brunofelix.lumina.data.remote.dto.UserDto
import dev.brunofelix.lumina.domain.model.User

fun FirebaseUser.toDomain(): User {
    return User(
        id = uid,
        name = displayName.orEmpty(),
        email = email.orEmpty(),
        photoUrl = photoUrl?.toString()
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        name = name,
        email = email,
        photoUrl = photoUrl
    )
}
