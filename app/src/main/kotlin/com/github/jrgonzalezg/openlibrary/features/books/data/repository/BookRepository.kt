/*
 * Copyright (C) 2017 Juan Ramón González González (https://github.com/jrgonzalezg)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jrgonzalezg.openlibrary.features.books.data.repository

import com.github.jrgonzalezg.openlibrary.domain.Result
import com.github.jrgonzalezg.openlibrary.features.books.data.repository.datasource.CloudBookDataSource
import com.github.jrgonzalezg.openlibrary.features.books.data.repository.datasource.LocalBookDataSource
import com.github.jrgonzalezg.openlibrary.features.books.domain.Book
import com.github.jrgonzalezg.openlibrary.features.books.domain.BookError
import com.github.jrgonzalezg.openlibrary.features.books.domain.BookSummariesError
import com.github.jrgonzalezg.openlibrary.features.books.domain.BookSummary
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(private val cloudBookDataSource: CloudBookDataSource,
    private val localBookDataSource: LocalBookDataSource) {
  fun getBook(key: String): Result<BookError, Book> {
    return async(CommonPool) {
      val localBook = localBookDataSource.getBook(key).await()
      if (localBook.isRight()) {
        localBook
      } else {
        val cloudBook = cloudBookDataSource.getBook(key).await()
        if (cloudBook.isRight()) {
          localBookDataSource.insertBook(cloudBook.get())
        }

        cloudBook
      }
    }
  }

  fun getBookSummaries(): Result<BookSummariesError, List<BookSummary>> {
    return async(CommonPool) {
      val localBookSummaries = localBookDataSource.getBookSummaries().await()
      if (localBookSummaries.isRight()) {
        localBookSummaries
      } else {
        val cloudBookSummaries = cloudBookDataSource.getBookSummaries().await()
        if (cloudBookSummaries.isRight()) {
          localBookDataSource.insertBookSummaries(cloudBookSummaries.get())
        }

        cloudBookSummaries
      }
    }
  }
}
