package dev.brunofelix.lumina.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.lumina.domain.use_case.CreateDeckUseCase
import dev.brunofelix.lumina.domain.use_case.CreateDeckUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class DeckUseCaseModule {

    @Binds
    abstract fun bindCreateDeckUseCase(
        impl: CreateDeckUseCaseImpl
    ): CreateDeckUseCase
}
