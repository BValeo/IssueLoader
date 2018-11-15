package me.bvaleo.issueloader

import me.bvaleo.issueloader.presenter.impl.MainPresenter
import me.bvaleo.issueloader.view.IMainView
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner
import kotlin.NullPointerException as lol

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    val presenter = MainPresenter()
    val view = Mockito.mock(IMainView::class.java)

    val repo = "repo"
    val empty = ""

    @Before
    fun before() {
        presenter.attachView(view)
    }

    @Test
    fun testLoad() {
        presenter.loadIssue(repo)
        Mockito.verify(view).onLoading()
    }

    @Test
    fun testEmptyLoad() {
        presenter.loadIssue(empty)
        Mockito.verify(view).setDefaultView()
    }


    @Test()
    @Throws(kotlin.NullPointerException::class)
    fun testDetachViewNotEmptyQuery() {
        presenter.detachView()

        presenter.loadIssue(repo)
    }

    @Test()
    @Throws(kotlin.NullPointerException::class)
    fun testDetachViewEmptyQuery() {
        presenter.detachView()

        presenter.loadIssue(empty)
    }
}