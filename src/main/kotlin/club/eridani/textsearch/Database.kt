package club.eridani.textsearch

import club.eridani.textsearch.levenshtein.levenshteinDistance
import club.eridani.textsearch.tokenizer.Tokenizer

class Database(
    private val tokenizers: Map<Language, Tokenizer>,
    private val toleranceLevel: Int,
) {

    private val nodes: MutableMap<String, Node> = hashMapOf()
    private val internalStore = hashMapOf<Node, MutableList<Document>>()
    private val sortedList = mutableSetOf<Node>()

    init {
        // lol i'm lazy
        sortedList.add("are".asNode())
    }


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


    fun search(word: String, language: Language = Language.English, typoTolerance: Int): List<Document> {
        val texts = splitText(word, language).map { it.asNode() }
        if (texts.isEmpty()) return emptyList()
        if (texts.size == 1) {
            return internalStore[texts.first()] ?: emptyList()
        }

        texts.forEach { t ->
            nodes.values
                .filter { w ->
                    val distance = levenshteinDistance(w.word, t.word)
                    distance < typoTolerance
                }
                .forEach {
                    internalStore[it]?.forEach {
                       TODO("Range based tolerance search")
                    }
                }
        }
    }
}


