
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
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

    // Слова, которые нужно показать
    val words6Letters = listOf("совхоз", "пробка", "мастак","комбат") // Слова из 6 букв
    val words4Letters = listOf("клоп", "приз", "морж") // Слова из 4 букв

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

    // Загружаем значения ячеек из PreferenceManager
    LaunchedEffect(Unit) {
        val savedValues = preferenceManager.getCellValues()
        cellValues.clear()
        cellValues.addAll(savedValues)
    }

    // Ваш контент
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Отображение слов
        Row(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Слова из 6 букв:",
                    fontSize = 20.sp,
                    color = Color.Black
                )
                words6Letters.forEach { word ->
                    Text(
                        text = word,
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.width(32.dp)) // Пробел между колонками

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Слова из 4 букв:",
                    fontSize = 20.sp,
                    color = Color.Black
                )
                words4Letters.forEach { word ->
                    Text(
                        text = word,
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            }
        }

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
                                .border(BorderStroke(1.dp, Color.Black))
                                .clickable {
                                    selectedCellIndex = index // Устанавливаем выбранную ячейку
                                    inputValue = cellValues[index] // Загружаем текущее значение в поле ввода
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = cellValues[index],
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
        if (selectedCellIndex != -1) {
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

        // Отображение введенных букв из ячеек с цифрами от 1 до 7
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            // Сбор введенных букв из ячеек с цифрами от 1 до 7
            val displayedLetters = (0 until 7).map { index ->
                cellValues[index + 3 * 7] // Получаем значения из ячеек с номерами 1-7
            }

            displayedLetters.forEach { letter ->
                Text(
                    text = letter,
                    fontStyle = FontStyle.Italic,
                    fontSize = 40.sp,
                    color = Color.Red,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}
