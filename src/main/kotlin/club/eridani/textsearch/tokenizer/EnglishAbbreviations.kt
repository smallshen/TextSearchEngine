package club.eridani.textsearch.tokenizer

import java.util.stream.Collectors

object EnglishAbbreviations {

    private val dictionary: Set<String> by lazy {
        val stream = EnglishAbbreviations::class.java.getResourceAsStream("/smile/nlp/tokenizer/abbreviations_en.txt") ?: error("Could not load English abbreviations")

        stream.bufferedReader().use { br ->
            br.lines()
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .collect(Collectors.toSet())
        }
    }


    fun contains(word: String): Boolean {
        return dictionary.contains(word)
    }

}