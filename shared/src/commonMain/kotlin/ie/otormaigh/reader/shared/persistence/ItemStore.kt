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

package ie.otormaigh.reader.shared.persistence

import ie.otormaigh.reader.shared.entity.HackerNewsItemResponse
import ie.otormaigh.reader.shared.networking.HackerNewsApi
import java.net.URI

class ItemStore(databaseDriverFactory: DatabaseDriverFactory) {
  private val database by lazy { ReaderDatabase(databaseDriverFactory.createDriver()) }
  private val api by lazy { HackerNewsApi() }

  suspend fun fetchAllItems(): List<HackerNewsItem> {
    val query = database.hackerNewsItemQueries.selectAll()
    if (query.executeAsList().isEmpty()) insertFromResponse(api.getFeedItems())

    return query.executeAsList()
  }

  private fun insertFromResponse(items: List<HackerNewsItemResponse>) {
    items.forEach { item ->
      database.hackerNewsItemQueries.insert(
        HackerNewsItem(
          id = item.id.toString(),
          title = item.title,
          url = item.url,
          urlHost = URI(item.url).host.removePrefix("www."),
          score = item.score
        )
      )
    }
  }
}