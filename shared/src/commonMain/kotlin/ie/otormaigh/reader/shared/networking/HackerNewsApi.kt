/*
 * Copyright 2021 Elliot Tormey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ie.otormaigh.reader.shared.networking

import ie.otormaigh.reader.shared.entity.HackerNewsItemResponse
import io.ktor.client.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json
import kotlin.native.concurrent.ThreadLocal

class HackerNewsApi {
  private val httpClient = HttpClient {
    // TODO: Logging
    install(JsonFeature) {
      val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
      }
      serializer = KotlinxSerializer(json)
    }
  }

  @Throws(Exception::class)
  suspend fun getFeedItems(page: Int = 1): List<HackerNewsItemResponse> {
    val ids: List<Int> = httpClient.get("$BASE_URL/topstories.json")
    val start = PAGE_SIZE * (page.takeIf { it >= 1 } ?: 1)
    val end = start + PAGE_SIZE

    return ids
      .subList(start, end)
      .mapNotNull { id ->
        try {
          getItem(id)
        } catch (e: Exception) {
          null
        }
      }
  }

  private suspend fun getItem(id: Int): HackerNewsItemResponse =
    httpClient.get("$BASE_URL/item/$id.json/")

  companion object {
    private const val BASE_URL = "https://hacker-news.firebaseio.com/v0"
    private const val PAGE_SIZE = 25
  }
}