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
package com.varpihovsky.core_db.internal

import com.varpihovsky.core.util.add
import com.varpihovsky.core.util.remove
import com.varpihovsky.core.util.replaceAndReturn
import com.varpihovsky.repo_data.Listable
import com.varpihovsky.repo_data.Single
import com.varpihovsky.repo_data.SingleHolder
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import org.kodein.db.*
import org.kodein.db.model.orm.Metadata

/**
 * Returns instance of [Metadata] by given [key].
 *
 * @param key key of requested instance.
 */
internal inline fun <reified M : Metadata> DB.get(key: Key<M>) = get(M::class, key)

/**
 * Returns single with type [M].
 *
 * @return [Single]
 */
internal inline fun <reified M : Single> DB.get() = get<M>(keyById())

/**
 * Creates new or replaces existing one single with given [single].
 *
 * @param single should be extended by [Single].
 *
 * @return [Unit]
 */
internal inline fun <reified M : Single> DB.putSingle(single: M) = put(keyById(), single)

/**
 * Creates new or replaces existing one model with given [model].
 *
 * @param model should be extended by [Metadata].
 *
 * @return [Unit]
 */
internal inline fun <reified M : Metadata> DB.putMetadata(model: M) = put(keyById(model.id), model)

/**
 * Deletes data about given [model].
 *
 * @param model should be extended by [Metadata].
 *
 * @return [Unit]
 */
internal inline fun <reified M : Metadata> DB.delete(model: M) =
    delete(M::class, keyById(M::class, model.id))

/**
 * Deletes data about single, given in generic.
 *
 * @return [Unit]
 */
internal inline fun <reified M : Single> DB.delete() = delete(keyById<M>())

/**
 * Returns key of Single given in generic.
 *
 * @return [Key] of [Single]
 */
internal inline fun <reified M : Single> DB.keyById() = keyById<M>(Single.identifier)

/**
 * Returns flow of list with type of given [T] generic.
 *
 * It is flow of list of flows under the hood, so every change of list or element returns new list.
 */
@OptIn(FlowPreview::class)
internal inline fun <reified K : SingleHolder<T>, reified T : Listable<K>> DB.listFlow(): Flow<List<T>> {
    val flow = flowOf<K>(keyById())
    val f = flow.mapNotNull { it?.list?.map { key -> flowOf(key) } }.map { instantCombine(it) }
    return f.flattenConcat().map { it.filterNotNull() }
}

/**
 * Converts iterable of flow into flow of list.
 */
private inline fun <reified T> instantCombine(
    flows: Iterable<Flow<T>>
): Flow<List<T>> = combine(flows.map { flow ->
    flow.map {
        @Suppress("USELESS_CAST") // Required for onStart(null)
        Holder(it) as Holder<T>?
    }.onStart { emit(null) }
}) { holders -> holders.filterNotNull().map { holder -> holder.value } }

/**
 * Class used to hold some data. Used in the [instantCombine] function.
 */
private class Holder<T>(val value: T)

/**
 * Puts [model] into database and [SingleHolder], so both list and value will be updated.
 *
 * @param policy if it is last, function set new id to the [model]. If it is as is, creates new or
 * replaces existing model by id.
 *
 * @param model model to put, always must extend [Listable] interface.
 *
 * @param holderFactory used to create new factory if it isn't exist.
 */
internal inline fun <reified K : SingleHolder<T>, reified T : Listable<K>> DB.put(
    policy: PutPolicy = PutPolicy.LAST,
    model: T,
    holderFactory: () -> K
) {
    val holder = get() ?: holderFactory()

    val newModel = when (policy) {
        PutPolicy.LAST -> model.with(get(holder.list.last())?.id ?: 0)
        PutPolicy.AS_IS -> model
    }

    val oldKey = keyById<T>(model.id)
    val key = put(newModel)
    val newHolder = when (policy) {
        PutPolicy.LAST -> holder.with(holder.list.add(key) as List<Key<T>>)
        PutPolicy.AS_IS -> holder.with(holder.list.replaceAndReturn(oldKey, key) as List<Key<T>>)
    }

    putSingle(newHolder)
}

internal enum class PutPolicy {
    LAST, AS_IS
}

/**
 * Deletes model from both database and holder.
 *
 * @param model model to delete.
 */
internal inline fun <reified K : SingleHolder<T>, reified T : Listable<K>> DB.delete(
    model: T
) {
    val holder = get<K>() ?: return

    val key = keyById<T>(model.id)

    delete<T>(model)
    val newHolder = holder.with(holder.list.remove(key))

    putSingle(newHolder)
}

/** Deletes all instances with type [T] both from holder [K] and database. */
internal inline fun <reified K : SingleHolder<T>, reified T : Listable<K>> DB.deleteAll() {
    val holder = get<K>() ?: return

    putSingle(holder.with(listOf()))

    val cursor = find<T>().all()

    deleteAll(cursor)

    cursor.close()
}

internal inline fun <reified M : Any> DB.allList(): List<M> {
    val cursor = find<M>().all()
    val list = cursor.asModelSequence().toList()
    cursor.close()
    return list
}