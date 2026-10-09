package dev.brunofelix.lumina.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.brunofelix.lumina.domain.use_case.SignOutUseCase
import dev.brunofelix.lumina.domain.use_case.SignOutUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileUseCaseModule {

    @Binds
    abstract fun bindSignOutUseCase(
        impl: SignOutUseCaseImpl
    ): SignOutUseCase
}
