package club.eridani.textsearch.tokenizer


import smile.nlp.normalizer.SimpleNormalizer
import java.util.regex.Pattern


class SimpleEnglishTokenizer(
    private val splitContraction: Boolean = true
) : Tokenizer {

    override fun splitText(str: String): List<String> {
        var text = SimpleNormalizer.getInstance().normalize(str)
        if (splitContraction) {
            text = WONT_CONTRACTION.matcher(text).replaceAll("$1ill not")
            text = SHANT_CONTRACTION.matcher(text).replaceAll("$1ll not")
            text = AINT_CONTRACTION.matcher(text).replaceAll("$1m not")
            for (regexp in NOT_CONTRACTIONS) {
                text = regexp.matcher(text).replaceAll("$1 not")
            }
            for (regexp in CONTRACTIONS2) {
                text = regexp.matcher(text).replaceAll("$1 $2")
            }
            for (regexp in CONTRACTIONS3) {
                text = regexp.matcher(text).replaceAll("$1 $2 $3")
            }
        }
        text = DELIMITERS[0].matcher(text).replaceAll(" $1 ")
        text = DELIMITERS[1].matcher(text).replaceAll(" $1")
        text = DELIMITERS[2].matcher(text).replaceAll(" $1")
        text = DELIMITERS[3].matcher(text).replaceAll(" . ")
        text = DELIMITERS[4].matcher(text).replaceAll(" $1 ")

        val words: Array<String> = WHITESPACE.split(text)

        if (words.size > 1 && words[words.size - 1] == ".") {
            if (EnglishAbbreviations.contains(words[words.size - 2])) {
                words[words.size - 2] = words[words.size - 2] + "."
            }
        }
        val result = mutableListOf<String>()
        for (token in words) {
            if (token.isNotEmpty()) {
                result.add(token)
            }
        }
        return result
    }

    companion object {
        private val WONT_CONTRACTION = Pattern.compile("(?i)\\b(w)(on't)\\b")
        private val SHANT_CONTRACTION = Pattern.compile("(?i)\\b(sha)(n't)\\b")
        private val AINT_CONTRACTION = Pattern.compile("(?i)\\b(a)(in't)\\b")
        private val NOT_CONTRACTIONS = arrayOf(
            Pattern.compile("(?i)\\b(can)('t|not)\\b"),
            Pattern.compile("(?i)(.)(n't)\\b")
        )

        /**
         * List of contractions adapted from Robert MacIntyre's tokenizer.
         */
        private val CONTRACTIONS2 = arrayOf(
            Pattern.compile("(?i)(.)('ll|'re|'ve|'s|'m|'d)\\b"),
            Pattern.compile("(?i)\\b(D)('ye)\\b"),
            Pattern.compile("(?i)\\b(Gim)(me)\\b"),
            Pattern.compile("(?i)\\b(Gon)(na)\\b"),
            Pattern.compile("(?i)\\b(Got)(ta)\\b"),
            Pattern.compile("(?i)\\b(Lem)(me)\\b"),
            Pattern.compile("(?i)\\b(Mor)('n)\\b"),
            Pattern.compile("(?i)\\b(T)(is)\\b"),
            Pattern.compile("(?i)\\b(T)(was)\\b"),
            Pattern.compile("(?i)\\b(Wan)(na)\\b")
        )
        private val CONTRACTIONS3 = arrayOf(
            Pattern.compile("(?i)\\b(Whad)(dd)(ya)\\b"),
            Pattern.compile("(?i)\\b(Wha)(t)(cha)\\b")
        )
        private val DELIMITERS = arrayOf( // Separate most punctuation
            Pattern.compile("((?U)[^\\w.'\\-/,&])"),  // Separate commas if they're followed by space (e.g., don't separate 2,500)
            Pattern.compile("(?U)(,\\s)"),  // Separate single quotes if they're followed by a space.
            Pattern.compile("(?U)('\\s)"),  // Separate periods that come before newline or end of string.
            Pattern.compile("(?U)\\. *(\\n|$)"),  // Separate continuous periods such as ... in ToC.
            Pattern.compile("(?U)(\\.{3,})")
        )
        private val WHITESPACE = Pattern.compile("(?U)\\s+")
    }
}