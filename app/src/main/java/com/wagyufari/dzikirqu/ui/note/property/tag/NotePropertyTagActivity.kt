package com.wagyufari.dzikirqu.ui.note.property.tag

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseActivity
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.room.dao.getNotePropertyDao
import com.wagyufari.dzikirqu.databinding.ActivityNotePropertyTagBinding
import com.wagyufari.dzikirqu.model.NoteProperty
import com.wagyufari.dzikirqu.model.NotePropertyType
import com.wagyufari.dzikirqu.model.events.NotePropertySelectedEvent
import com.wagyufari.dzikirqu.ui.note.composer.input.BasicTextFieldHint
import com.wagyufari.dzikirqu.ui.v2.theme.lato
import com.wagyufari.dzikirqu.util.Appbar
import com.wagyufari.dzikirqu.util.RxBus
import com.wagyufari.dzikirqu.util.io
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotePropertyTagActivity :
    BaseActivity<ActivityNotePropertyTagBinding, NotePropertyTagViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_note_property_tag
    override val viewModel: NotePropertyTagViewModel by viewModels()

    val isComposingNewProperty = mutableStateOf(false)
    val selectedTags = mutableStateListOf<String>()

    companion object {
        const val EXTRA_SELECTED_TAGS = "arg_selected_tags"

        fun newIntent(context: Context, tags: List<String>): Intent {
            return Intent(context, NotePropertyTagActivity::class.java).apply {
                putExtra(EXTRA_SELECTED_TAGS, tags.toTypedArray())
            }
        }

        fun NotePropertyTagActivity.getSelectedTags(): List<String> {
            return intent.getStringArrayExtra(EXTRA_SELECTED_TAGS)?.toList() ?: listOf()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedTags.addAll(getSelectedTags())

        viewModel.tags.observe(this) {

            val tags = it.filter { it.type == NotePropertyType.Tag }.map { it.content }

            viewDataBinding.composeView.setContent {
                var newProperty by remember {
                    mutableStateOf("")
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Appbar().withBackButton().setTitle("Tag")
                            .setRightButton(rightButtonTitle = listOf(LocaleConstants.SAVE.locale()),
                                rightButtonImage = listOf(),
                                rightButtonHandler = listOf {
                                    RxBus
                                        .getDefault()
                                        .send(NotePropertySelectedEvent(properties = selectedTags.map {
                                            NoteProperty
                                                .newTag()
                                                .apply {
                                                    content = it
                                                }
                                        }, type = NotePropertyType.Tag))
                                    finish()
                                },
                                rightButtonImageTint = null).build()
                        Column(Modifier.padding(16.dp)) {
                            Spacer(Modifier.height(16.dp))
                            FlowRow(
                                mainAxisSpacing = 8.dp,
                                crossAxisSpacing = 8.dp
                            ) {
                                tags.toHashSet().forEach { tag ->
                                    ItemProperty(
                                        isEditing = false,
                                        isSelected = selectedTags.any { it == tag },
                                        property = tag,
                                        onClicked = {
                                            if (selectedTags.any { it == tag }) {
                                                selectedTags.removeAll { it == tag }
                                            } else {
                                                selectedTags.add(tag)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    EditNewTag(property = newProperty) {
                        io {
                            if (!selectedTags.contains(it)) {
                                getNotePropertyDao().putNoteProperty(NoteProperty.newTag(it))
                                selectedTags.add(it)
                            }
                        }
                    }

                }

            }

        }

    }

    @Composable
    fun EditNewTag(property: String, onSubmit: (String) -> Unit) {
        var tfv by remember {
            val textFieldValue =
                TextFieldValue(
                    text = property,
                    selection = TextRange(property.length, property.length)
                )
            mutableStateOf(textFieldValue)
        }
        val focusRequester by remember { mutableStateOf(FocusRequester.Default) }

        BasicTextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .padding(16.dp),
            value = tfv,
            onValueChange = {
                tfv = it
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = lato,
                color = colorResource(id = R.color.textPrimary)
            ),
            keyboardActions = KeyboardActions {
                onSubmit(tfv.text.lowercase().replace(" ", ""))
                tfv = TextFieldValue("")
                focusRequester.freeFocus()
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            decorationBox = { innerTextField ->
                BasicTextFieldHint(
                    modifier = Modifier,
                    text = tfv.text,
                    hint = LocaleConstants.ADD_NEW_LABEL.locale(),
                    fontSize = 18
                )
                innerTextField()
            },
        )
    }

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
    @Composable
    fun ItemProperty(
        isEditing: Boolean,
        isSelected: Boolean,
        property: String,
        onValueChange: (String) -> Unit? = {},
        onClicked: () -> Unit,
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
                    .focusRequester(focusRequester)
                    .clip(RoundedCornerShape(24.dp))
                    .background(colorResource(id = R.color.neutral_300))
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                value = tfv,
                onValueChange = {
                    tfv = it
                    onValueChange(it.text)
                },
                keyboardActions = KeyboardActions {
                    focusRequester.freeFocus()
                    isEditing = false
                    isComposingNewProperty.value = false
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                decorationBox = { innerTextField ->
                    BasicTextFieldHint(
                        modifier = Modifier,
                        text = tfv.text,
                        hint = "Tag",
                        fontSize = 16
                    )
                    innerTextField()
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = lato,
                    color = colorResource(id = R.color.textPrimary)
                ),
            )
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        } else {
            Card(shape = RoundedCornerShape(24.dp),
                backgroundColor = colorResource(id = if (isSelected) R.color.colorPrimary else R.color.neutral_300),
                onClick = {
                    onClicked()
                }) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                    text = "#" + property,
                    fontSize = 16.sp,
                    fontFamily = lato,
                    color = colorResource(id = if (isSelected) R.color.white else R.color.textTertiary)
                )
            }
        }
    }
}