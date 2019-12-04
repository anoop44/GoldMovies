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

package com.skydoves.mvvm_coroutines.di

import com.skydoves.mvvm_coroutines.repository.DiscoverRepository
import com.skydoves.mvvm_coroutines.repository.MovieRepository
import com.skydoves.mvvm_coroutines.repository.PeopleRepository
import com.skydoves.mvvm_coroutines.repository.TvRepository
import org.koin.dsl.module

val repositoryModule = module {
  single { DiscoverRepository(get(), get(), get()) }
  single { MovieRepository(get(), get()) }
  single { TvRepository(get(), get()) }
  single { PeopleRepository(get(), get()) }
}
