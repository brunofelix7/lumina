package dev.brunofelix.lumina.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.lumina.data.repository.AuthRepositoryImpl
import dev.brunofelix.lumina.data.repository.DeckRepositoryImpl
import dev.brunofelix.lumina.domain.repository.AuthRepository
import dev.brunofelix.lumina.domain.repository.DeckRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDeckRepository(
        impl: DeckRepositoryImpl
    ): DeckRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}
