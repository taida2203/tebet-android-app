package com.tebet.mojual.view.selectfuturedate

import com.tebet.mojual.data.models.Price
import com.tebet.mojual.view.base.BaseActivityNavigator

interface SelectFutureDateNavigator :BaseActivityNavigator{
    fun itemSelected(item: Price)
}
