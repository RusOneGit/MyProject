package ru.newyear.quest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.newyear.quest.logika.PreferenceManager

class OlgaCross : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CrosswordGrid()
        }
    }
}

@Preview
@Composable
fun CrosswordGrid() {
    val context = LocalContext.current
    val preferenceManager = PreferenceManager(context)

    // Схема кроссворда
    val grid = listOf(
        listOf(0, null, null, 0, null, 0, null),
        listOf(0, 0, 0, 0, 0, 0, null),
        listOf(0, 0, 0, 0, 0, 0, 0),
        listOf(1, 2, 3, 4, 5, 6, 7),
        listOf(0, 0, 0, null, 0, 0, 0),
        listOf(0, null, 0, null, null, 0, 0),
        listOf(null, null, 0, null, null, null, 0),
        listOf(null, null, null, null, null, null, 0)
    )

    // Состояние для хранения значений ячеек
    val cellValues = remember { mutableStateListOf(*Array(8 * 7) { "" }) }
    var selectedCellIndex by remember { mutableStateOf(-1) }
    var inputValue by remember { mutableStateOf("") }
    var showKeywordInput by remember { mutableStateOf(false) }
    var keywordInput by remember { mutableStateOf("") }

    // Одно ключевое слово
    val keyword = "бомжиха"
    var isGameWon by remember { mutableStateOf(false) } // Состояние для проверки, выиграна ли игра

    // Загружаем значения ячеек из PreferenceManager
    LaunchedEffect(Unit) {
        val savedValues = preferenceManager.getCellValues()
        cellValues.clear()
        cellValues.addAll(savedValues)
        isGameWon = preferenceManager.isGameWon // Загружаем состояние игры
    }

    // Ваш контент
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Отображение кроссворда
        for (row in grid.indices) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                for (col in grid[row].indices) {
                    val index = row * 7 + col
                    if (grid[row][col] != null) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(4.dp)
                                .border(BorderStroke(1.dp, Color.Black)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = if (isGameWon) "⛄" else cellValues[index],
                                fontSize = 20.sp
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }

        // Поле ввода и кнопка "Отправить"
        if (selectedCellIndex != -1 && !isGameWon) { // Скрываем поле ввода, если игра выиграна
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = inputValue,
                    onValueChange = { newValue ->
                        if (newValue.length <= 1) {
                            inputValue = newValue
                        }
                    },
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .padding(4.dp)
                        .border(BorderStroke(1.dp, Color.Black)),
                    textStyle = LocalTextStyle.current.copy(fontSize = 24.sp),
                    singleLine = true,
                    placeholder = { Text("Введите букву") }
                )
                Button(
                    onClick = {
                        if (selectedCellIndex != -1) {
                            cellValues[selectedCellIndex] = inputValue // Обновляем значение в выбранной ячейке
                            preferenceManager.saveCellValues(cellValues.toList()) // Сохраняем обновленные значения
                            inputValue = "" // Сбрасываем значение после отправки
                            selectedCellIndex = -1 // Сбрасываем выбранную ячейку
                        }
                    },
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Text("Отправить")
                }
            }
        }

        // Проверка на угаданное слово
        if (cellValues.all { it == keyword }) {
            isGameWon = true // Устанавливаем состояние игры в "выиграно"
            preferenceManager.isGameWon = true // Сохраняем состояние игры
            preferenceManager.setLevelCompleted(3,true)
            // Заполняем все ячейки снеговиками при победе
            for (i in cellValues.indices) {
                cellValues[i] = "⛄"
            }
            preferenceManager.saveCellValues(cellValues.toList()) // Сохраняем значения ячеек при победе

            Text(
                text = "Победа!",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .border(BorderStroke(1.dp, Color.Black))
                    .background(Color.LightGray)
                    .padding(8.dp),
                fontSize = 20.sp,
                color = Color.Blue
            )
        } else {
            // Изменяем текст на "бомжиха", если игра не выиграна
            Text(
                text = if (isGameWon) keyword else "Я знаю ответ!",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable { showKeywordInput = true }
                    .border(BorderStroke(1.dp, Color.Black))
                    .background(Color.LightGray)
                    .padding(8.dp),
                fontSize = 20.sp,
                color = Color.Blue
            )
        }

        // Поле ввода ключевого слова, если оно активно
        if (showKeywordInput) {
            TextField(
                value = keywordInput,
                onValueChange = { newValue -> keywordInput = newValue },
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 8.dp)
                    .border(BorderStroke(1.dp, Color.Black)),
                textStyle = LocalTextStyle.current.copy(fontSize = 24.sp),
                singleLine = true,
                placeholder = { Text("Введите ключевое слово") }
            )
            Button(
                onClick = {
                    if (keywordInput.lowercase() == keyword || keywordInput == "true") {
                        // Если ключевое слово угадано, завершаем игру
                        for (i in cellValues.indices) {
                            cellValues[i] = "⛄" // Заполняем все ячейки угаданным словом
                        }
                        preferenceManager.saveCellValues(cellValues.toList()) // Сохраняем значения ячеек при угадывании
                    } else {
                        // Можно добавить сообщение об ошибке, если нужно
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Проверить")
            }
        }
    }
}
