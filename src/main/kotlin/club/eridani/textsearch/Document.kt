package club.eridani.textsearch

class Document(val nodes: List<Node>) {

    fun asString(): String {
        return nodes.joinToString(" ") { it.word }
    }

}