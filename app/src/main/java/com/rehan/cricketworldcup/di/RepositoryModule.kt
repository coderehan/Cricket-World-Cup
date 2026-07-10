package com.rehan.cricketworldcup.di

import com.rehan.cricketworldcup.data.repository.TeamRepository
import com.rehan.cricketworldcup.data.repository.TeamRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module responsible for providing repository dependencies.
 * This binds the TeamRepository interface to its concrete implementation.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTeamRepository(
        teamRepositoryImpl: TeamRepositoryImpl
    ): TeamRepository
}