package com.kmno.tmdb.domain.movie

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kmno.tmdb.data.local.MovieDao
import com.kmno.tmdb.data.local.MovieEntity
import com.kmno.tmdb.data.remote.MovieDto
import com.kmno.tmdb.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Kamran Nourinezhad on 16 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val movieDao: MovieDao
) : MovieRepository {

    override suspend fun getNowPlayingMovies(page: Int): List<Movie> {
        val response = remoteDataSource.getNowPlayingMovies(page)
        return response.results.map { it.toDomain() }
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        val response = remoteDataSource.searchMovies(query)
        return response.results.map { it.toDomain() }
    }

    override suspend fun fetchMovieDetails(movieId: Int): Flow<Movie> {
        return flow {
            val response = remoteDataSource.fetchMovieDetails(movieId)
            emit(response.toDomain())
        }
    }

    override fun getWatchlist(): Flow<List<Movie>> {
        return movieDao.getWatchlist().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun addToWatchlist(movie: Movie) {
        movieDao.addToWatchlist(movie.toEntity())
    }

    override suspend fun removeFromWatchlist(movie: Movie) {
        movieDao.removeFromWatchlist(movie.toEntity())
    }

    override suspend fun isInWatchlist(movieId: Int): Boolean {
        Timber.e("MovieRepositoryImpl", "Checking if movie with ID $movieId is in watchlist")
        Timber.d(movieDao.isInWatchlist(movieId).toString())
        return movieDao.isInWatchlist(movieId)
    }

    override fun getNowPlayingPagingFlow(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NowPlayingPagingSource(remoteDataSource) }
        ).flow
    }
}

class NowPlayingPagingSource @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = remoteDataSource.getNowPlayingMovies(page = page)
            val movies = response.results.map { it.toDomain() }

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}

// Extension functions for mapping
private fun MovieDto.toDomain(): Movie = Movie(
    id = this.id,
    title = this.title,
    overview = this.overview,
    posterPath = this.posterPath,
    releaseDate = this.releaseDate
)

private fun MovieEntity.toDomain(): Movie = Movie(
    id = this.id,
    title = this.title,
    overview = this.overview,
    posterPath = this.posterPath,
    releaseDate = this.releaseDate
)

private fun Movie.toEntity(): MovieEntity = MovieEntity(
    id = this.id,
    title = this.title,
    overview = this.overview,
    posterPath = this.posterPath,
    releaseDate = this.releaseDate
)