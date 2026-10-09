package dev.brunofelix.lumina.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.lumina.domain.use_case.ObserveCurrentUserUseCase
import dev.brunofelix.lumina.domain.use_case.ObserveCurrentUserUseCaseImpl
import dev.brunofelix.lumina.domain.use_case.ObserveDecksUseCase
import dev.brunofelix.lumina.domain.use_case.ObserveDecksUseCaseImpl
import dev.brunofelix.lumina.domain.use_case.SignUpWithEmailUseCase
import dev.brunofelix.lumina.domain.use_case.SignUpWithEmailUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindObserveCurrentUserUseCase(
        impl: ObserveCurrentUserUseCaseImpl
    ): ObserveCurrentUserUseCase

    @Binds
    abstract fun bindObserveDecksUseCase(
        impl: ObserveDecksUseCaseImpl
    ): ObserveDecksUseCase

    @Binds
    abstract fun bindSignUpWithEmailUseCase(
        impl: SignUpWithEmailUseCaseImpl
    ): SignUpWithEmailUseCase
}
