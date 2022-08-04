@file:Suppress("DuplicatedCode")

package club.eridani.textsearch.levenshtein

import club.eridani.textsearch.Node


fun levenshteinDistance(first: String, second: String): Int {
    if (first == second) return 0

    val firstLength = first.length
    val secondLength = second.length
    val distance = Array(firstLength + 1) { IntArray(secondLength + 1) }
    for (i in 0..firstLength) {
        distance[i][0] = i
    }
    for (j in 0..secondLength) {
        distance[0][j] = j
    }
    for (i in 1..firstLength) {
        for (j in 1..secondLength) {
            distance[i][j] = minOf(
                distance[i - 1][j] + 1,
                distance[i][j - 1] + 1,
                distance[i - 1][j - 1] + if (first[i - 1] == second[j - 1]) 0 else 1
            )
        }
    }
    return distance[firstLength][secondLength]
}

fun levenshteinDistance(first: List<Node>, second: List<Node>): Int {
    if (first == second) return 0

    val firstLength = first.size
    val secondLength = second.size
    val distance = Array(firstLength + 1) { IntArray(secondLength + 1) }
    for (i in 0..firstLength) {
        distance[i][0] = i
    }
    for (j in 0..secondLength) {
        distance[0][j] = j
    }
    for (i in 1..firstLength) {
        for (j in 1..secondLength) {
            distance[i][j] = minOf(
                distance[i - 1][j] + 1,
                distance[i][j - 1] + 1,
                distance[i - 1][j - 1] + if (first[i - 1] == second[j - 1]) 0 else 1
            )
        }
    }
    return distance[firstLength][secondLength]
}