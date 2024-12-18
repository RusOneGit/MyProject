package ru.newyear.quest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.newyear.quest.logika.PreferenceManager

class LizaRebus : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            rebusCreate() // Вызов вашего Composable здесь
        }
    }
}

@Preview
@Composable
fun rebusCreate() {
    val context = LocalContext.current
    val preferenceManager = PreferenceManager(context)

    // Список вопросов и правильных ответов
    val questions = listOf(
        Pair("Сколько получается ?", "15"), // Первый вопрос и ответ
        Pair("Верно! Этот месяц – старший сын,\n" +
                "Льда и снега господин: ", "декабрь")  // Второй вопрос и ответ
    )

    var currentQuestionIndex by remember { mutableStateOf(0) } // Индекс текущего вопроса
    var text by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf(false) } // Состояние для отслеживания правильного ответа
    var message by remember { mutableStateOf("") }
    var isGameWon by remember { mutableStateOf(false) } // Состояние для отслеживания победы

    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.rebus), // Убедитесь, что ресурс существует
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit // Сохраняет пропорции
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Если игрок выиграл, показываем сообщение о победе
        if (isGameWon) {
            Text("Огонь!Следующий уровень уже разблокирован!", modifier = Modifier.padding(16.dp))
        } else {
            // Если ответ правильный, показываем сообщение
            if (isCorrect) {
                // Проверяем, есть ли следующий вопрос
                if (currentQuestionIndex < questions.size - 1) {
                    // Переход к следующему вопросу
                    currentQuestionIndex++ // Переход к следующему вопросу
                    isCorrect = false // Сбрасываем состояние
                    text = "" // Очищаем текстовое поле
                    message = "" // Очищаем сообщение об ошибке
                } else {
                    // Если это последний вопрос, игрок выиграл

                    isGameWon = true
                    preferenceManager.isGameWon
                    preferenceManager.setLevelCompleted(2,true)
                }
            }

            // Отображаем текущий вопрос
            Text(questions[currentQuestionIndex].first, modifier = Modifier.padding(16.dp))

            TextField(
                value = text,
                onValueChange = { newText ->
                    text = newText // Обновляем состояние при изменении текста
                },
                label = { Text("Введите ответ") }, // Подсказка для пользователя
                modifier = Modifier.fillMaxWidth() // Занимает всю ширину
            )

            Spacer(modifier = Modifier.height(16.dp)) // Пробел между полем ввода и кнопкой

            // Кнопка для проверки введенного текста
            Button(onClick = {
                // Проверяем ответ на текущий вопрос
                if (text.trim().equals(questions[currentQuestionIndex].second, ignoreCase = true)) {
                    preferenceManager.setLevelCompleted(currentQuestionIndex + 1, true)
                    isCorrect = true // Устанавливаем состояние на true, если ответ правильный
                    message = "" // Очищаем сообщение об ошибке
                } else {
                    message = "Попробуй еще раз!" // Уведомление об ошибке
                }
            }) {
                Text("Отправить") // Переместили Text внутрь Button
            }

            // Отображение сообщения об ошибке
            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    color = androidx.compose.ui.graphics.Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
