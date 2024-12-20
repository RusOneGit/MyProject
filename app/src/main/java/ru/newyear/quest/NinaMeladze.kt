package ru.newyear.quest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.newyear.quest.logika.PreferenceManager

class NinaMeladze : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                CrossWord()
                Spacer(modifier = Modifier.height(16.dp))
                ClickableTextFieldsExample()
            }
        }
    }
}

@Composable
fun CrossWord() {
    val context = LocalContext.current
    val preferenceManager = PreferenceManager(context)

    // Схема кроссворда
    val grid = listOf(
        listOf("#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "2", "#", "#", "#", "#", "#", "#"),
        listOf("#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "0", "#", "#", "#", "#", "#", "#"),
        listOf("#", "#", "#", "#", "#", "#", "#", "#", "3", "#", "0", "#", "#", "#", "#", "#", "#"),
        listOf("#", "#", "#", "#", "#", "#", "#", "1", "0", "0", "0", "#", "#", "#", "#", "8", "#"),
        listOf("#", "#", "#", "#", "#", "#", "7", "#", "0", "#", "0", "#", "#", "#", "#", "0", "#"),
        listOf("#", "#", "#", "#", "#", "#", "0", "#", "0", "#", "0", "#", "#", "#", "#", "0", "#"),
        listOf("#", "#", "#", "#", "4", "0", "0", "0", "0", "#", "0", "#", "6", "#", "#", "0", "#"),
        listOf(
            "#",
            "#",
            "10",
            "#",
            "#",
            "#",
            "0",
            "#",
            "#",
            "5",
            "0",
            "0",
            "0",
            "0",
            "0",
            "0",
            "0"
        ),
        listOf("#", "#", "0", "#", "#", "#", "0", "#", "#", "#", "0", "#", "0", "#", "#", "0", "#"),
        listOf("#", "#", "0", "#", "#", "#", "0", "#", "#", "#", "0", "#", "0", "#", "#", "0", "#"),
        listOf("9", "0", "0", "0", "0", "0", "0", "#", "#", "#", "0", "#", "0", "#", "#", "0", "#"),
        listOf("#", "#", "0", "#", "#", "#", "#", "#", "#", "#", "#", "#", "0", "#", "#", "#", "#"),
        listOf("#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "0", "#", "#", "#", "#"),
        listOf("#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "0", "#", "#", "#", "#"),
        listOf("#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "0", "#", "#", "#", "#"),
        listOf("#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "0", "#", "#", "#", "#")
    )

    // Состояние для хранения значений ячеек
    val cellValues = remember { mutableStateListOf(*Array(16 * 17) { "" }) }

    // Отображение кроссворда
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        for (row in grid.indices) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                for (col in grid[row].indices) {
                    val index = row * 17 + col
                    val cellContent = grid[row][col]

                    if (cellContent == "#") {
                        Box(
                            modifier = Modifier
                                .size(21.dp)
                                .padding(0.dp)
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(text = "", fontSize = 20.sp)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(21.dp)
                                .padding(0.dp)
                                .border(BorderStroke(1.dp, Color.Black)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = if (cellContent != "0") cellContent else cellValues[index],
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClickableTextFieldsExample() {
    val context = LocalContext.current
    val preferenceManager = PreferenceManager(context)

    // Список для хранения текста каждой строки
    val textList = remember {
        mutableStateListOf(
            "1.Именно это количество шагов надо сделать назад тихо на пальцах.",
            "2.Я повсюду …, забери меня, мама, домой!",
            "3.Цыганка, губы которой были сладкие, как вино",
            "4.Ради девушки с таким именем он тысячи раз обрывал провода.",
            "5.Её красота обманчива и влечёт напрасными надеждами.",
            "6.Эта женщина горяча и бешена",
            "7.Солёная, словно кровь, …-любовь.",
            "8.Именно так она вошла в его грешную жизнь.",
            "9.… мои обетованные.",
            "10.Что для тебя любовь, скажи? Свобода или сладкий…"
        )
    }

    // Список для хранения правильных ответов
    val correctAnswers = listOf(
        "сто",
        "иностранец",
        "сэра",
        "вера",
        "актриса",
        "тропикана",
        "текила",
        "красиво",
        "небеса",
        "плен"
    )

    // Список для хранения состояния видимости полей ввода и правильных ответов
    val inputAnswers = remember { mutableStateListOf<String>().apply { repeat(textList.size) { add("") } } }
    val isAnsweredCorrectly = remember { mutableStateListOf<Boolean>().apply { repeat(textList.size) { add(preferenceManager.sharedPreferences.getBoolean("answer_$it", false)) } } }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(textList.indices.toList()) { index ->
            Column {
                // Неизменяемое текстовое поле
                Text(
                    text = textList[index],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    style = MaterialTheme.typography.bodyLarge
                )

                // Поле ввода для ответа
                TextField(
                    value = inputAnswers[index],
                    onValueChange = { newAnswer ->
                        inputAnswers[index] = newAnswer // Обновляем ответ
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    placeholder = { Text("Введите ваш ответ...") },
                    singleLine = true
                )

                // Кнопка для проверки ответа
                Button(
                    onClick = {
                        val currentAnswer = inputAnswers[index].trim()
                        if (currentAnswer.equals(correctAnswers[index], ignoreCase = true)) {
                            Toast.makeText(context, "Правильный ответ!", Toast.LENGTH_SHORT).show()
                            isAnsweredCorrectly[index] = true
                            preferenceManager.sharedPreferences.edit()
                                .putBoolean("answer_$index", true)
                                .apply()
                              val cellValues = preferenceManager.getCellValues()
                            // Заполнение клеток кроссворда в зависимости от номера вопроса
                            fillCrossword(index + 1, currentAnswer, cellValues, preferenceManager)

                            // Проверяем, все ли ответы даны
                            if (isAnsweredCorrectly.all { it }) {
                                preferenceManager.setLevelCompleted(0, true)
                                Toast.makeText(context, "Поздравляем! Вы выиграли!", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(context, "Неправильный ответ. Попробуйте снова.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Проверить ответ")
                }

                // Если ответ правильный, показываем правильный ответ
                if (isAnsweredCorrectly[index]) {
                    Text(
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Normal,
                        text = correctAnswers[index],
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Green),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// Функция для заполнения кроссворда
fun fillCrossword(questionIndex: Int, answer: String, cellValues: MutableList<String>, preferenceManager: PreferenceManager) {
    // Определяем начальную позицию в кроссворде в зависимости от номера вопроса
    val startPositions = listOf(
        Pair(0, 10), // Вопрос 1 (1)
        Pair(1, 10), // Вопрос 2 (2)
        Pair(2, 8),  // Вопрос 3 (3)
        Pair(3, 7),  // Вопрос 4 (4)
        Pair(4, 6),  // Вопрос 5 (5)
        Pair(5, 5),  // Вопрос 6 (6)
        Pair(6, 5),  // Вопрос 7 (7)
        Pair(7, 5),  // Вопрос 8 (8)
        Pair(8, 2),  // Вопрос 9 (9)
        Pair(9, 2)   // Вопрос 10 (10)
    )

    val (row, col) = startPositions[questionIndex - 1] // Получаем начальную позицию

    // Заполняем ячейки в зависимости от направления
    if (questionIndex % 2 == 1) { // Вопросы 1, 3, 5, 9 - вправо
        for (i in answer.indices) {
            // Заполняем вправо
            cellValues[row * 17 + (col + i)] = answer[i].toString()
        }
    } else { // Вопросы 2, 4, 6, 7, 8, 10 - вниз
        for (i in answer.indices) {
            // Заполняем вниз
            cellValues[(row + i) * 17 + col] = answer[i].toString()
        }
    }

    // Сохраняем обновленные значения
    preferenceManager.saveCellValues(cellValues.toList())
}

@Preview
@Composable
fun preview() {
    // Предварительный просмотр можно оставить пустым или добавить тестовые данные
}
