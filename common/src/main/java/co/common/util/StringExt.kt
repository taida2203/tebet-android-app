package co.common.util

fun String.isValidEmail() =
        this.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(this.trim { it <= ' ' }).matches()