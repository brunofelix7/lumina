package dev.brunofelix.lumina.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.lumina.domain.use_case.SignInWithEmailUseCase
import dev.brunofelix.lumina.domain.use_case.SignInWithEmailUseCaseImpl
import dev.brunofelix.lumina.domain.use_case.SignInWithGoogleUseCase
import dev.brunofelix.lumina.domain.use_case.SignInWithGoogleUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthUseCaseModule {

    @Binds
    abstract fun bindSignInWithEmailUseCase(
        impl: SignInWithEmailUseCaseImpl
    ): SignInWithEmailUseCase

    @Binds
    abstract fun bindSignInWithGoogleUseCase(
        impl: SignInWithGoogleUseCaseImpl
    ): SignInWithGoogleUseCase
}
