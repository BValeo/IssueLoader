package me.bvaleo.issueloader.model

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.bvaleo.issueloader.network.ApiProvider
import me.bvaleo.issueloader.presenter.impl.MainPresenter.Constants.DATA_NOT_FOUND
import me.bvaleo.issueloader.presenter.impl.MainPresenter.Constants.HAS_DATA
import me.bvaleo.issueloader.presenter.impl.MainPresenter.Constants.INTERNET_ERROR
import java.util.concurrent.TimeUnit

class IssueRepository(private val liveData: MutableLiveData<Pair<String, List<Issue>>>) {

    private companion object {
        const val TIMEOUT = "timeout"
        const val NOT_FOUND = "404"
        const val UNABLE_HOST = "Unable to resolve host"

        val empty_list = listOf<Issue>()
    }

    private var mService = ApiProvider.getService()
    private val disposable = CompositeDisposable()

    fun getIssueFromRepo(path: String) {
        disposable.clear()
        disposable.add(
                mService.getIssues(path)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .repeatWhen { d -> d.delay(10, TimeUnit.SECONDS) }
                        .subscribe(
                                { liveData.value = HAS_DATA to it.filter { checkIssue(it) } },
                                {
                                    it.message?.let {
                                        if (it.contains(TIMEOUT) || it.contains(UNABLE_HOST)) liveData.value = INTERNET_ERROR to empty_list
                                        if (it.contains(NOT_FOUND)) liveData.value = DATA_NOT_FOUND to empty_list
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