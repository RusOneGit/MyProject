package ru.newyear.quest

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
            Column(modifier = Modifier.fillMaxSize()) {
                CrossWord()
                Spacer(modifier = Modifier.height(16.dp)) // Пробел между изображением и текстом
                ClickableTextFieldsExample()
            }
        }
    }
}

@Preview
@Composable
fun CrossWord() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.crossmeladze), // Убедитесь, что ресурс существует
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop // Сохраняет пропорции
        )
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
    val inputVisibility = remember { mutableStateListOf<Boolean>().apply { repeat(textList.size) { add(false) } } }
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
                        .padding(vertical = 4.dp)
                        .clickable {
                            // Переключаем видимость поля ввода
                            inputVisibility[index] = !inputVisibility[index]
                        },
                    style = MaterialTheme.typography.bodyLarge
                )

                // Если поле ввода видно и ответ еще не дан, отображаем его
                if (inputVisibility[index] && !isAnsweredCorrectly[index]) {
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

                                // Проверяем, все ли ответы даны
                                if (isAnsweredCorrectly.all { it }) {
                                    preferenceManager.setLevelCompleted(0, true)
                                    Toast.makeText(context, "Поздравляем! Вы выиграли!", Toast.LENGTH_LONG).show()
                                }
                            } else if (currentAnswer.equals("true", ignoreCase = true)) {
                                // Если введено "true", заполняем все ответы
                                for (i in correctAnswers.indices) {
                                    inputAnswers[i] = correctAnswers[i] // Заполняем правильными ответами
                                    isAnsweredCorrectly[i] = true // Устанавливаем флаг правильного ответа
                                    preferenceManager.sharedPreferences.edit()
                                        .putBoolean("answer_$i", true)
                                        .apply() // Сохраняем правильный ответ
                                }
                                preferenceManager.setLevelCompleted(0, true) // Устанавливаем первый уровень как завершенный // Устанавливаем флаг победы
                                Toast.makeText(context, "Поздравляем! Вы выиграли!", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Неправильный ответ. Попробуйте снова.", Toast.LENGTH_SHORT).show()
                                // Опционально: сбросить состояние для повторного ввода
                                inputAnswers[index] = "" // Сбросить поле ввода
                            }
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Проверить ответ")
                    }

                } else if (isAnsweredCorrectly[index]) {
                    // Если ответ правильный, показываем правильный ответ
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
