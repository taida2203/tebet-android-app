package com.tebet.mojual.data.models

import java.io.Serializable

data class Question(
    var questionId: Long,
    var code: String?,
    var content: String,
    var answer: String?
): Serializable