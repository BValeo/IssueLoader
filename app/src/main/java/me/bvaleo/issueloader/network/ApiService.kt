package me.bvaleo.issueloader.network

import io.reactivex.Single
import me.bvaleo.issueloader.BuildConfig
import me.bvaleo.issueloader.model.Issue
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/repos/github/{repo}/issues?client_id=${BuildConfig.ClientId}&client_secret=${BuildConfig.SecretId}&state=all")
    fun getIssues(@Path("repo") string: String) : Single<List<Issue>>
}