package io.github.devapro.droid.edit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.core.security.Totp
import io.github.devapro.droid.core.ui.EOutlinedTextField

@Composable
fun TotpSecretField(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        EOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("TOTP secret (Base32)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        val trimmed = value.trim()
        if (trimmed.isNotEmpty() && !Totp.isValidSecret(trimmed)) {
            Text(
                text = "Invalid Base32 secret",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        } else {
            Text(
                text = "Paste the Base32 secret from your provider's setup screen.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
