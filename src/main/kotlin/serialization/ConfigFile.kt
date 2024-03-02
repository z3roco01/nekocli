package z3roco01.nekocli.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy

@Serializable
data class ConfigFile(val apiKey: String)