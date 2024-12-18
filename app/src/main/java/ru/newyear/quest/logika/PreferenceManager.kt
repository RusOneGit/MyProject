package ru.newyear.quest.logika

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("game_preferences", Context.MODE_PRIVATE)

    // Метод для установки состояния уровня (например, пройден ли уровень)
    fun setLevelCompleted(levelIndex: Int, completed: Boolean) {
        sharedPreferences.edit().putBoolean("level_$levelIndex", completed).apply()
    }

    // Метод для проверки, пройден ли уровень
    fun isLevelCompleted(levelIndex: Int): Boolean {
        return sharedPreferences.getBoolean("level_$levelIndex", false)
    }

    private val cellValuesKey = "cell_values"

    // Метод для получения сохраненных значений ячеек
    fun getCellValues(): List<String> {
        val savedValues = sharedPreferences.getString(cellValuesKey, null)
        return savedValues?.split(",")?.map { it.trim() } ?: List(56) { "" } // Возвращаем пустые строки, если нет сохраненных значений
    }

    // Метод для сохранения значений ячеек
    fun saveCellValues(values: List<String>) {
        val joinedValues = values.joinToString(",")
        sharedPreferences.edit().putString(cellValuesKey, joinedValues).apply()
    }

}
