package me.bvaleo.issueloader

import me.bvaleo.issueloader.model.Issue
import me.bvaleo.issueloader.network.ApiProvider
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException

@RunWith(MockitoJUnitRunner::class)
class ApiServiceTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()

        const val testQueryEmpty = ""
        const val testFakeQuery = "repo"
        const val testQuery = "fetch"
    }

    @Test
    fun testEmptyError() {
        val tester = ApiProvider.getService().getIssues(testQueryEmpty).test()

        tester.assertError(HttpException::class.java)
        tester.assertNoValues()
        tester.assertSubscribed()
    }

    @Test
    fun testNotEmptyComplete() {
        val tester = ApiProvider.getService().getIssues(testQuery).test()

        tester.assertComplete()
        tester.assertNoErrors()
        tester.assertNoTimeout()
        tester.assertValue{t: List<Issue> ->  t.size == 30}
    }

    @Test
    fun testFakeDataError() {
        val tester = ApiProvider.getService().getIssues(testFakeQuery).test()

        tester.assertError(HttpException::class.java)
        tester.assertNoValues()
        tester.assertNotComplete()
    }


}