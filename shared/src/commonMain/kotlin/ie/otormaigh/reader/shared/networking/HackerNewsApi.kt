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

import ie.otormaigh.reader.shared.entity.HackerNewsItem
import io.ktor.client.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json

class HackerNewsApi {
  private val httpClient = HttpClient {
    // TODO: Logging
    install(JsonFeature) {
      val json = Json { ignoreUnknownKeys = true }
      serializer = KotlinxSerializer(json)
    }
  }
  private val cachedItems = mutableListOf<HackerNewsItem>()

  suspend fun getFeedItems(page: Int = 1): List<HackerNewsItem> {
    // TODO: Some better caching/pagination
    if (page <= 1 || cachedItems.isEmpty()) {
      cachedItems.clear()
    } else {
      val start = PAGE_SIZE * page
      val end = start + PAGE_SIZE
      return cachedItems.subList(start, end)
    }

    val ids: List<Int> = httpClient.get("$BASE_URL/topstories.json")
    val items = mutableListOf<HackerNewsItem>()

    ids.take(25).forEach { id ->
      items += getItem(id)
    }
    return items
  }

  private suspend fun getItem(id: Int): HackerNewsItem =
    httpClient.get("$BASE_URL/item/$id.json/")

  companion object {
    private const val BASE_URL = "https://hacker-news.firebaseio.com/v0"
    private const val PAGE_SIZE = 25
  }
}