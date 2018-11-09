package me.bvaleo.issueloader.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Issue(@SerializedName("html_url")
                 @Expose
                 var htmlUrl: String,
                 @SerializedName("id")
                 @Expose
                 var id: Int,
                 @SerializedName("number")
                 @Expose
                 var number: Int,
                 @SerializedName("title")
                 @Expose
                 var title: String,
                 @SerializedName("user")
                 @Expose
                 var user: User,
                 @SerializedName("state")
                 @Expose
                 var state: String,
                 @SerializedName("comments")
                 @Expose
                 var comments: Int,
                 @SerializedName("created_at")
                 @Expose
                 var createdAt: String)