/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */

class LruCache<K, V>(private val capacity: Int = 10): BaseMap<K, V> {
    override fun get(key: K): V? = core[key]?.value

    var tail: Node<K, V>? = null
    var head: Node<K, V>? = null

    init {
        assert(capacity > 0)
    }

    private val core = mutableMapOf<K, Node<K, V>>()

    override val size get() = core.size

    data class Node<K, V>(val key: K, val value: V) {
        var prev: Node<K, V>? = null
        var next: Node<K, V>? = null
        fun addLink(other: Node<K, V>?) {
            next = other
            other?.prev = this
        }
    }

    private fun achievable(): Boolean {
        var cur = head
        var i = 0
        while (cur != tail && i < capacity) {
            cur = cur?.next
            i++
        }
        return cur == tail
    }

    private fun remove(node: Node<K, V>) {
        val prev = node.prev
        val next = node.next
        prev?.next = next
        next?.prev = prev
        core.remove(node.key)
    }


    override fun add(key: K, value: V) {
        assert(achievable())
        if (core[key] == null) {
            val node = Node(key, value)
            node.addLink(head)
            head = node
            if (size == 0) {
                tail = head
            } else {
                if (size == capacity) {
                    tail = tail?.prev
                    remove(tail!!.next!!)
                }
            }
            core[key] = node
            assert(head != null && tail != null)
            assert(achievable())
        }
    }

    override fun remove(key: K): V? {
        assert(achievable())
        val ans = core[key]
        if (ans == head) {
            head = ans?.next
        }
        if (ans == tail) {
            tail = ans?.prev
        }
        if (ans != null) {
            remove(ans)
        }
        assert(achievable())
        return ans?.value
    }

}

