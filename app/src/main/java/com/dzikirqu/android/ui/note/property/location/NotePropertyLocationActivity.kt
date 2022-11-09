package com.dzikirqu.android.ui.note.property.location

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseActivity
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.room.dao.getNotePropertyDao
import com.dzikirqu.android.databinding.ActivityNotePropertyLocationBinding
import com.dzikirqu.android.model.NoteProperty
import com.dzikirqu.android.model.NotePropertyType
import com.dzikirqu.android.model.events.NotePropertySelectedEvent
import com.dzikirqu.android.model.updateLocation
import com.dzikirqu.android.ui.bsd.EditTextBSD
import com.dzikirqu.android.ui.note.composer.input.BasicTextFieldHint
import com.dzikirqu.android.ui.v2.theme.lato
import com.dzikirqu.android.util.Appbar
import com.dzikirqu.android.util.RxBus
import com.dzikirqu.android.util.io
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotePropertyLocationActivity :
    BaseActivity<ActivityNotePropertyLocationBinding, NotePropertyLocationViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_note_property_location
    override val viewModel: NotePropertyLocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.locations.observe(this){

            val locations = it.filter { it.type == NotePropertyType.Location }
            viewDataBinding.composeView.setContent {
                var newProperty by remember {
                    mutableStateOf(
                        NoteProperty.newLocation()
                    )
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Appbar().withBackButton().setTitle(LocaleConstants.LOCATION.locale()).build()
                        Column(Modifier
                            .weight(1f)) {
                            LazyColumn(
                                Modifier
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                item{
                                    locations.forEach { property ->
                                        ItemProperty(
                                            isEditing = false,
                                            property = property.content,
                                            onActionDone = {
                                                io {
                                                    property.updateLocation(
                                                        this@NotePropertyLocationActivity,
                                                        it
                                                    )
                                                }
                                            },
                                            onEdit = {
                                                EditTextBSD.newInstance(property).show(supportFragmentManager, "")
                                            })
                                    }
                                }
                            }
                        }
                        EditNewProperty(property = newProperty.content) {
                            io {
                                getNotePropertyDao().putNoteProperty(NoteProperty.newLocation(it))
                                newProperty = NoteProperty.newLocation()
                            }
                        }
                    }
                }
            }
        }

    }

    @Composable
    fun EditNewProperty(property: String, onSubmit: (String) -> Unit) {
        var tfv by remember {
            val textFieldValue =
                TextFieldValue(
                    text = property,
                    selection = TextRange(property.length, property.length)
                )
            mutableStateOf(textFieldValue)
        }
        var isEditing by remember { mutableStateOf(false) }
        val focusRequester by remember { mutableStateOf(FocusRequester.Default) }

        if (!isEditing) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        isEditing = true
                    },
                text = LocaleConstants.ADD_NEW_LOCATION.locale(),
                fontFamily = lato,
                fontSize = 18.sp,
                color = colorResource(
                    id = R.color.textTertiary
                )
            )
        } else {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(focusRequester),
                value = tfv,
                onValueChange = {
                    tfv = it
                },
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = lato,
                    color = colorResource(id = R.color.textPrimary)
                ),
                keyboardActions = KeyboardActions {
                    onSubmit(tfv.text)
                    tfv = TextFieldValue("")
                    isEditing = false
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                decorationBox = { innerTextField ->
                    BasicTextFieldHint(
                        modifier = Modifier,
                        text = tfv.text,
                        hint = LocaleConstants.ADD_NEW_LOCATION.locale(),
                        fontSize = 18
                    )
                    innerTextField()
                },
            )
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }

    @Composable
    fun ItemProperty(
        isEditing: Boolean,
        property: String,
        onValueChange: (String) -> Unit? = {},
        onActionDone: (String) -> Unit,
        onEdit: () -> Unit,
    ) {
        var isEditing by remember { mutableStateOf(isEditing) }
        val focusRequester by remember { mutableStateOf(FocusRequester.Default) }

        if (isEditing) {
            var tfv by remember {
                val textFieldValue =
                    TextFieldValue(
                        text = property,
                        selection = TextRange(property.length, property.length)
                    )
                mutableStateOf(textFieldValue)
            }

            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = tfv,
                onValueChange = {
                    tfv = it
                    onValueChange(it.text)
                },
                keyboardActions = KeyboardActions {
                    onActionDone(tfv.text)
                    focusRequester.freeFocus()
                    isEditing = false
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                decorationBox = { innerTextField ->
                    BasicTextFieldHint(
                        modifier = Modifier,
                        text = tfv.text,
                        hint = "Pemateri",
                        fontSize = 20
                    )
                    innerTextField()
                },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = lato,
                    color = colorResource(id = R.color.textPrimary)
                )
            )
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        } else {
            Box(Modifier.fillMaxWidth().clickable {
                RxBus
                    .getDefault()
                    .send(NotePropertySelectedEvent(NoteProperty.newLocation(property)))
                finish()
            }){
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                        , horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = property,
                        fontSize = 20.sp,
                        fontFamily = lato,
                        color = colorResource(id = R.color.textPrimary)
                    )
                    Image(modifier = Modifier
                        .clickable {
                            onEdit()
                        }
                        .height(24.dp)
                        .width(24.dp),
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            colorResource(id = R.color.textPrimary)
                        ))
                }
                Divider()
            }
        }
    }

}