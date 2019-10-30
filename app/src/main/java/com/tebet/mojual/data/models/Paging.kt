package com.tebet.mojual.data.models

data class Paging<T>(var total: Int = 0, var data: List<T> = ArrayList())
