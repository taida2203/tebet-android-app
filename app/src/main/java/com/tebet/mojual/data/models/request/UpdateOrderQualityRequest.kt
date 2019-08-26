package com.tebet.mojual.data.models.request

import com.tebet.mojual.data.models.Quality

data class UpdateOrderQualityRequest(var qualityList: List<Quality>)