package io.github.devapro.droid.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.core.security.PasswordStrength
import io.github.devapro.droid.core.security.PasswordStrengthEstimator
import io.github.devapro.droid.core.security.PasswordStrengthLevel

@Composable
fun PasswordStrengthMeter(
    password: String,
    modifier: Modifier = Modifier
) {
    if (password.isEmpty()) return

    val strength: PasswordStrength = remember(password) {
        PasswordStrengthEstimator.estimate(password)
    }

    val color = when (strength.level) {
        PasswordStrengthLevel.VERY_WEAK -> Color(0xFFE53935)
        PasswordStrengthLevel.WEAK -> Color(0xFFFB8C00)
        PasswordStrengthLevel.FAIR -> Color(0xFFFDD835)
        PasswordStrengthLevel.STRONG -> Color(0xFF7CB342)
        PasswordStrengthLevel.VERY_STRONG -> Color(0xFF43A047)
    }

    val progress = ((strength.level.score + 1) / 5f).coerceIn(0f, 1f)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = strength.level.label,
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
            Text(
                text = "${strength.entropyBits.toInt()} bits",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
