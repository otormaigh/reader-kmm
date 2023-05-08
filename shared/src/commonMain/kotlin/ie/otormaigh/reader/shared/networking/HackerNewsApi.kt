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
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

class HackerNewsApi {
  private val httpClient = HttpClient(CIO) {
    install(Logging) {
      logger = Logger.DEFAULT
      level = LogLevel.HEADERS
    }

    install(ContentNegotiation) {
      json()
    }
  }

  @Throws(Exception::class)
  suspend fun getFeedItems(page: Int = 1): List<HackerNewsItemResponse> {
    val ids: List<Int> = httpClient.get("$BASE_URL/topstories.json").body()
    val start = PAGE_SIZE * (page.takeIf { it >= 1 } ?: 1)
    val end = start + PAGE_SIZE

    return ids
      .subList(start, end)
      .map { id ->
        getItem(id)
      }
  }

  private suspend fun getItem(id: Int): HackerNewsItemResponse =
    httpClient.get("$BASE_URL/item/$id.json/").body()

  companion object {
    private const val BASE_URL = "https://hacker-news.firebaseio.com/v0"
    private const val PAGE_SIZE = 25
  }
}