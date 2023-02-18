package com.example.lanote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lanote.destinations.MainDestination
import com.example.lanote.destinations.NoteListDestination
import com.example.lanote.ui.NotesListViewModel
import com.example.lanote.ui.theme.LaNoteTheme
import com.example.lanote.ui.theme.Nunito
import com.example.lanote.ui.theme.Secondary
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaNoteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Secondary),
                    color = MaterialTheme.colors.background
                ) {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }
}

@RootNavGraph(start = true)
@Destination
@Composable
fun Main(navigator: DestinationsNavigator, viewModel: NotesListViewModel = hiltViewModel()) {
    var thereIsNotes by remember { mutableStateOf(true) }
    thereIsNotes = viewModel.Notes.isNotEmpty()
    Box(
        modifier = Modifier.background(Secondary)
    ) {
        Box {
            Column(
                modifier = Modifier.background(Secondary)
            ) {
                Header()
                if (thereIsNotes) {
                    navigator.navigate(NoteListDestination)
                } else {
                    Spacer(modifier = Modifier.padding(40.dp))
                    Image(
                        painter = painterResource(id = R.drawable.notebook_rafiki),
                        contentDescription = "rafiki"
                    )
                    Text(
                        text = "Create your first notes", style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light,
                            fontFamily = Nunito,
                            color = Color.White
                        ), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(BottomEnd)
                .padding(end = 20.dp, bottom = 50.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    viewModel.addNote("Empty Note")
                }, shape = CircleShape, backgroundColor = MaterialTheme.colors.secondary
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add Notes",
                    tint = Color.White,
                )
            }
        }
        Spacer(modifier = Modifier.fillMaxHeight())
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LaNoteTheme {
//        Main()
    }
}

@Composable
fun NotesButton(@DrawableRes icon: Int) {
    Button(
        onClick = { /*TODO*/ }, modifier = Modifier.size(50.dp), shape = RoundedCornerShape(15.dp)
    ) {
        Icon(
            painter = painterResource(id = icon), contentDescription = "info"
        )
    }
}

@Composable
fun Header() {
    Row(modifier = Modifier.padding(top = 45.dp, start = 25.dp)) {
        Text(
            text = "Notes", style = TextStyle(
                fontSize = 43.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Nunito,
                color = Color.White
            )
        )
        Spacer(modifier = Modifier.padding(50.dp))
        NotesButton(R.drawable.baseline_search_24)
        Spacer(modifier = Modifier.padding(11.dp))
        NotesButton(R.drawable.baseline_info_24)
        Spacer(modifier = Modifier.padding(3.dp))
    }
}

@Destination
@Composable
fun NoteList(navigator: DestinationsNavigator, viewModel: NotesListViewModel = hiltViewModel()) {
    var noNotes by remember { mutableStateOf(false) }
    noNotes = viewModel.Notes.isEmpty()
    Box(
        modifier = Modifier.background(Secondary)
    ) {
        Box {
            Column(
                modifier = Modifier.background(Secondary)
            ) {
                Header()
                if (noNotes) {
                    navigator.navigate(MainDestination)
                } else {
                    val randomColors = listOf(
                        Color(0xFFFD99FF),
                        Color(0xFF91F48F),
                        Color(0xFFFFF599),
                        Color(0xFFFF9E9E),
                        Color(0xFF9EFFFF),
                        Color(0xFFB69CFF)
                    )
                    LazyColumn {
                        items(viewModel.Notes) {
                            var holdNote by remember { mutableStateOf(false) }
                            var color by remember { mutableStateOf(randomColors.random()) }
                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(25.dp, 12.5.dp)
                                    .requiredHeight(50.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(color)
                                    .fillMaxWidth()
                                    .border(
                                        border = ButtonDefaults.outlinedBorder,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .pointerInput(Unit) {
                                        detectTapGestures(onLongPress = {
                                            holdNote = true
                                        }, onPress = {
                                            color = color.copy(alpha = 0.4f)
//                            navigator.navigate(NoteEditDestination)
                                        })
                                    }) {
                                AnimatedVisibility(visible = !holdNote) {
                                    NoteTitle(text = it.Title)
                                }
                                AnimatedVisibility(visible = holdNote) {
                                    DeleteNoteButton(onBack = {
                                        holdNote = false
                                        color = color.copy(alpha = 1f)
                                    }, onDelete = {
                                        viewModel.deleteNote(it)
                                        holdNote = false
                                        color = color.copy(alpha = 1f)
                                    })
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(20.dp))
                }

            }
        }
        Box(
            modifier = Modifier
                .align(BottomEnd)
                .padding(end = 20.dp, bottom = 50.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    viewModel.addNote("Empty Note")
                }, shape = CircleShape, backgroundColor = MaterialTheme.colors.secondary
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add Notes",
                    tint = Color.White,
                )
            }
        }
        Spacer(modifier = Modifier.fillMaxHeight())
    }
}

@Composable
fun NoteTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(24.dp, 0.dp),
        textAlign = TextAlign.Justify,
        style = TextStyle(
            fontSize = 25.sp, fontFamily = Nunito, fontWeight = FontWeight.SemiBold
        )
    )
}

@Composable
fun DeleteNoteButton(onDelete: () -> Unit, onBack: () -> Unit) {
    Row {
        Button(
            onClick = { onDelete() },
            modifier = Modifier
                .weight(8f)
                .fillMaxHeight(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
        ) {
            Image(imageVector = Icons.Default.Delete, contentDescription = "Delete Note")
        }
        Button(
            onClick = { onBack() },
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
        ) {
            Icon(painter = painterResource(id = R.drawable.arrow_back), contentDescription = "back")
        }
    }
}

@Destination
@Composable
fun NoteEdit(navigator: DestinationsNavigator) {
    Row {
        NotesButton(icon = R.drawable.arrow_back)
        Spacer(modifier = Modifier.padding(50.dp))
    }
}

