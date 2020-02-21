package com.tebet.mojual.data.models.enumeration


/**
 * Created by TaiDA
 */
enum class ContainerOrderType {
    OTHER,
    JERRYCAN,
    DRUM;


    companion object {
        @JvmStatic
        fun getByName(name: String): ContainerOrderType? {
            return ContainerOrderType.values().firstOrNull {
                it.name.equals(name, ignoreCase = true)
            }
        }
    }
}
