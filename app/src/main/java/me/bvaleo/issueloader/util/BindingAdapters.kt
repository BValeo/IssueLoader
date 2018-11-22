package me.bvaleo.issueloader.util

import android.databinding.BindingAdapter
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import me.bvaleo.issueloader.R

@BindingAdapter("uiState")
fun onLoading(progressBar: ProgressBar, uiState: UIState) {
    progressBar.visibility = when(uiState) {
        Loading -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("uiState")
fun onHasData(view: View, uiState: UIState) {
    view.visibility = when(uiState) {
        HasData -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("uiState")
fun onDataMessage(view: TextView, uiState: UIState) {
    val state = when(uiState) {
        NoData -> View.VISIBLE to view.context.getString(R.string.no_data)
        NotFound -> View.VISIBLE to view.context.getString(R.string.not_found)
        is Error -> View.VISIBLE to view.context.getString((uiState).errorMsgId)
        else -> View.GONE to view.context.getString(R.string.empty_string)
    }

    view.visibility = state.first
    view.text = state.second
}
