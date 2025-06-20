package io.github.devapro.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.github.devapro.model.AdditionalFieldsModel
import io.github.devapro.model.ItemModel
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPasswordScreen(
    item: ItemModel? = null,
    onSave: (ItemModel) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(item?.title ?: "") }
    var description by remember { mutableStateOf(item?.description ?: "") }
    var url by remember { mutableStateOf(item?.url ?: "") }
    var username by remember { mutableStateOf(item?.username ?: "") }
    var password by remember { mutableStateOf(item?.password ?: "") }
    var passwordVisible by remember { mutableStateOf(false) }
        var additionalFields by remember { 
        mutableStateOf(item?.additionalFields ?: mutableListOf())
    }
    
    val isEditing = item != null
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Password" else "Add Password") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            val newItem = ItemModel(
                                id = item?.id ?: UUID.generateUUID().toString(),
                                title = title.trim(),
                                description = description.trim(),
                                url = url.trim(),
                                username = username.trim(),
                                password = password,
                                additionalFields = additionalFields.filter { 
                                    it.name.isNotBlank() || it.value.isNotBlank() 
                                }
                            )
                            onSave(newItem)
                        },
                        enabled = title.isNotBlank() && password.isNotBlank()
                    ) {
                        Text("Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            
            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
            
            item {
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("Website URL") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                )
            }
            
            item {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username/Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            
            item {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                )
            }
            
            item {
                Divider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Additional Fields",
                        style = MaterialTheme.typography.titleMedium
                    )
                    TextButton(
                        onClick = {
//                            additionalFields.add(
//                                AdditionalFieldsModel(
//                                    id = UUID.generateUUID().toString(),
//                                    name = "",
//                                    value = ""
//                                )
//                            )
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Field")
                    }
                }
            }
            
            itemsIndexed(additionalFields) { index, field ->
                AdditionalFieldItem(
                    field = field,
                    onNameChange = { newName ->
//                        additionalFields[index] = AdditionalFieldsModel(
//                            id = field.id,
//                            name = newName,
//                            value = field.value
//                        )
                    },
                    onValueChange = { newValue ->
//                        additionalFields[index] = AdditionalFieldsModel(
//                            id = field.id,
//                            name = field.name,
//                            value = newValue
//                        )
                    },
                    onDelete = {
//                        additionalFields.removeAt(index)
                    }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun AdditionalFieldItem(
    field: AdditionalFieldsModel,
    onNameChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Custom Field",
                    style = MaterialTheme.typography.titleSmall
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete field",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            OutlinedTextField(
                value = field.name,
                onValueChange = onNameChange,
                label = { Text("Field Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            OutlinedTextField(
                value = field.value,
                onValueChange = onValueChange,
                label = { Text("Field Value") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
} 