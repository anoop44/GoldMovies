/*
 * Designed and developed by 2019 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.mvvm_coroutines.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.skydoves.common.ApiUtil.getCall
import com.skydoves.common.MockTestUtils.Companion.mockMovieList
import com.skydoves.common.MockTestUtils.Companion.mockTvList
import com.skydoves.entity.database.MovieDao
import com.skydoves.entity.database.TvDao
import com.skydoves.entity.entities.Movie
import com.skydoves.entity.entities.Tv
import com.skydoves.entity.response.DiscoverMovieResponse
import com.skydoves.entity.response.DiscoverTvResponse
import com.skydoves.network.ApiResponse
import com.skydoves.network.client.TheDiscoverClient
import com.skydoves.network.service.TheDiscoverService
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DiscoveryRepositoryTest {

  private lateinit var repository: DiscoverRepository
  private lateinit var client: TheDiscoverClient
  private val service = mock<TheDiscoverService>()
  private val movieDao = mock<MovieDao>()
  private val tvDao = mock<TvDao>()

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @Before
  fun setup() {
    client = TheDiscoverClient(service)
    repository = DiscoverRepository(client, movieDao, tvDao)
  }

  @Test
  fun loadMovieListFromNetworkTest() = runBlocking {
    val loadFromDB = movieDao.getMovieList(1)
    whenever(movieDao.getMovieList(1)).thenReturn(loadFromDB)

    val mockResponse = DiscoverMovieResponse(1, emptyList(), 100, 10)
    whenever(service.fetchDiscoverMovie(1)).thenReturn(getCall(mockResponse))

    val data = repository.loadMovies(1) { }
    verify(movieDao, times(2)).getMovieList(1)

    val observer = mock<Observer<List<Movie>>>()
    data.observeForever(observer)
    val updatedData = mockMovieList()
    whenever(movieDao.getMovieList(1)).thenReturn(updatedData)
    data.postValue(updatedData)
    verify(observer).onChanged(updatedData)

    client.fetchDiscoverMovie(1) {
      when (it) {
        is ApiResponse.Success -> {
          assertEquals(it.data, `is`(mockResponse))
          assertEquals(it.data?.results, `is`(updatedData))
        }
        else -> assertThat(it, instanceOf(ApiResponse.Failure::class.java))
      }
    }
  }

  @Test
  fun loadTvListFromNetworkTest() = runBlocking {
    val loadFromDB = tvDao.getTvList(1)
    whenever(tvDao.getTvList(1)).thenReturn(loadFromDB)

    val mockResponse = DiscoverTvResponse(1, emptyList(), 100, 10)
    whenever(service.fetchDiscoverTv(1)).thenReturn(getCall(mockResponse))

    val data = repository.loadTvs(1) { }
    verify(tvDao, times(2)).getTvList(1)

    val observer = mock<Observer<List<Tv>>>()
    data.observeForever(observer)
    val updatedData = mockTvList()
    whenever(tvDao.getTvList(1)).thenReturn(updatedData)
    data.postValue(updatedData)
    verify(observer).onChanged(updatedData)

    client.fetchDiscoverTv(1) {
      when (it) {
        is ApiResponse.Success -> {
          assertEquals(it.data, `is`(mockResponse))
          assertEquals(it.data?.results, `is`(updatedData))
        }
        else -> assertThat(it, instanceOf(ApiResponse.Failure::class.java))
      }
    }
  }
}
