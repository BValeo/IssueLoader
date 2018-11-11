package me.bvaleo.issueloader.presenter.impl

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.ObservableField
import me.bvaleo.issueloader.model.Issue
import me.bvaleo.issueloader.model.IssueRepository
import me.bvaleo.issueloader.presenter.IMainPresenter
import me.bvaleo.issueloader.util.*
import me.bvaleo.issueloader.view.IMainView

class MainPresenter : IMainPresenter, ChangeTextCallback {
    override val uiState: ObservableField<UIState> = ObservableField(Default)
    private var liveData: MutableLiveData<List<Issue>>? = null

    private var repository: IssueRepository? = null
    private var mView: IMainView? = null


    override fun attachView(view: IMainView) {
        mView = view
        mView!!.setWatcher()

        if(liveData == null) {
            liveData = MutableLiveData()
            liveData?.observe(view as LifecycleOwner, Observer { it?.let { mView!!.setData(it) } })
            repository = IssueRepository(liveData!!, uiState)
        }

    }

    override fun detachView() {
        mView?.removeWatcher()
        repository?.dispose()

        mView = null
        repository = null
    }

    override fun onTextChanged(repo: String) {
        if(repo.isEmpty()) {
            uiState.set(Default)
        } else {
            loadIssue(repo)
        }
    }

    private fun loadIssue(repo: String) {
        uiState.set(Loading)
        repository?.getIssueFromRepo(repo)
    }
}