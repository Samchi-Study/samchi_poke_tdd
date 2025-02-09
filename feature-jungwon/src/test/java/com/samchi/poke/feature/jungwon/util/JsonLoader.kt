package com.samchi.poke.feature.jungwon.util

object JsonLoader {
    fun loadJson(fileName: String): String {
        val resourceName = "/json/$fileName.json"
        return JsonLoader::class.java.getResourceAsStream(resourceName)?.bufferedReader()
            ?.use { it.readText() }
            ?: throw IllegalArgumentException("파일을 찾을 수 없습니다: $fileName")
    }
}