package dev.brunofelix.lumina.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.lumina.data.remote.source.AuthRemoteDataSource
import dev.brunofelix.lumina.data.remote.source.AuthRemoteDataSourceImpl
import dev.brunofelix.lumina.data.remote.source.DeckRemoteDataSource
import dev.brunofelix.lumina.data.remote.source.DeckRemoteDataSourceImpl
import dev.brunofelix.lumina.data.remote.source.UserRemoteDataSource
import dev.brunofelix.lumina.data.remote.source.UserRemoteDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(
        impl: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindUserRemoteDataSource(
        impl: UserRemoteDataSourceImpl
    ): UserRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindDeckRemoteDataSource(
        impl: DeckRemoteDataSourceImpl
    ): DeckRemoteDataSource
}
