package z3roco01.nekocli.network

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import z3roco01.nekocli.network.serialization.SiteInfo
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

class NekowebApiConnection(Key: String) {
    val authKey = Key

    fun getInfo(name: String): SiteInfo {
        return get<SiteInfo>(SITE_INFO_ENDPOINT + name)
    }

    fun create(path: String, isFolder: Boolean): String {
        val parameters = FormParameters()
        parameters.set("pathname", path)
        parameters.set("isFolder", isFolder.toString())

        postUrlEncoded(FILES_BASE + CREATE_ENDPOINT, parameters)

        return "created ${if(isFolder) "folder" else "file"} $path !! :D"
    }

    fun delete(path: String): String {
        val parameters = FormParameters()
        parameters.set("pathname", path)

        postUrlEncoded(FILES_BASE + DELETE_ENDPOINT, parameters)

        return "deleted $path 3:"
    }

    fun rename(path: String, newpath: String): String {
        val parameters = FormParameters()
        parameters.set("pathname", path)
        parameters.set("newpathname", newpath)

        postUrlEncoded(FILES_BASE + RENAME_ENDPOINT, parameters)

        return "renamed $path to $newpath !! :3"
    }

    fun edit(path: String, content: String): String {
        val parameters = FormParameters()
        parameters.set("pathname", path)
        parameters.set("content", content)

        postMultipartFormData(FILES_BASE + EDIT_ENDPOINT, parameters)

        return "edited $path !!! :p"
    }

    fun readFolder(path: String): JsonArray {
        return get<JsonArray>(FILES_BASE + READ_FOLDER_ENDPOINT + "?pathname=" + path)
    }

    private fun postUrlEncoded(path: String, parameters: FormParameters): String {
        return post(path, "application/x-www-form-urlencoded", parameters.encode().encodeToByteArray())
    }

    private fun postMultipartFormData(path: String, parameters: FormParameters): String {
        val boundary = FormParameters.multipartFormDataBoundary()
        return post(path, "multipart/form-data; boundary=$boundary", parameters.multipartFormData(boundary).encodeToByteArray())
    }

    private inline fun <reified T>get(path: String): T {
        var data = ""

        with(URL(API_BASE + path).openConnection() as HttpURLConnection) {
            requestMethod = "GET"

            setRequestProperty("Authorization", authKey)

            val reader: BufferedReader = inputStream.bufferedReader()
            var line: String? = reader.readLine()
            while (line != null) {
                data += line
                line = reader.readLine()
            }
            reader.close()
        }

        return Json.decodeFromString<T>(data)
    }


    private fun post(path: String, contentType: String, body: ByteArray): String{
        var data = ""

        with(URL(API_BASE + path).openConnection() as HttpURLConnection) {
            doOutput = true
            requestMethod = "POST"

            setRequestProperty("Authorization", authKey)
            setRequestProperty("Content-Type", contentType)

            outputStream.write(body)

            val reader: BufferedReader = inputStream.bufferedReader()
            var line: String? = reader.readLine()
            while (line != null) {
                data += line
                line = reader.readLine()
            }
            reader.close()
        }

        return data
    }

    private companion object {
        val API_BASE             = "https://nekoweb.org/api/"

        val SITE_INFO_ENDPOINT   = "site/info/"

        val FILES_BASE           = "files/"
        val CREATE_ENDPOINT      = "create/"
        val UPLOAD_ENDPOINT      = "upload/"
        val DELETE_ENDPOINT      = "delete/"
        val RENAME_ENDPOINT      = "rename/"
        val EDIT_ENDPOINT        = "edit/"
        val READ_FOLDER_ENDPOINT = "readfolder"
    }
}