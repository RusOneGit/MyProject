package ru.newyear.quest

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import android.content.Intent
import android.view.WindowInsets.Side.all
import android.widget.Toast
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.newyear.quest.logika.PreferenceManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskScreen()
            MainScreen()
        }
    }
}

@Preview
@Composable
fun TaskScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Устанавливаем фон
        Image(
            painter = painterResource(id = R.drawable.background), // Убедитесь, что ресурс существует
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Сохраняет пропорции
        )
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp
        ) {
            items(12) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f) // Используем aspectRatio для равномерного распределения
                        .background(Color.Gray.copy(0.5f))
                        .clickable { onColumnHeaderClick(context, index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Задача ${index + 1}", color = Color.White)
                }
            }
        }
    }
}



fun onColumnHeaderClick(context: Context, columnIndex: Int) {
    val preferenceManager = PreferenceManager(context)

    when (columnIndex) {
        0 -> {
            val intent = Intent(context, NinaMeladze::class.java)
            context.startActivity(intent)

        }

        1 -> {
            // Проверяем, пройден ли второй уровень
            if (!preferenceManager.isLevelCompleted(0)) {
                val intent = Intent(context, LizaWeinach::class.java)
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Сначала пройдите первый уровень.", Toast.LENGTH_SHORT).show()
            }
        }

        2-> {
            if (!preferenceManager.isLevelCompleted(0)) {
                val intent = Intent(context, LizaRebus::class.java)
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Сначала пройдите второй уровень.", Toast.LENGTH_SHORT).show()
            }
        }
        else -> {
            Toast.makeText(context, "Сначала пройдите предыдущие уровни.", Toast.LENGTH_SHORT).show()
        }
        }
}

