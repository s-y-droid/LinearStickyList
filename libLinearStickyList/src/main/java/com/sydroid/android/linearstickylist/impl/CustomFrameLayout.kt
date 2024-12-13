/**
 *    Copyright 2024 s-y-droid
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.sydroid.android.linearstickylist.impl

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout

class CustomFrameLayout(context: Context) : FrameLayout(context) {

    private var listener: (() -> Unit)? = null

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    }

    fun setOnLayoutCallback(cb: (() -> Unit)?) {
        listener = cb
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener = null
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            listener?.invoke()
        }
    }
}
