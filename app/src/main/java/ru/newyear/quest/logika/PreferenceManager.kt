package ru.newyear.quest.logika


import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("game_preferences", Context.MODE_PRIVATE)

    var isGameWon: Boolean
        get() = sharedPreferences.getBoolean("is_game_won", false)
        set(value) {
            sharedPreferences.edit().putBoolean("is_game_won", value).apply()
        }

    // Метод для установки состояния уровня (например, пройден ли уровень)
    fun setLevelCompleted(levelIndex: Int, completed: Boolean) {
        sharedPreferences.edit().putBoolean("level_$levelIndex", completed).apply()
    }

    // Метод для проверки, пройден ли уровень
    fun isLevelCompleted(levelIndex: Int): Boolean {
        return sharedPreferences.getBoolean("level_$levelIndex", false)
    }
}
