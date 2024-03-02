package z3roco01.nekocli.network

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class FormParameters() {
    private val parameters = HashMap<String, String>()

    fun set(key: String, value: String) {
        parameters.set(key, value)
    }

    fun get(key: String): String? {
        return parameters.get(key)
    }

    fun encode(): String {
        var full = ""
        var count = parameters.size
        for(parameter in parameters){
            full += parameter.toString()
            count--
            if(count > 0)
                full += "&"
        }


        return encode(full)
    }

    fun multipartFormData(boundary: String): String {
        var full = ""
        var count = parameters.size
        for(parameter in parameters) {
            full += "--$boundary\r\n"
            full += "Content-Disposition: form-data; name=\"${parameter.key}\"\r\n\r\n"
            full += parameter.value + "\r\n"
            count--
        }

        full += "--$boundary--\r\n"

        return full
    }

    companion object {
        fun multipartFormDataBoundary(): String {
            return "--------------------------${(0..Long.MAX_VALUE).random()}"
        }

        private fun encode(value: String): String {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString()).replace("%3D", "=").replace("%26", "&")
        }
    }
}