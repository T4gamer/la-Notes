package com.example.lanote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lanote.destinations.ScreenEditDestination
import com.example.lanote.destinations.ScreenNotesDestination
import com.example.lanote.model.Note
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

@Composable
fun SearchTemplate(addCommand: () -> Unit, mainBody: @Composable () -> Unit) {
    Header()
    Box(
        modifier = Modifier.background(Secondary)
    ) {
        Box {
            Column(
                modifier = Modifier.background(Secondary)
            ) {
                mainBody()
            }
        }
        Box(
            modifier = Modifier
                .align(BottomEnd)
                .padding(end = 20.dp, bottom = 50.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    addCommand()
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
fun EmptyNotes() {
    Header()
    Spacer(modifier = Modifier.padding(40.dp))
    Image(
        painter = painterResource(id = R.drawable.notebook_rafiki), contentDescription = "rafiki"
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

@Composable
fun NotesButton(@DrawableRes icon: Int, action: () -> Unit = {}) {
    Button(
        onClick = { action() }, modifier = Modifier.size(50.dp), shape = RoundedCornerShape(15.dp)
    ) {
        Icon(
            painter = painterResource(id = icon), contentDescription = "info"
        )
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 45.dp, start = 25.dp, end = 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Notes", style = TextStyle(
                fontSize = 43.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Nunito,
                color = Color.White
            )
        )
        Row() {
            NotesButton(R.drawable.baseline_search_24)
            Spacer(modifier = Modifier.padding(11.dp))
            NotesButton(R.drawable.baseline_info_24)
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteList(viewModel: NotesListViewModel = hiltViewModel(), onEdit: (note: Note) -> Unit) {
    Column(
        modifier = Modifier.background(Secondary)
    ) {
        val randomColors = listOf(
            Color(0xFFFD99FF),
            Color(0xFF91F48F),
            Color(0xFFFFF599),
            Color(0xFFFF9E9E),
            Color(0xFF9EFFFF),
            Color(0xFFB69CFF)
        )
        Header()
        LazyColumn {
            items(viewModel.Notes) {
                var holdNote by remember { mutableStateOf(false) }
                var color by remember { mutableStateOf(randomColors.random()) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
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
                        .combinedClickable(onClick = {
                            color = color.copy(alpha = 0.4f)
                            onEdit(it)
                        }, onLongClick = { holdNote = true })
                ) {
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

@Composable
fun PreviewText(text: String, textSize: Int) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Gray,
        style = TextStyle(
            fontSize = textSize.sp, fontFamily = Nunito
        )
    )
}

@Composable
fun NoteEdit(
    note: Note,
    isNew:Boolean,
    viewModel: NotesListViewModel = hiltViewModel(),
    back: () -> Unit
) {
    var titleText by remember { mutableStateOf(note.Title) }
    var contentText by remember { mutableStateOf(note.Content) }
    var isView by remember { mutableStateOf(false) }
//    var isNew by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Secondary)
    ) {
        Column(modifier = Modifier.padding(25.dp, 0.dp)) {
            if (!isView) {
                editTopBar(onBack = { back() }, onVisable = { isView = !isView }) {
                    if(isNew) {
                        viewModel.addNote(titleText, contentText)
                        back()
                    }else{
                        note.Title = titleText
                        note.Content = contentText
                        viewModel.updateNote(note)
                        back()
                    }
                }
                TextField(
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Gray,
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Secondary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    value = titleText, onValueChange = { titleText = it },
                    textStyle = TextStyle(fontFamily = Nunito, fontSize = 48.sp)
                )
                TextField(
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Gray,
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Secondary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    value = contentText, onValueChange = { contentText = it },
                    textStyle = TextStyle(fontFamily = Nunito, fontSize = 23.sp)
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(45.dp, 25.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    NotesButton(icon = R.drawable.arrow_back, action = {
                        back()
                    })
                    Row {
                        Spacer(modifier = Modifier.padding(11.dp))
                        NotesButton(
                            icon = R.drawable.edit,
                            action = {
                                isView = false
                            })
                    }
                }
                PreviewText(text = titleText, textSize = 48)
                PreviewText(text = contentText, textSize = 23)
            }
        }
    }
}

@Composable
fun editTopBar(onBack: () -> Unit, onVisable: () -> Unit, onSave: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(45.dp, 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        NotesButton(icon = R.drawable.arrow_back, action = {
            onBack()
        })
        Row {
            NotesButton(icon = R.drawable.visibility, action = { onVisable() })
            Spacer(modifier = Modifier.padding(11.dp))
            NotesButton(
                icon = R.drawable.save,
                action = {
                    onSave()
                    onBack()
                })
        }
    }
}

@RootNavGraph(start = true)
@Destination
@Composable
fun ScreenNotes(viewModel: NotesListViewModel = hiltViewModel(), navigator: DestinationsNavigator) {
    var gotNotes by remember { mutableStateOf(true) }
    gotNotes = viewModel.Notes.isEmpty()
    SearchTemplate(addCommand = {
        navigator.navigate(ScreenEditDestination(Note(-1, "Title here", "Type SomeThing Here ..."),true))
    }) {
        if (!gotNotes) {
            NoteList { navigator.navigate(ScreenEditDestination(it,false)) }
        } else {
            EmptyNotes()
        }
//
    }
}

@Destination
@Composable
fun ScreenEdit(navigator: DestinationsNavigator, note: Note,isNew: Boolean) {
    NoteEdit(note,isNew) {
        navigator.navigate(ScreenNotesDestination)
    }
}