package club.eridani.textsearch

import club.eridani.textsearch.tokenizer.Tokenizer

class Database(
    val tokenizers: Map<Language, Tokenizer>
) {

    private val nodes: MutableMap<String, Node> = hashMapOf()
    private val internalStore = hashMapOf<Node, MutableList<Document>>()


    fun insert(text: String, language: Language = Language.English) {
        val texts = splitText(text, language).map { it.asNode() }
        val document = Document(texts)

        texts.forEach { n ->
            internalStore.getOrPut(n) { mutableListOf() }.add(document)
        }
    }

    private fun String.asNode(): Node {
        return nodes.getOrPut(this) { Node(this) }
    }

    private fun splitText(text: String, language: Language = Language.English): List<String> {
        return tokenizers[language]?.splitText(text) ?: error("No tokenizer for language $language")
    }


    fun search(word: String, language: Language = Language.English): List<Document> {
        return internalStore[word.asNode()] ?: emptyList()
    }
}