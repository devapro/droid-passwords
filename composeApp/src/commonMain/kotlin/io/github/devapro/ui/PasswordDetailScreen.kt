package io.github.devapro.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.devapro.model.AdditionalFieldsModel
import io.github.devapro.model.ItemModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordDetailScreen(
    item: ItemModel,
    onBack: () -> Unit,
    onEdit: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    var passwordVisible by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = item.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary
                        )
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
            // Title
            item {
                DetailFieldCard(
                    label = "Title",
                    value = item.title,
                    onCopy = { 
                        clipboardManager.setText(AnnotatedString(item.title))
                    }
                )
            }
            
            // Description
            if (item.description.isNotBlank()) {
                item {
                    DetailFieldCard(
                        label = "Description",
                        value = item.description,
                        onCopy = { 
                            clipboardManager.setText(AnnotatedString(item.description))
                        }
                    )
                }
            }
            
            // Website URL
            if (item.url.isNotBlank()) {
                item {
                    DetailFieldCard(
                        label = "Website URL",
                        value = item.url,
                        onCopy = { 
                            clipboardManager.setText(AnnotatedString(item.url))
                        }
                    )
                }
            }
            
            // Username
            if (item.username.isNotBlank()) {
                item {
                    DetailFieldCard(
                        label = "Username",
                        value = item.username,
                        onCopy = { 
                            clipboardManager.setText(AnnotatedString(item.username))
                        }
                    )
                }
            }
            
            // Password
            item {
                PasswordFieldCard(
                    value = item.password,
                    isVisible = passwordVisible,
                    onToggleVisibility = { passwordVisible = !passwordVisible },
                    onCopy = { 
                        clipboardManager.setText(AnnotatedString(item.password))
                    }
                )
            }
            
            // Additional Fields
            if (item.additionalFields.isNotEmpty()) {
                item {
                    Text(
                        text = "Additional Fields",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                
                items(item.additionalFields) { field ->
                    if (field.name.isNotBlank() || field.value.isNotBlank()) {
                        DetailFieldCard(
                            label = field.name.ifBlank { "Custom Field" },
                            value = field.value,
                            onCopy = { 
                                clipboardManager.setText(AnnotatedString(field.value))
                            }
                        )
                    }
                }
            }
            
            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun DetailFieldCard(
    label: String,
    value: String,
    onCopy: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(
                    onClick = onCopy,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.ContentCopy,
                        contentDescription = "Copy $label",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun PasswordFieldCard(
    value: String,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    onCopy: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isVisible) value else "•".repeat(value.length.coerceAtMost(12)),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = if (isVisible) null else androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
                Row {
                    IconButton(
                        onClick = onToggleVisibility,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = if (isVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (isVisible) "Hide password" else "Show password",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "Copy password",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
} 