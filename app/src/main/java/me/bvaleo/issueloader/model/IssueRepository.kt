package me.bvaleo.issueloader.model

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.bvaleo.issueloader.R
import me.bvaleo.issueloader.network.ApiProvider
import me.bvaleo.issueloader.util.*
import java.util.concurrent.TimeUnit

class IssueRepository(private val liveData: MutableLiveData<List<Issue>>, private val uiState: ObservableField<UIState>) {

    private companion object {
        const val TIMEOUT = "timeout"
        const val NOT_FOUND = "404"
        const val UNABLE_HOST = "Unable to resolve host"
    }

    private var mService = ApiProvider.getService()
    private val disposable = CompositeDisposable()

    fun getIssueFromRepo(path: String) {
        disposable.clear()
        disposable.add(
                mService.getIssues(path)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .repeatWhen { objectObservable -> objectObservable.delay(10, TimeUnit.SECONDS) }
                        .subscribe(
                                {
                                    if (it.isEmpty()) {
                                        uiState.set(NoData)
                                    } else {
                                        Log.d("getDataIssues", "Call this method")
                                        liveData.value = it.filter { checkIssue(it) }
                                        uiState.set(HasData)
                                    }
                                },
                                {
                                    it.message?.let {
                                        Log.d("getIssue", it)
                                        if (it.contains(TIMEOUT) || it.contains(UNABLE_HOST)) {
                                            uiState.set(Error(R.string.internet_error))
                                            Log.d("getIssue", it)
                                        }
                                        if (it.contains(NOT_FOUND))
                                            uiState.set(NotFound)
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