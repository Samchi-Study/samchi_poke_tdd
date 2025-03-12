package com.samchi.poke.kanghwi.presentation

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.samchi.poke.kanghwi.LocalDataSource
import com.samchi.poke.kanghwi.pagingsource.PokemonPagingSource
import com.samchi.poke.model.Pokemon
import com.samchi.poke.network.PokeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
internal object PagerModule {

    @Provides
    fun providePager(
        api: PokeApi,
        localDataSource: LocalDataSource
    ): Pager<Int, Pokemon> = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = true
        ),
        pagingSourceFactory = { PokemonPagingSource(api, localDataSource) }
    )

}
