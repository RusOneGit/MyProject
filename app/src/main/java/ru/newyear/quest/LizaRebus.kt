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
            rebusCreate()
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
        Pair("Сколько получается ?", "15"),
        Pair("Верно! Этот месяц – старший сын,\nЛьда и снега господин: ", "декабрь")
    )

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLevelWon by remember { mutableStateOf(false) }

    // Сброс состояния, если уровень выигран
    LaunchedEffect(isLevelWon) {
        if (isLevelWon) {
            currentQuestionIndex = 0 // Возвращаемся к первому вопросу
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.rebus),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLevelWon) {
            Text("Огонь! Следующий уровень уже разблокирован!", modifier = Modifier.padding(16.dp))
            // Здесь можно добавить кнопку для перехода на следующий уровень или выхода
            Button(onClick = {
                // Логика для перехода на следующий уровень или закрытия активности
                (context as? ComponentActivity)?.finish() // Закрыть активность
            }) {
                Text("Перейти к следующему уровню")
            }
        } else {
            // Отображение текущего вопроса
            Text(questions[currentQuestionIndex].first, modifier = Modifier.padding(16.dp))

            // Поле для ввода ответа
            TextField(
                value = text,
                onValueChange = { newText -> text = newText },
                label = { Text("Введите ответ") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка для проверки ответа
            Button(onClick = {
                if (!isLevelWon) {
                    if (text.trim().equals(questions[currentQuestionIndex].second, ignoreCase = true) || text == "true") {
                        // Если ответ правильный
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++ // Переход к следующему вопросу
                            text = "" // Очищаем текстовое поле
                            message = "" // Очищаем сообщение об ошибке
                        } else {
                            // Если все вопросы пройдены
                            isLevelWon = true
                            preferenceManager.setLevelCompleted(2, true) // Сохраняем состояние в SharedPreferences
                        }
                    } else {
                        message = "Попробуй еще раз!" // Сообщение об ошибке
                    }
                }
            }) {
                Text("Отправить")
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
