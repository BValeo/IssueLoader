package me.bvaleo.issueloader.presenter

import me.bvaleo.issueloader.view.IMainView

interface IMainPresenter {
    fun attachView(view: IMainView)
    fun detachView()
    fun loadIssue(repo: String)
}