package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlassBackgroundBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background == com.example.ui.theme.MedNovaBackgroundDark

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                if (isDark) com.example.ui.theme.MedNovaBackgroundDark
                else com.example.ui.theme.MedNovaBackgroundLight
            )
    ) {
        // Decorative ambient glowing orbs
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            if (isDark) {
                // Top-Left Blue Orb
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x3B2563EB),
                            Color(0x102563EB),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.1f, h * 0.08f),
                        radius = w * 0.8f
                    )
                )
                // Bottom-Right Teal Orb
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x2B14B8A6),
                            Color(0x0814B8A6),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.95f, h * 0.85f),
                        radius = w * 0.85f
                    )
                )
                // Center-Left Purple Orb
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x226366F1),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.3f, h * 0.5f),
                        radius = w * 0.6f
                    )
                )
            } else {
                // Subtle bright pastel glowing orbs for Light mode
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x333B82F6),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.1f, h * 0.1f),
                        radius = w * 0.7f
                    )
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x2D14B8A6),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.9f, h * 0.8f),
                        radius = w * 0.7f
                    )
                )
            }
        }

        content()
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    bgAlpha: Float = 0.12f,
    borderAlpha: Float = 0.22f,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background == com.example.ui.theme.MedNovaBackgroundDark

    val bgBrush = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = bgAlpha),
                Color.White.copy(alpha = bgAlpha * 0.45f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.88f),
                Color.White.copy(alpha = 0.65f)
            )
        )
    }

    val borderBrush = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = borderAlpha),
                Color.White.copy(alpha = borderAlpha * 0.25f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.90f),
                Color.White.copy(alpha = 0.40f)
            )
        )
    }

    var boxModifier = modifier
        .clip(shape)
        .background(bgBrush)
        .border(BorderStroke(1.dp, borderBrush), shape)

    if (onClick != null) {
        boxModifier = boxModifier.clickable { onClick() }
    }

    Box(modifier = boxModifier) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun GlassBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    bgAlpha: Float = 0.10f,
    borderAlpha: Float = 0.18f,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background == com.example.ui.theme.MedNovaBackgroundDark

    val bgBrush = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = bgAlpha),
                Color.White.copy(alpha = bgAlpha * 0.3f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.82f),
                Color.White.copy(alpha = 0.55f)
            )
        )
    }

    val borderBrush = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = borderAlpha),
                Color.White.copy(alpha = borderAlpha * 0.2f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.7f),
                Color.White.copy(alpha = 0.3f)
            )
        )
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(bgBrush)
            .border(BorderStroke(1.dp, borderBrush), shape),
        content = content
    )
}

@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = com.example.ui.theme.MedNovaBlue,
    contentColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        accentColor,
                        accentColor.copy(alpha = 0.8f)
                    )
                )
            )
            .border(
                BorderStroke(1.dp, Color.White.copy(alpha = 0.35f)),
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
