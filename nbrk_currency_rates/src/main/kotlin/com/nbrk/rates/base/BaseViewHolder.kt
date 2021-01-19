package com.nbrk.rates.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
abstract class BaseViewHolder<in D>(itemView: View) : RecyclerView.ViewHolder(itemView) {
  abstract fun onBind(item: D)
}