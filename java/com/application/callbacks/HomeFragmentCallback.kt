package com.application.callbacks

import androidx.recyclerview.widget.RecyclerView

interface HomeFragmentCallback {
    fun getSearchView(): com.google.android.material.search.SearchView
    fun getSearchRecyclerView(): RecyclerView
}