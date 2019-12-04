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

package com.skydoves.network

import com.skydoves.network.service.PeopleService
import java.io.IOException
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class PeopleServiceTest : ApiAbstract<PeopleService>() {

  private lateinit var service: PeopleService

  @Before
  fun initService() {
    this.service = createService(PeopleService::class.java)
  }

  @Throws(IOException::class)
  @Test
  fun fetchPersonListTest() {
    enqueueResponse("/tmdb_people.json")
    this.service.fetchPopularPeople(1).transform {
      when (it) {
        is ApiResponse.Success -> {
          assertThat(it.data?.results?.get(0)?.id, `is`(28782))
          assertThat(it.data?.total_pages, `is`(984))
          assertThat(it.data?.total_results, `is`(19671))
        }
        else -> assertThat(it, CoreMatchers.instanceOf(ApiResponse.Failure::class.java))
      }
    }
  }

  @Throws(IOException::class)
  @Test
  fun fetchPersonDetail() {
    enqueueResponse("tmdb_person.json")
    this.service.fetchPersonDetail(123).transform {
      when (it) {
        is ApiResponse.Success -> {
          assertThat(it.data?.birthday, `is`("1963-12-18"))
          assertThat(it.data?.known_for_department, `is`("Acting"))
          assertThat(it.data?.place_of_birth, `is`("Shawnee, Oklahoma, USA"))
        }
        else -> assertThat(it, CoreMatchers.instanceOf(ApiResponse.Failure::class.java))
      }
    }
  }
}
