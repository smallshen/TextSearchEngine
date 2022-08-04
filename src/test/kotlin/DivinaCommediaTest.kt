@file:OptIn(ExperimentalSerializationApi::class)

import club.eridani.textsearch.Database
import club.eridani.textsearch.Language
import club.eridani.textsearch.levenshtein.levenshteinDistance
import club.eridani.textsearch.tokenizer.SimpleEnglishTokenizer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File

@Serializable
private data class Sentence(val id: String, val txt: String)


fun main() {
    val all: List<Sentence> = File("testDataset/divinaCommedia.json").inputStream().use { input -> Json.decodeFromStream(input) }
    val database = Database(
        mapOf(
            Language.English to SimpleEnglishTokenizer()
        )
    )

    all.forEach { sentence ->
        database.insert(sentence.txt)
    }

    val result = database.search("stelle")

    println(result.size)

    result.forEach {
        println(it.asString())
    }
}