package ro.pub.cs.systems.eim.practicaltest02v5



class ExpiringHashMap<K, V>(private val defaultExpiryTime: Long) {
    private val map = HashMap<K, Pair<V, Long>>()

    @Synchronized
    fun put(key: K, value: V, expiryTime: Long = defaultExpiryTime) {
        val absoluteExpiryTime = System.currentTimeMillis() + expiryTime
        map[key] = Pair(value, absoluteExpiryTime)
        cleanup()
    }

    @Synchronized
    fun get(key: K): V? {
        cleanup()
        val entry = map[key]
        return if (entry != null && entry.second > System.currentTimeMillis()) {
            entry.first
        } else {
            map.remove(key)
            null
        }
    }

    @Synchronized
    fun remove(key: K) {
        map.remove(key)
    }

    @Synchronized
    fun size(): Int {
        cleanup()
        return map.size
    }

    @Synchronized
    fun isEmpty(): Boolean {
        cleanup()
        return map.isEmpty()
    }

    private fun cleanup() {
        val currentTime = System.currentTimeMillis()
        val iterator = map.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value.second <= currentTime) {
                iterator.remove()
            }
        }
    }
}