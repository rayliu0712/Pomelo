package com.ray.pomelo

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val client = HttpClient(OkHttp) {
  install(ContentNegotiation) {
    json()
  }
}

@Serializable
data class Plugin(
  val name: String,
  val desc: String,
  val repo: String
)


suspend fun getStore(): List<Plugin> {
  val res = client.get(
    "https://raw.githubusercontent.com/rayliu0712/Pomelo/main/store.json"
  )
  val bodyText = res.bodyAsText()
  return Json.decodeFromString<List<Plugin>>(bodyText)
}
