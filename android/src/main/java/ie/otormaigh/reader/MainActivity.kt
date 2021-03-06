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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import ie.otormaigh.reader.databinding.ActivityMainBinding
import ie.otormaigh.reader.shared.Greeting
import ie.otormaigh.reader.shared.persistence.DatabaseDriverFactory
import ie.otormaigh.reader.shared.persistence.ItemStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  private val itemStore by lazy { ItemStore(DatabaseDriverFactory(this)) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    CoroutineScope(Dispatchers.IO).launch {
      val items = itemStore.fetchAllItems()
      withContext(Dispatchers.Main) {
        binding.contentRoot.setContent {
          MaterialTheme {
            StoryListView(items)
          }
        }
      }
    }
  }
}
