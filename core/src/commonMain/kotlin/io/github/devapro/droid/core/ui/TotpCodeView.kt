package io.github.devapro.droid.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.devapro.droid.core.security.Totp
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun TotpCodeView(
    secret: String,
    onCopy: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var nowSeconds by remember { mutableStateOf(Clock.System.now().epochSeconds) }

    LaunchedEffect(secret) {
        while (true) {
            nowSeconds = Clock.System.now().epochSeconds
            delay(1000L)
        }
    }

    val code = remember(secret, nowSeconds / Totp.DEFAULT_PERIOD_SECONDS) {
        Totp.generate(secret, nowSeconds)
    }
    val remaining = Totp.secondsRemaining(nowSeconds)
    val progress = remaining.toFloat() / Totp.DEFAULT_PERIOD_SECONDS.toFloat()

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "One-time code",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (code != null) {
                    IconButton(onClick = { onCopy(code) }) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "Copy code",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            if (code == null) {
                Text(
                    text = "Invalid TOTP secret",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = formatCode(code),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 32.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            color = if (remaining <= 5) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = remaining.toString(),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

private fun formatCode(code: String): String {
    if (code.length != 6) return code
    return "${code.substring(0, 3)} ${code.substring(3)}"
}
