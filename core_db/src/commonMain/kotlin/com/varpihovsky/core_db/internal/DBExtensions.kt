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

import com.varpihovsky.repo_data.Listable
import com.varpihovsky.repo_data.Single
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
 * Puts [model] into database.
 *
 * @param policy if it is last, function set new id to the [model]. If it is as is, creates new or
 * replaces existing model by id.
 *
 * @param model model to put, always must extend [Listable] interface.
 */
internal inline fun <reified T : Listable> DB.putListable(
    model: T
) {
    putMetadata(model)
}

/**
 * Deletes model from database.
 *
 * @param model model to delete.
 */
internal inline fun <reified T : Listable> DB.delete(model: T) = delete(keyById<T>(model.id))

/** Deletes all instances with type [T] from database. */
internal inline fun <reified T : Listable> DB.deleteAll() {
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