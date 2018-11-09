package me.bvaleo.issueloader.view.activity

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout.VERTICAL
import me.bvaleo.issueloader.R
import me.bvaleo.issueloader.databinding.ActivityMainBinding
import me.bvaleo.issueloader.model.Issue
import me.bvaleo.issueloader.presenter.IMainPresenter
import me.bvaleo.issueloader.presenter.impl.MainPresenter
import me.bvaleo.issueloader.util.ChangeTextCallback
import me.bvaleo.issueloader.util.EditTextListener
import me.bvaleo.issueloader.view.IMainView
import me.bvaleo.issueloader.view.adapter.IssueAdapter

class MainActivity : AppCompatActivity(), IMainView {


    private lateinit var mPresenter: IMainPresenter
    private lateinit var bind: ActivityMainBinding
    private lateinit var mAdapter: IssueAdapter
    private lateinit var callback: EditTextListener

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
        bind.issuesList.adapter = mAdapter
        bind.issuesList.layoutManager = LinearLayoutManager(this)
        bind.issuesList.addItemDecoration(DividerItemDecoration(this, VERTICAL))
        bind.presenter = mPresenter as MainPresenter
        callback = EditTextListener(bind.etRepo, mPresenter as ChangeTextCallback)

        mPresenter.attachView(this@MainActivity)
    }

    override fun setWatcher() {
        bind.etRepo.addTextChangedListener(callback)
    }

    override fun removeWatcher() {
        bind.etRepo.removeTextChangedListener(callback)
    }

    override fun setData(items: List<Issue>) {
        mAdapter.setData(items)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}
