package dev.brunofelix.lumina.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.lumina.core.data.use_case.CreateDeckUseCaseImpl
import dev.brunofelix.lumina.core.data.use_case.ObserveDecksUseCaseImpl
import dev.brunofelix.lumina.core.domain.use_case.CreateDeckUseCase
import dev.brunofelix.lumina.core.domain.use_case.ObserveDecksUseCase

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
}
