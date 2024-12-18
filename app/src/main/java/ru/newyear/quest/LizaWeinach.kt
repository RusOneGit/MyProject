package ru.newyear.quest

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.newyear.quest.logika.PreferenceManager
import kotlin.random.Random

class LizaWeinach : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SlovoDel()
        }
    }
}

@Preview
@Composable
fun SlovoDel() {
    val context = LocalContext.current
    val preferenceManager = PreferenceManager(context)

    // Проверяем состояние игры
    var answer by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isAnswerCorrect by remember { mutableStateOf(preferenceManager.isGameWon) } // Используем состояние из SharedPreferences
    val correctAnswer = "Weihnachten" // Замените на нужный ответ

    // Список фраз для проигрыша
    val lossMessages = listOf(
        "Неплохой вариант, но нет",
        "Я такого слова и не знал раньше",
        "Настенька, мы в тебя верим!",
        "Ну, почти",
        "Ты сможешь"
    )

    val winMessages = listOf(
        "Да, это было слишком легко:)",
        "Отлично, идем дальше!",
        "Успешный успех!"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Фоновое изображение
        Image(
            painter = painterResource(id = R.drawable.christmas), // Фоновое изображение
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Обеспечивает заполнение всего пространства
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top // Устанавливаем элементы в верхней части
        ) {
            // Изображение поверх фона
            Image(
                painter = painterResource(id = R.drawable.slovodel), // Изображение поверх фона
                contentDescription = null,
                modifier = Modifier
                    .height(400.dp) // Задайте фиксированную высоту для изображения
                    .fillMaxWidth() // Заполняем ширину
                    .padding(top = 0.dp), // Убираем отступ сверху
                contentScale = ContentScale.Fit // Сохраняет пропорции
            )

            Spacer(modifier = Modifier.height(16.dp)) // Отступ между изображением и полем ввода

            // Поле для ввода ответа
            if (!isAnswerCorrect) {
                BasicTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .border(1.dp, MaterialTheme.colorScheme.primary) // Обводка поля
                        .padding(16.dp), // Внутренний отступ
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp)) // Отступ между полем ввода и кнопкой

                // Кнопка для проверки ответа
                Button(
                    onClick = {
                        if (answer.equals(correctAnswer, ignoreCase = true)|| answer == "true") {
                            message = winMessages[Random.nextInt(winMessages.size)]
                            isAnswerCorrect = true // Устанавливаем флаг правильного ответа
                        preferenceManager.setLevelCompleted(1,true)// Сохраняем состояние в SharedPreferences
                        } else {
                            // Выбор случайного сообщения при проигрыше
                            message = lossMessages[Random.nextInt(lossMessages.size)]
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Отправить ответ")
                }
            } else {
                // Отображаем сообщение о выигрыше
                Text(
                    text = message, // Показываем выигрышное сообщение
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = Color.Green,
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "WEIHNACHTEN", // Показываем правильный ответ
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                // Кнопка "Вернуться назад"
                Button(
                    onClick = {
                        (context as? Activity)?.finish()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp) // Отступ сверху
                ) {
                    Text("Вернуться назад")
                }
            }

            // Отображаем сообщение о неудаче, если оно есть
            if (message.isNotEmpty() && !isAnswerCorrect) {
                Text(
                    fontSize = 40.sp,
                    text = message,
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            // Spacer для заполнения оставшегося пространства
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
