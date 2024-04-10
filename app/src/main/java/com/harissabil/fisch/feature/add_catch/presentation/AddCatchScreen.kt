package com.harissabil.fisch.feature.add_catch.presentation

import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AddCatchScreen(
    viewModel: AddCatchViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val imageBitmaps: SnapshotStateList<Bitmap> = remember {
        mutableStateListOf()
    }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val photoPicker =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            imageBitmaps.add(
                MediaStore.Images.Media.getBitmap(
                    context.contentResolver,
                    uri
                )
            )
        }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        imageBitmaps.remove(imageBitmaps.lastOrNull())
                        photoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                imageBitmaps.iterator().forEach { index ->
                    Image(
                        bitmap = index.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                            )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = state.fishName,
                    onValueChange = viewModel::setFishName,
                    label = { Text(text = "Fish name") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = if (state.isGenerating) 0.dp else 6.dp,
                        pressedElevation = 0.dp
                    ),
                    onClick = {
                        if (state.isGenerating.not()) {
                            viewModel.identifyFish(images = imageBitmaps)
                            keyboardController?.hide()
                        } else if (imageBitmaps.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Please select an image to identify the fish.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    AnimatedContent(
                        targetState = state.isGenerating,
                        label = ""
                    ) { generating ->
                        if (generating) {
                            CircularProgressIndicator()
                        } else {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null
                            )
                        }
                    }
                }

            }
        }

    }
}