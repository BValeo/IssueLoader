package me.bvaleo.issueloader.presenter

import android.databinding.ObservableField
import me.bvaleo.issueloader.util.UIState
import me.bvaleo.issueloader.view.IMainView

interface IMainPresenter {
    val uiState: ObservableField<UIState>
    fun attachView(view: IMainView)
    fun detachView()
}