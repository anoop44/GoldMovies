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

package com.skydoves.common_ui.viewholders

import android.view.View
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import com.skydoves.common_ui.databinding.ItemReviewBinding
import com.skydoves.entity.Review

/** ReviewListViewHolder is a viewHolder class for binding a [Review] item. */
class ReviewListViewHolder(val view: View) : BaseViewHolder(view) {

  private lateinit var review: Review
  private val binding by bindings<ItemReviewBinding>(view)

  override fun bindData(data: Any) {
    if (data is Review) {
      review = data
      binding.apply {
        review = data
        executePendingBindings()
      }
    }
  }

  override fun onClick(v: View?) = Unit

  override fun onLongClick(v: View?) = false
}
