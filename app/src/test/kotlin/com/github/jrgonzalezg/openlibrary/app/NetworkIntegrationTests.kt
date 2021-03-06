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

package com.github.jrgonzalezg.openlibrary.app

import com.github.jrgonzalezg.openlibrary.IntegrationTests
import io.kotlintest.TestCaseContext

abstract class NetworkIntegrationTests : IntegrationTests() {
  lateinit var networkComponent: IntegrationTestNetworkComponent

  val networkInterceptor: (TestCaseContext, () -> Unit) -> Unit = { context, testCase ->
    networkComponent = DaggerIntegrationTestNetworkComponent.builder().integrationTestNetworkModule(
        IntegrationTestNetworkModule()).build()

    testCase()
  }

  override val defaultTestCaseConfig = super.defaultTestCaseConfig.copy(
      interceptors = super.defaultTestCaseConfig.interceptors + networkInterceptor)
}
