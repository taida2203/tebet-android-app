package co.sdk.auth.network

import android.text.TextUtils
import android.util.Base64

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.HashMap
import java.util.TreeSet
import java.util.UUID

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import co.sdk.auth.AuthSdk
import timber.log.Timber

object SignatureGenerator {
    private val MAX_LENGTH_RAND_STRING = 32
    fun generate(method: String, path: String, params: String): String {
        // 1. Define Consumer-Key and Consumer-Secret (a random string).
        val map = HashMap<String, String>()

        // 2. Define the OAuth parameters
        val oauth_consumer_key = AuthSdk.instance.consumerKey
        val oauth_timestamp = System.currentTimeMillis() / 1000
        val oauth_signature_method = "HMAC-SHA1"
        val oauth_version = "1.0"
        val oauth_nonce = random()

        map["oauth_consumer_key"] = oauth_consumer_key
        map["oauth_timestamp"] = oauth_timestamp.toString()
        map["oauth_signature_method"] = oauth_signature_method
        map["oauth_version"] = oauth_version
        map["oauth_nonce"] = oauth_nonce

        //3. Collect all params from OAuth params and query params
        if (!TextUtils.isEmpty(params) && params.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size > 0) {
            for (paramWithValue in params.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                var param = ""
                var value = ""
                val paramsSplited = paramWithValue.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (paramsSplited.size >= 1) {
                    param = paramsSplited[0]
                    if (paramsSplited.size > 1) {
                        value = paramsSplited[1]
                    }
                }
                if (!TextUtils.isEmpty(param)) {
                    map[param] = value
                }
            }
        }

        //4. UTF‌-8 encode, then percentage encode and sort params lexicographically
        val keys = TreeSet(map.keys)

        //5. Concat all params into single Parameter String via format: key=value&key1=value1 (even if value is empty).
        var parameterString = StringBuilder()
        for (key in keys) {
            val value = map[key]
            parameterString.append(key).append("=").append(value).append("&")
        }
        parameterString = StringBuilder(parameterString.substring(0, parameterString.length - 1))

        //6. Create Signature Base String via following format
        var signatureBaseString = ""
        try {
            signatureBaseString = method +
                    "&" + URLEncoder.encode(path, "UTF-8") +
                    "&" + URLEncoder.encode(parameterString.toString(), "UTF-8")
        } catch (ignored: UnsupportedEncodingException) {
            Timber.e(ignored)
        }

        try {
            //7. Sign message with the HMAC-SHA1 algorithm using Consumer-Secret & Token-Secret
            val TOKEN_SECRET = AuthSdk.instance.consumerSecret + "&"
            val signature = URLEncoder.encode(hmacSha1(signatureBaseString, TOKEN_SECRET), "UTF-8")

            //8. Append authorization header to request
            return "OAuth oauth_consumer_key=" + oauth_consumer_key +
                    ", oauth_nonce=" + oauth_nonce +
                    ", oauth_timestamp=" + oauth_timestamp +
                    ", oauth_version=" + oauth_version +
                    ", oauth_signature_method=" + oauth_signature_method +
                    ", oauth_signature=" + signature

        } catch (e: UnsupportedEncodingException) {
            Timber.e(e)
        } catch (e: NoSuchAlgorithmException) {
            Timber.e(e)
        } catch (e: InvalidKeyException) {
            Timber.e(e)
        }

        return ""
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    private fun hmacSha1(value: String, key: String): String {
        val type = "HmacSHA1"
        val secret = SecretKeySpec(key.toByteArray(), type)
        val mac = Mac.getInstance(type)
        mac.init(secret)
        val bytes = mac.doFinal(value.toByteArray())
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    private fun random(): String {
        //        Random generator = new Random();
        //        StringBuilder randomStringBuilder = new StringBuilder();
        //        int randomLength = generator.nextInt(MAX_LENGTH_RAND_STRING);
        //        char tempChar;
        //        for (int i = 0; i < randomLength; i++){
        //            tempChar = (char) (generator.nextInt(96) + 32);
        //            randomStringBuilder.append(tempChar);
        //        }
        //        return randomStringBuilder.toString();
        val randomString = UUID.randomUUID()
        var result = "random"
        if (randomString != null) {
            result = randomString.toString().replace("-".toRegex(), "")
        }
        return result
    }

}
