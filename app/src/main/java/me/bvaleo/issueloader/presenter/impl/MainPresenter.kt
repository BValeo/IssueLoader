package me.bvaleo.issueloader.presenter.impl

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import me.bvaleo.issueloader.R
import me.bvaleo.issueloader.model.Issue
import me.bvaleo.issueloader.model.IssueRepository
import me.bvaleo.issueloader.network.ApiProvider
import me.bvaleo.issueloader.presenter.IMainPresenter
import me.bvaleo.issueloader.view.IMainView

class MainPresenter : IMainPresenter {

    companion object Constants{
        const val HAS_DATA = "HasData"
        const val INTERNET_ERROR = "InternetError"
        const val DATA_NOT_FOUND = "DataNotFound"
    }

    private var repository: IssueRepository? = null
    private var mView: IMainView? = null

    override fun attachView(view: IMainView) {
        mView = view
        repository = IssueRepository(ApiProvider.getService()).apply {
            callback.observeForever(observer)
        }
    }

    override fun detachView() {
        repository?.dispose()

        repository = null
        mView = null
    }

    override fun loadIssue(repo: String) {
        if (repo.isEmpty()) {
            mView?.setDefaultView()
        } else {
            mView?.onLoading()
            repository?.getIssueFromRepo(repo)
        }
    }

    private val observer = Observer<Pair<String, List<Issue>>> { response ->
        response?.let {
            when (it.first) {
                HAS_DATA -> if(it.second.isNotEmpty()) mView?.onFetchDataSuccess(it.second) else mView?.onFetchEmptyData()
                INTERNET_ERROR -> mView?.onFetchDataError(R.string.internet_error)
                DATA_NOT_FOUND -> mView?.onFetchDataError(R.string.not_found)
                else -> mView?.setDefaultView()
            }
        }
    }
}