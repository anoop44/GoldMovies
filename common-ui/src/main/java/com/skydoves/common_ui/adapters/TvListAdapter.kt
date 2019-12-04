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

package com.skydoves.common_ui.adapters

import android.view.View
import com.skydoves.baserecyclerviewadapter.BaseAdapter
import com.skydoves.baserecyclerviewadapter.SectionRow
import com.skydoves.common_ui.R
import com.skydoves.common_ui.viewholders.TvListViewHolder
import com.skydoves.entity.entities.Tv

/** TvFavouriteListAdapter is an adapter class for binding [Tv] items. */
class TvListAdapter(
  private val delegate: TvListViewHolder.Delegate
) : BaseAdapter() {

  init {
    addSection(ArrayList<Tv>())
  }

  fun addTvList(tvs: List<Tv>) {
    val section = sections()[0]
    section.addAll(tvs)
    notifyItemRangeInserted(section.size - tvs.size + 1, tvs.size)
  }

  override fun layout(sectionRow: SectionRow) = R.layout.item_tv

  override fun viewHolder(layout: Int, view: View) = TvListViewHolder(view, delegate)
}
