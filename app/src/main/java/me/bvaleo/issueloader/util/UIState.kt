package me.bvaleo.issueloader.util

import android.support.annotation.StringRes

sealed class UIState

object Default : UIState()

object Loading : UIState()

object HasData : UIState()

object NoData : UIState()

object NotFound : UIState()

class Error(@StringRes val errorMsigId: Int = 0) : UIState()