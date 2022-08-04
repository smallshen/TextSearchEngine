package club.eridani.textsearch

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap

data class Node(val word: String) {
//    val children: MutableMap<String, Node> = hashMapOf()

    val distanceCache = Object2IntOpenHashMap<Node>()
}