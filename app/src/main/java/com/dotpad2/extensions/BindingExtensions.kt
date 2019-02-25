package com.dotpad2.extensions

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:tag")
fun setTag(view: View, element:Any) {
    view.tag = element
}