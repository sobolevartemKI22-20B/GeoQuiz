package com.example.geoquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.geoquiz.ui.theme.GeoQuizTheme

data class QuizQuestion(
    val text: String,
    val correctAnswer: Boolean
)

val questions = listOf(
    QuizQuestion(
        "Canberra is the capital of Australia.",
        true
    ),
    QuizQuestion(
        "The Pacific Ocean is larger than the Atlantic Ocean.",
        true
    ),
    QuizQuestion(
        "The Suez Canal connects the Red Sea and the Indian Ocean.",
        false
    ),
    QuizQuestion(
        "The source of the Nile River is in Egypt.",
        false
    ),
    QuizQuestion(
        "The Amazon River is the longest river in the Americas.",
        true
    ),
    QuizQuestion(
        "Lake Baikal is the world's oldest and deepest freshwater lake.",
        true
    )
)



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                GeoQuizScreen()
            }
        }
    }
}

@Composable
fun GeoQuizScreen() {

    var currentQuestionIndex by remember {
        mutableIntStateOf(0)
    }

    var answered by remember {
        mutableStateOf(false)
    }

    var correctCount by remember {
        mutableIntStateOf(0)
    }

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    val currentQuestion = questions[currentQuestionIndex]

    val isLastQuestion =
        currentQuestionIndex == questions.lastIndex

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "GeoQuiz",
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Вопрос ${currentQuestionIndex + 1} из ${questions.size}",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = currentQuestion.text,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопки ответов скрываются после ответа
            if (!answered) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Button(
                        onClick = {
                            if (currentQuestion.correctAnswer) {
                                correctCount++
                            }

                            answered = true

                            if (isLastQuestion) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Тест завершён! Правильных ответов: " +
                                                "$correctCount из ${questions.size}"
                                    )
                                }
                            }
                        }
                    ) {
                        Text("True")
                    }

                    Button(
                        onClick = {
                            if (!currentQuestion.correctAnswer) {
                                correctCount++
                            }

                            answered = true

                            if (isLastQuestion) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Тест завершён! Правильных ответов: " +
                                                "$correctCount из ${questions.size}"
                                    )
                                }
                            }
                        }
                    ) {
                        Text("False")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!(isLastQuestion && answered)) {

                Button(
                    onClick = {
                        currentQuestionIndex++
                        answered = false
                    },
                    enabled = answered
                ) {
                    Text("Next")
                }
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun GeoQuizPreview() {
    MaterialTheme() {
        GeoQuizScreen()
    }
}