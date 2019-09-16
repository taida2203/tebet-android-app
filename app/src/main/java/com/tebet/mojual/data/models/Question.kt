package com.tebet.mojual.data.models

data class Question(
    var questionId: Long,
    var code: String?,
    var content: String,
    var answer: String?
)