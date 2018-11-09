package me.bvaleo.issueloader.view

import me.bvaleo.issueloader.model.Issue

interface IMainView {
    fun setWatcher()
    fun removeWatcher()
    fun setData(items: List<Issue>)
}