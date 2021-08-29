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

package ie.otormaigh.reader

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ie.otormaigh.reader.shared.persistence.HackerNewsItem

@Composable
fun StoryListView(
  listItems: List<HackerNewsItem>
) {
  LazyColumn(
    modifier = Modifier.padding(
      start = 12.dp,
      end = 12.dp
    )
  ) {
    items(listItems) { listItem ->
      StoryListItemView(listItem)
    }
  }
}

@Composable
fun StoryListItemView(listItem: HackerNewsItem) {
  Spacer(modifier = Modifier.padding(bottom = 6.dp))

  Row(
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = listItem.score.toString(),
      style = MaterialTheme.typography.body2,
      fontWeight = FontWeight.Bold,
      modifier = Modifier
        .padding(end = 8.dp)
        .width(28.dp)
        .align(Alignment.CenterVertically)
    )

    Column {
      Text(
        text = listItem.title,
        style = MaterialTheme.typography.body1,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = listItem.urlHost,
        style = MaterialTheme.typography.body2
      )
    }
  }

  Spacer(modifier = Modifier.padding(bottom = 6.dp))
  Divider()
}

@Preview
@Composable
fun StoryListItemView_Preview() {
  StoryListItemView(
    HackerNewsItem(
      id = "1",
      title = "This is a title",
      url = "https://news.ycombinator.com",
      urlHost = "https://news.ycombinator.com",
      score = 42
    )
  )
}