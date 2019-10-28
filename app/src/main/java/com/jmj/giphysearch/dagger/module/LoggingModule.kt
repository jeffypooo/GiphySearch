package com.jmj.giphysearch.dagger.module

import com.jmj.giphysearch.domain.log.GlobalLogger
import com.jmj.giphysearch.domain.log.Logger
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LoggingModule {

  @Provides
  fun provideLogger(): Logger = GlobalLogger

}

