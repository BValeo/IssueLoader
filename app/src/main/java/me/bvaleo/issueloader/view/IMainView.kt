package me.bvaleo.issueloader.view

import android.databinding.ObservableField
import android.support.annotation.StringRes
import me.bvaleo.issueloader.model.Issue
import me.bvaleo.issueloader.util.UIState

interface IMainView {
    val uiState: ObservableField<UIState>
    fun onLoading()
    fun onFetchDataSuccess(items: List<Issue>)
    fun onFetchEmptyData()
    fun onFetchDataError(@StringRes error_msg: Int)
    fun setDefaultView()
}