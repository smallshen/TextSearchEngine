package club.eridani.textsearch.tokenizer


interface Tokenizer {


    fun splitText(str: String): List<String>

}