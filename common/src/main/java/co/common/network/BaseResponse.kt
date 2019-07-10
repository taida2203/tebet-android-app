package co.common.network

/**
 * Created by kal on 6/9/2017.
 */

@Deprecated("")
open class BaseResponse {

    var isStatus: Boolean = false
    var message: String? = null
    var data: List<*>? = null
    var rule: Int = 0
    var startDate: String? = null
}
