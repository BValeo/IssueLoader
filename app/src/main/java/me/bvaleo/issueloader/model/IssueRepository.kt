package me.bvaleo.issueloader.model

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.bvaleo.issueloader.network.ApiService
import me.bvaleo.issueloader.presenter.impl.MainPresenter.Constants.DATA_NOT_FOUND
import me.bvaleo.issueloader.presenter.impl.MainPresenter.Constants.HAS_DATA
import me.bvaleo.issueloader.presenter.impl.MainPresenter.Constants.INTERNET_ERROR
import java.util.concurrent.TimeUnit

class IssueRepository(private val mService: ApiService) {

    private companion object {
        const val TIMEOUT = "timeout"
        const val NOT_FOUND = "404"
        const val UNABLE_HOST = "Unable to resolve host"

        val empty_list = listOf<Issue>()
    }

    private val disposable = CompositeDisposable()
    val callback: MutableLiveData<Pair<String, List<Issue>>> = MutableLiveData()

    fun getIssueFromRepo(path: String) {
        disposable.clear()
        disposable.add(
                mService.getIssues(path)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .repeatWhen { d -> d.delay(10, TimeUnit.SECONDS) }
                        .subscribe(
                                {
                                    callback.value = HAS_DATA to it.filter { issue -> checkIssue(issue) } },
                                {
                                    Log.e("ErrorLoadIssue", it.message, it)
                                    it.message?.let { msg ->
                                        if (msg.contains(TIMEOUT) || msg.contains(UNABLE_HOST)) callback.value = INTERNET_ERROR to empty_list
                                        if (msg.contains(NOT_FOUND)) callback.value = DATA_NOT_FOUND to empty_list
                                    }
                                }
                        )
        )
    }

    private fun checkIssue(issue: Issue) : Boolean {
        return with(issue) {
            title.isNotEmpty() && state.isNotEmpty() && comments > -1 && user.avatarUrl.isNotEmpty()
        }
    }

    fun dispose() = disposable.dispose()
}