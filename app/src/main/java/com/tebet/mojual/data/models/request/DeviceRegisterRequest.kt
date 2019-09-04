package com.tebet.mojual.data.models.request

data class DeviceRegisterRequest(var deviceToken: String?, var deviceType: String? = "MOBILE")