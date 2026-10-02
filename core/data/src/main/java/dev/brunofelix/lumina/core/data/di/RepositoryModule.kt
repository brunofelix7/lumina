package dev.brunofelix.lumina.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.lumina.core.data.repository.InMemoryDeckRepositoryImpl
import dev.brunofelix.lumina.core.domain.repository.DeckRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDeckRepository(
        impl: InMemoryDeckRepositoryImpl
    ): DeckRepository
}
