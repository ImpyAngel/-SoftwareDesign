/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
interface BaseMap<K, V> {
    fun add(key: K, value: V)
    fun remove(key: K): V?
    operator fun get(key: K): V?
    val size: Int
}