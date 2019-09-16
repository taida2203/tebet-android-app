package com.tebet.mojual.data.models

import java.io.Serializable

class ProfileSetting(
    var profileSettingId: Long? = null,
    var basePrice: Double? = null,
    var qcType: String? = null,
    var deliveryBonus: Double? = null,
    var volumeBonusPerKg: Double? = null,
    var minPriceIncrease: Double? = null,
    var maxPriceIncrease: Double? = null,
    var rate: Float = 0.0f,
    var iotDefaultDensity: Double? = null,
    var note: String? = null
) : Serializable