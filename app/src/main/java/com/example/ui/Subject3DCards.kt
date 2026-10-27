package com.example.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.data.IComSubject
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

data class Subject3DTheme(
    val primaryGradient: List<Color>,
    val bevelColor: Color,
    val glowColor: Color,
    val accentBadgeColor: Color,
    val icon: ImageVector,
    val badgeLabel: String
)

fun getSubject3DTheme(subjectCode: String): Subject3DTheme {
    return when (subjectCode) {
        "ACC_1", "ACC_2" -> Subject3DTheme(
            primaryGradient = listOf(Color(0xFF0A2540), Color(0xFF1E3A8A), Color(0xFF0F172A)),
            bevelColor = Color(0xFFF59E0B),
            glowColor = Color(0xFFFBBF24),
            accentBadgeColor = Color(0xFFFEF3C7),
            icon = Icons.Default.AccountBalance,
            badgeLabel = "Accounting"
        )
        "ECO_1" -> Subject3DTheme(
            primaryGradient = listOf(Color(0xFF064E3B), Color(0xFF059669), Color(0xFF022C22)),
            bevelColor = Color(0xFF34D399),
            glowColor = Color(0xFF6EE7B7),
            accentBadgeColor = Color(0xFFD1FAE5),
            icon = Icons.Default.AutoGraph,
            badgeLabel = "Economics"
        )
        "POC_1" -> Subject3DTheme(
            primaryGradient = listOf(Color(0xFF3B0764), Color(0xFF6D28D9), Color(0xFF1E1B4B)),
            bevelColor = Color(0xFFC084FC),
            glowColor = Color(0xFFE9D5FF),
            accentBadgeColor = Color(0xFFF3E8FF),
            icon = Icons.Default.BusinessCenter,
            badgeLabel = "Commerce"
        )
        "BM_1" -> Subject3DTheme(
            primaryGradient = listOf(Color(0xFF7C2D12), Color(0xFFD97706), Color(0xFF451A03)),
            bevelColor = Color(0xFFFBBF24),
            glowColor = Color(0xFFFDE68A),
            accentBadgeColor = Color(0xFFFEF3C7),
            icon = Icons.Default.Calculate,
            badgeLabel = "Business Math"
        )
        "BLAW_2" -> Subject3DTheme(
            primaryGradient = listOf(Color(0xFF0C4A6E), Color(0xFF0284C7), Color(0xFF082F49)),
            bevelColor = Color(0xFF38BDF8),
            glowColor = Color(0xFFBAE6FD),
            accentBadgeColor = Color(0xFFE0F2FE),
            icon = Icons.Default.AccountBalance,
            badgeLabel = "Banking"
        )
        "CG_2" -> Subject3DTheme(
            primaryGradient = listOf(Color(0xFF14532D), Color(0xFF16A34A), Color(0xFF052E16)),
            bevelColor = Color(0xFF4ADE80),
            glowColor = Color(0xFFBBF7D0),
            accentBadgeColor = Color(0xFFDCFCE7),
            icon = Icons.Default.Public,
            badgeLabel = "Geography"
        )
        else -> Subject3DTheme(
            primaryGradient = listOf(Color(0xFF312E81), Color(0xFF4F46E5), Color(0xFF1E1B4B)),
            bevelColor = Color(0xFF818CF8),
            glowColor = Color(0xFFC7D2FE),
            accentBadgeColor = Color(0xFFE0E7FF),
            icon = Icons.Default.Insights,
            badgeLabel = "Statistics"
        )
    }
}

@Composable
fun Animated3DHeroBanner(
    notesCount: Int,
    pendingTasksCount: Int,
    quizzesCount: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hero_3d_transition")

    val floatAnim by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hero_float"
    )

    val shimmerProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "hero_shimmer"
    )

    var isPressed by remember { mutableStateOf(false) }
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "hero_press_scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 8.dp)
            .graphicsLayer {
                scaleX = pressScale
                scaleY = pressScale
                rotationX = floatAnim * 2.5f
                rotationY = -floatAnim * 2.5f
                cameraDistance = 16f * density
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            }
    ) {
        // 3D Deep Extrusion Base Layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 8.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF04101E), Color(0xFF022C22))
                    )
                )
        )

        // Main 3D Card Body
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = Color(0xFF10B981),
                    spotColor = Color(0xFFF59E0B)
                )
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF0A2540),
                            Color(0xFF0F3D5E),
                            Color(0xFF065F46)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(900f, 500f)
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.45f),
                            Color(0xFFFBBF24).copy(alpha = 0.55f),
                            Color.White.copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
        ) {
            // Animated 3D Holographic Isometric Commerce Cube & Floating Ledger Bars
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height

                // Animated diagonal specular light sweep
                val sweepX = (shimmerProgress * 1.6f - 0.3f) * w
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x33FBBF24),
                            Color.Transparent
                        ),
                        center = Offset(sweepX, h * 0.3f),
                        radius = w * 0.55f
                    ),
                    radius = w * 0.55f,
                    center = Offset(sweepX, h * 0.3f)
                )

                // Floating 3D Isometric Financial Bars on the right
                val baseBarX = w * 0.78f
                val baseBarY = h * 0.52f + (floatAnim * 8f)
                val barHeights = listOf(28f, 46f, 68f)
                barHeights.forEachIndexed { idx, bh ->
                    val bx = baseBarX + idx * 32f
                    val by = baseBarY - bh
                    // Front face
                    drawRoundRect(
                        color = Color(0x4434D399),
                        topLeft = Offset(bx, by),
                        size = Size(20f, bh),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    // Top 3D cap highlight
                    drawRoundRect(
                        color = Color(0x66FBBF24),
                        topLeft = Offset(bx - 3f, by - 5f),
                        size = Size(20f, 8f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }

                // Rotating 3D wireframe ring
                rotate(degrees = shimmerProgress * 360f, pivot = Offset(w * 0.88f, h * 0.22f)) {
                    drawCircle(
                        color = Color(0x33FBBF24),
                        radius = 48f,
                        center = Offset(w * 0.88f, h * 0.22f),
                        style = Stroke(width = 3f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color(0xFFF59E0B).copy(alpha = 0.22f),
                        modifier = Modifier.border(
                            1.dp,
                            Color(0xFFFBBF24).copy(alpha = 0.6f),
                            RoundedCornerShape(50)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "3D INTERACTIVE COMMERCE PORTAL",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFFDE68A)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "I.Com Smart Dashboard",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
                Text(
                    text = "Accounting • Economics • Principles of Commerce • Business Math",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFD1FAE5),
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // 3D Glassmorphic Floating Telemetry Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Glass3DStatTile(
                        label = "Cloud Notes",
                        value = notesCount.toString(),
                        accent = Color(0xFF34D399),
                        modifier = Modifier.weight(1f)
                    )
                    Glass3DStatTile(
                        label = "Study Tasks",
                        value = pendingTasksCount.toString(),
                        accent = Color(0xFFFBBF24),
                        modifier = Modifier.weight(1f)
                    )
                    Glass3DStatTile(
                        label = "MCQ Scores",
                        value = quizzesCount.toString(),
                        accent = Color(0xFF60A5FA),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun Glass3DStatTile(
    label: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.35f), Color.Transparent)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = accent
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.9f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Interactive 3D & Animated Subject Navigation Card for Accounting, Economics,
 * Principles of Commerce, Business Math, and Part 2 subjects.
 */
@Composable
fun Subject3DNavigationCard(
    subject: IComSubject,
    index: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = remember(subject.code) { getSubject3DTheme(subject.code) }

    // Staggered entrance animation
    val entranceProgress = remember { Animatable(0f) }
    LaunchedEffect(subject.code) {
        delay(index * 90L)
        entranceProgress.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    // Continuous subtle 3D levitation & orbital icon animation
    val infiniteTransition = rememberInfiniteTransition(label = "card_3d_${subject.code}")
    val phaseOffset = (index % 4) * 0.45f
    val wave by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600 + index * 250, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_${subject.code}"
    )

    val tiltX = sin(wave + phaseOffset) * 4.5f
    val tiltY = cos(wave + phaseOffset) * 5.5f
    val iconFloatY = sin(wave + phaseOffset) * 5f

    // Interactive 3D touch press tilt
    var isPressed by remember { mutableStateOf(false) }
    var pressOffset by remember { mutableStateOf(Offset.Zero) }

    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale_${subject.code}"
    )

    val animatedPressDepth by animateFloatAsState(
        targetValue = if (isPressed) 2f else 9f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "depth_${subject.code}"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .graphicsLayer {
                val entry = entranceProgress.value
                alpha = entry.coerceIn(0f, 1f)
                translationY = (1f - entry) * 70f
                scaleX = animatedScale * (0.85f + 0.15f * entry)
                scaleY = animatedScale * (0.85f + 0.15f * entry)

                // Combine continuous 3D levitation with interactive touch tilt
                val interactiveTiltX = if (isPressed) (pressOffset.y - 110f) * -0.08f else 0f
                val interactiveTiltY = if (isPressed) (pressOffset.x - 160f) * 0.08f else 0f
                rotationX = tiltX + interactiveTiltX
                rotationY = tiltY + interactiveTiltY
                cameraDistance = 14f * density
            }
            .pointerInput(subject.code) {
                detectTapGestures(
                    onPress = { offset ->
                        isPressed = true
                        pressOffset = offset
                        val released = tryAwaitRelease()
                        isPressed = false
                        if (released) {
                            onClick()
                        }
                    }
                )
            }
            .testTag("subject_card_${subject.code}")
    ) {
        // 1. 3D Extruded Bottom Edge (gives physical 3D block thickness)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset { IntOffset(0, animatedPressDepth.dp.roundToPx()) }
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            theme.primaryGradient.last(),
                            theme.bevelColor.copy(alpha = 0.75f),
                            theme.primaryGradient.first()
                        )
                    )
                )
        )

        // 2. Main 3D Elevated Surface
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (isPressed) 4.dp else 14.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = theme.bevelColor,
                    spotColor = theme.glowColor
                )
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = theme.primaryGradient,
                        start = Offset(0f, 0f),
                        end = Offset(600f, 500f)
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            theme.glowColor.copy(alpha = 0.7f),
                            Color.White.copy(alpha = 0.15f),
                            theme.bevelColor.copy(alpha = 0.5f)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            // 3. Custom 3D Subject Vector Artwork & Specular Lighting Canvas
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height

                // Ambient 3D top-right glow orb
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            theme.bevelColor.copy(alpha = 0.30f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.85f, h * 0.2f),
                        radius = w * 0.55f
                    ),
                    radius = w * 0.55f,
                    center = Offset(w * 0.85f, h * 0.2f)
                )

                // Subject-specific 3D Isometric Decorative Geometry
                when (subject.code) {
                    "ACC_1", "ACC_2" -> {
                        // 3D T-Account Ledger Grid
                        val cx = w * 0.82f
                        val cy = h * 0.42f + iconFloatY
                        drawLine(
                            color = theme.glowColor.copy(alpha = 0.25f),
                            start = Offset(cx - 35f, cy - 22f),
                            end = Offset(cx + 35f, cy - 22f),
                            strokeWidth = 4f
                        )
                        drawLine(
                            color = theme.glowColor.copy(alpha = 0.25f),
                            start = Offset(cx, cy - 22f),
                            end = Offset(cx, cy + 32f),
                            strokeWidth = 4f
                        )
                    }
                    "ECO_1" -> {
                        // 3D Demand & Supply Equilibrium Curves
                        val path1 = Path().apply {
                            moveTo(w * 0.68f, h * 0.20f + iconFloatY)
                            quadraticTo(w * 0.78f, h * 0.45f, w * 0.94f, h * 0.58f + iconFloatY)
                        }
                        val path2 = Path().apply {
                            moveTo(w * 0.68f, h * 0.58f + iconFloatY)
                            quadraticTo(w * 0.78f, h * 0.45f, w * 0.94f, h * 0.20f + iconFloatY)
                        }
                        drawPath(path1, color = theme.glowColor.copy(alpha = 0.28f), style = Stroke(width = 4f))
                        drawPath(path2, color = Color(0x44FBBF24), style = Stroke(width = 4f))
                    }
                    "POC_1" -> {
                        // 3D Interlocking Commerce Trade Rings
                        drawCircle(
                            color = theme.glowColor.copy(alpha = 0.22f),
                            radius = 28f,
                            center = Offset(w * 0.78f, h * 0.35f + iconFloatY),
                            style = Stroke(width = 3.5f)
                        )
                        drawCircle(
                            color = theme.bevelColor.copy(alpha = 0.28f),
                            radius = 28f,
                            center = Offset(w * 0.88f, h * 0.42f - iconFloatY),
                            style = Stroke(width = 3.5f)
                        )
                    }
                    "BM_1" -> {
                        // 3D Matrix Brackets & Exponential Curve
                        val bx = w * 0.75f
                        val by = h * 0.24f + iconFloatY
                        drawRect(
                            color = theme.glowColor.copy(alpha = 0.22f),
                            topLeft = Offset(bx, by),
                            size = Size(44f, 44f),
                            style = Stroke(width = 3.5f)
                        )
                    }
                }
            }

            // 4. Foreground Content with Levitating 3D Icon Badge
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // 3D Floating Pedestal Icon
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .graphicsLayer {
                                translationY = iconFloatY
                                rotationZ = sin(wave) * 4f
                            }
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(16.dp),
                                spotColor = theme.glowColor
                            )
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        theme.bevelColor,
                                        theme.primaryGradient.first()
                                    )
                                )
                            )
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = theme.icon,
                            contentDescription = subject.title,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Marks 3D Pill Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Black.copy(alpha = 0.28f),
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = theme.bevelColor.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    ) {
                        Text(
                            text = "${subject.totalMarks} Marks",
                            style = MaterialTheme.typography.labelLarge,
                            color = theme.accentBadgeColor,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = subject.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = subject.urduSubtitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = theme.glowColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = subject.keyTopics.take(3).joinToString(" • "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.80f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom 3D Action Strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.10f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${subject.chapters.size} Ch • ${subject.builtInMcqs.size} MCQs",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Explore",
                            style = MaterialTheme.typography.labelLarge,
                            color = theme.glowColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(theme.bevelColor.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Open ${subject.title}",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
