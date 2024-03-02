package z3roco01.nekocli.network.serialization

import kotlinx.serialization.Serializable
import java.sql.Timestamp
import java.util.Date

@Serializable
data class SiteInfo(val id: Long, val username: String, val title: String, val updates: Long, val followers: Long, val views: Long, val created_at: Long, val updated_at: Long) {
    override fun toString(): String {
        return "$username info:\n\tid: $id\n\tsite title: $title\n\tfollower count: $followers\n\tviews: $views\n\tcreated at: ${longToDate(created_at)}\n\tupdated at ${longToDate(updated_at)}\n\tupdate count: $updates"
    }

    companion object {
        private fun longToDate(value: Long): Date {
            return Date(Timestamp(value).time)
        }
    }
}