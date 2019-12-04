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

package com.skydoves.mvvm.repository

import androidx.lifecycle.MutableLiveData
import com.skydoves.entity.database.PeopleDao
import com.skydoves.entity.entities.Person
import com.skydoves.entity.response.PersonDetail
import com.skydoves.network.ApiResponse
import com.skydoves.network.client.PeopleClient
import com.skydoves.network.message
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class PeopleRepository @Inject constructor(
  private val peopleClient: PeopleClient,
  private val peopleDao: PeopleDao
) : Repository {

  override var isLoading: Boolean = false

  init {
    Timber.d("Injection PeopleRepository")
  }

  fun loadPeople(page: Int, error: (String) -> Unit): MutableLiveData<List<Person>> {
    val liveData = MutableLiveData<List<Person>>()
    var people = peopleDao.getPeople(page)
    if (people.isEmpty()) {
      this.isLoading = true
      peopleClient.fetchPopularPeople(page) { response ->
        this.isLoading = false
        when (response) {
          is ApiResponse.Success -> {
            response.data?.let { data ->
              people = data.results
              people.forEach { it.page = page }
              liveData.postValue(people)
              peopleDao.insertPeople(people)
            }
          }
          is ApiResponse.Failure.Error -> error(response.message())
          is ApiResponse.Failure.Exception -> error(response.message())
        }
      }
    }
    liveData.postValue(people)
    return liveData
  }

  fun loadPersonDetail(id: Int, error: (String) -> Unit): MutableLiveData<PersonDetail> {
    val liveData = MutableLiveData<PersonDetail>()
    val person = peopleDao.getPerson(id)
    var personDetail = person.personDetail
    if (personDetail == null) {
      this.isLoading = true
      peopleClient.fetchPersonDetail(id) { response ->
        this.isLoading = false
        when (response) {
          is ApiResponse.Success -> {
            response.data?.let { data ->
              personDetail = data
              person.personDetail = personDetail
              liveData.postValue(personDetail)
              peopleDao.updatePerson(person)
            }
          }
          is ApiResponse.Failure.Error -> error(response.message())
          is ApiResponse.Failure.Exception -> error(response.message())
        }
      }
    }
    liveData.postValue(person.personDetail)
    return liveData
  }

  fun getPerson(id: Int) = peopleDao.getPerson(id)
}
