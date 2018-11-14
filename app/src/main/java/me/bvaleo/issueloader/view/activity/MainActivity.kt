package me.bvaleo.issueloader.view.activity

import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.databinding.ObservableField
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout.VERTICAL
import me.bvaleo.issueloader.R
import me.bvaleo.issueloader.databinding.ActivityMainBinding
import me.bvaleo.issueloader.model.Issue
import me.bvaleo.issueloader.presenter.IMainPresenter
import me.bvaleo.issueloader.presenter.impl.MainPresenter
import me.bvaleo.issueloader.util.*
import me.bvaleo.issueloader.view.IMainView
import me.bvaleo.issueloader.view.adapter.IssueAdapter

class MainActivity : AppCompatActivity(), IMainView, ChangeTextCallback {

    override val uiState: ObservableField<UIState> = ObservableField(Default)

    private lateinit var mPresenter: IMainPresenter
    private lateinit var bind: ActivityMainBinding
    private lateinit var mAdapter: IssueAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPresenter = lastCustomNonConfigurationInstance as? IMainPresenter ?: MainPresenter()
        initView()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return mPresenter
    }

    private fun initView() {
        mAdapter = IssueAdapter(mutableListOf())
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)

        with(bind) {
            issuesList.adapter = mAdapter
            issuesList.layoutManager = LinearLayoutManager(this@MainActivity)
            issuesList.addItemDecoration(DividerItemDecoration(this@MainActivity, VERTICAL))
            state = uiState
            etRepo.addTextChangedListener(EditTextListener(bind.etRepo, this@MainActivity))
        }

        mPresenter.attachView(this)
    }

    override fun onLoading() {
        uiState.set(Loading)
    }

    override fun onFetchDataSuccess(items: List<Issue>) {
        mAdapter.setData(items)
        uiState.set(HasData)
    }

    override fun onFetchEmptyData() {
        uiState.set(NoData)
    }

    override fun onFetchDataError(error_msg: Int) {
        uiState.set(Error(error_msg))

        if(error_msg == R.string.internet_error)
            Snackbar.make(bind.root, "", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry") {
                        mPresenter.loadIssue(bind.etRepo.text.toString())
                    }
                    .show()
        if(error_msg == R.string.not_found && bind.etRepo.text.isEmpty())
            setDefaultView()
    }

    override fun setDefaultView() {
        uiState.set(Default)
    }

    override fun onTextChanged(repo: String) {
        mPresenter.loadIssue(repo)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}
