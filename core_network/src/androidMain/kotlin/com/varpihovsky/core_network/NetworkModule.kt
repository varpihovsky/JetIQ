package com.varpihovsky.core_network

/* JetIQ
 * Copyright © 2021 Vladyslav Podrezenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.varpihovsky.core_network.result.adapter.ResultAdapterFactory
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal actual fun Module.provideApi() {
    single { get<Retrofit>().create(JetIQApi::class.java) }

    single {
        Retrofit
            .Builder()
            .client(get())
            .baseUrl("https://iq.vntu.edu.ua/b04213/curriculum/")
            .addCallAdapterFactory(ResultAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    single {
        OkHttpClient.Builder()
            // Uncomment if http logs are needed
            //.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
    }
}