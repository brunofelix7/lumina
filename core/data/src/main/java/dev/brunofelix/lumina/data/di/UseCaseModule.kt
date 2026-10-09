package dev.brunofelix.lumina.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.lumina.data.use_case.CreateDeckUseCaseImpl
import dev.brunofelix.lumina.data.use_case.ObserveCurrentUserUseCaseImpl
import dev.brunofelix.lumina.data.use_case.ObserveDecksUseCaseImpl
import dev.brunofelix.lumina.data.use_case.SignInWithEmailUseCaseImpl
import dev.brunofelix.lumina.data.use_case.SignInWithGoogleUseCaseImpl
import dev.brunofelix.lumina.data.use_case.SignOutUseCaseImpl
import dev.brunofelix.lumina.data.use_case.SignUpWithEmailUseCaseImpl
import dev.brunofelix.lumina.domain.use_case.CreateDeckUseCase
import dev.brunofelix.lumina.domain.use_case.ObserveCurrentUserUseCase
import dev.brunofelix.lumina.domain.use_case.ObserveDecksUseCase
import dev.brunofelix.lumina.domain.use_case.SignInWithEmailUseCase
import dev.brunofelix.lumina.domain.use_case.SignInWithGoogleUseCase
import dev.brunofelix.lumina.domain.use_case.SignOutUseCase
import dev.brunofelix.lumina.domain.use_case.SignUpWithEmailUseCase

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindObserveDecksUseCase(
        impl: ObserveDecksUseCaseImpl
    ): ObserveDecksUseCase

    @Binds
    abstract fun bindCreateDeckUseCase(
        impl: CreateDeckUseCaseImpl
    ): CreateDeckUseCase

    @Binds
    abstract fun bindSignUpWithEmailUseCase(
        impl: SignUpWithEmailUseCaseImpl
    ): SignUpWithEmailUseCase

    @Binds
    abstract fun bindSignInWithEmailUseCase(
        impl: SignInWithEmailUseCaseImpl
    ): SignInWithEmailUseCase

    @Binds
    abstract fun bindSignInWithGoogleUseCase(
        impl: SignInWithGoogleUseCaseImpl
    ): SignInWithGoogleUseCase

    @Binds
    abstract fun bindObserveCurrentUserUseCase(
        impl: ObserveCurrentUserUseCaseImpl
    ): ObserveCurrentUserUseCase

    @Binds
    abstract fun bindSignOutUseCase(
        impl: SignOutUseCaseImpl
    ): SignOutUseCase
}
