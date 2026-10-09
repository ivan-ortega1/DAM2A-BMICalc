package org.insbaixcamp.imca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.insbaixcamp.imca.ui.theme.CyberBlack
import org.insbaixcamp.imca.ui.theme.CyberCyan
import org.insbaixcamp.imca.ui.theme.CyberGreen
import org.insbaixcamp.imca.ui.theme.CyberGreenBright
import org.insbaixcamp.imca.ui.theme.CyberGreenDark
import org.insbaixcamp.imca.ui.theme.CyberGreenGlow
import org.insbaixcamp.imca.ui.theme.CyberGreenMuted
import org.insbaixcamp.imca.ui.theme.CyberOrange
import org.insbaixcamp.imca.ui.theme.CyberRed
import org.insbaixcamp.imca.ui.theme.CyberSurface
import org.insbaixcamp.imca.ui.theme.CyberSurfaceVariant
import org.insbaixcamp.imca.ui.theme.CyberTextDim
import org.insbaixcamp.imca.ui.theme.CyberTextSecondary
import org.insbaixcamp.imca.ui.theme.CyberYellow
import org.insbaixcamp.imca.ui.theme.ImcATheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImcATheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = CyberBlack
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        ScanlineBackground()
                        BMIScreen()
                    }
                }
            }
        }
    }
}

/**
 * Scanline / Grid effect overlay for CRT Hacker Terminal Aesthetic
 */
@Composable
fun ScanlineBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val scanlineSpacing = 8.dp.toPx()

        var y = 0f
        while (y < height) {
            drawLine(
                color = Color(0x0A00FF66),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
            y += scanlineSpacing
        }
    }
}

/**
 * Terminal blinking cursor component
 */
@Composable
fun BlinkingCursor(modifier: Modifier = Modifier, color: Color = CyberGreen) {
    val infiniteTransition = rememberInfiniteTransition(label = "cursor_blink")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Text(
        text = "█",
        color = color.copy(alpha = alpha),
        fontFamily = FontFamily.Monospace,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

/**
 * Main Hacker Cyberpunk BMI Terminal Screen
 */
@Composable
fun BMIScreen() {
    var name by remember { mutableStateOf("") }
    var weight by remember { mutableIntStateOf(80) }
    var height by remember { mutableIntStateOf(180) }
    var bmi by remember { mutableDoubleStateOf(0.0) }
    var isEvaluated by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- TOP TERMINAL HEADER ---
        CyberTerminalHeader()

        // --- SUBJECT PROFILE INPUT ---
        CyberCard(title = "01 // SUBJECT_IDENTIFICATION") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "usr@target:~$",
                    color = CyberCyan,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 8.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = {
                        Text(
                            text = "ENTER_SUBJECT_ID",
                            color = CyberTextDim,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CyberGreenBright,
                        unfocusedTextColor = CyberGreen,
                        focusedBorderColor = CyberGreen,
                        unfocusedBorderColor = CyberGreenMuted,
                        cursorColor = CyberGreenBright,
                        focusedContainerColor = CyberSurfaceVariant.copy(alpha = 0.5f),
                        unfocusedContainerColor = CyberSurface.copy(alpha = 0.3f)
                    ),
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // --- WEIGHT CONSOLE ---
        CyberCard(title = "02 // MASS_SPECS [WEIGHT]") {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TARGET_MASS:",
                        color = CyberTextSecondary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp
                    )

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$weight",
                            color = CyberGreenBright,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = " KG",
                            color = CyberGreenMuted,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Step Buttons (-10, -1, +1, +10)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CyberMiniButton(text = "-10") { if (weight > 30) weight -= 10 }
                    CyberMiniButton(text = "-1") { if (weight > 20) weight-- }
                    Spacer(modifier = Modifier.weight(1f))
                    CyberMiniButton(text = "+1") { if (weight < 300) weight++ }
                    CyberMiniButton(text = "+10") { if (weight < 290) weight += 10 }
                }
            }
        }

        // --- HEIGHT CONSOLE ---
        CyberCard(title = "03 // STATURE_SPECS [HEIGHT]") {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TARGET_STATURE:",
                        color = CyberTextSecondary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp
                    )

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$height",
                            color = CyberGreenBright,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = " CM",
                            color = CyberGreenMuted,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = height.toFloat(),
                    onValueChange = { height = it.toInt() },
                    valueRange = 100f..250f,
                    colors = SliderDefaults.colors(
                        thumbColor = CyberGreenBright,
                        activeTrackColor = CyberGreen,
                        inactiveTrackColor = CyberGreenDark,
                        activeTickColor = CyberCyan,
                        inactiveTickColor = CyberGreenMuted
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "100 CM", color = CyberTextDim, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                    Text(text = "175 CM", color = CyberTextDim, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                    Text(text = "250 CM", color = CyberTextDim, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                }
            }
        }

        // --- EXECUTE DIAGNOSTIC BUTTON ---
        Button(
            onClick = {
                val heightMeters = height / 100f
                bmi = (weight / (heightMeters * heightMeters)).toDouble()
                isEvaluated = true
            },
            shape = CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CyberGreen,
                contentColor = CyberBlack
            ),
            border = BorderStroke(1.dp, CyberGreenBright),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "> EXECUTE_BMI_DIAGNOSTIC",
                    color = CyberBlack,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                BlinkingCursor(color = CyberBlack)
            }
        }

        // --- DIAGNOSTIC REPORT CONSOLE ---
        AnimatedVisibility(
            visible = isEvaluated && bmi > 0,
            enter = fadeIn() + expandVertically()
        ) {
            CyberDiagnosticReport(
                subjectName = name.ifBlank { "ANONYMOUS_OPERATIVE" },
                heightCm = height,
                weightKg = weight,
                bmiValue = bmi
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Top Terminal Title / Status Bar
 */
@Composable
fun CyberTerminalHeader() {
    Surface(
        color = CyberSurface,
        shape = CutCornerShape(topEnd = 16.dp),
        border = BorderStroke(1.dp, CyberGreen),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(CyberGreenBright, shape = RoundedCornerShape(50))
                            .border(1.dp, CyberGreenGlow, RoundedCornerShape(50))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SYS://IMCA_BMI_TERMINAL",
                        color = CyberGreenBright,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "[ ONLINE ]",
                    color = CyberCyan,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "PROTOCOL: BIO_METRIC_EVALUATION_v2.0",
                    color = CyberTextDim,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp
                )
            }
        }
    }
}

/**
 * Styled Cyber Card Container
 */
@Composable
fun CyberCard(
    title: String,
    content: @Composable () -> Unit
) {
    Surface(
        color = CyberSurface,
        shape = CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp),
        border = BorderStroke(1.dp, CyberGreenMuted),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Text(
                    text = title,
                    color = CyberGreen,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            content()
        }
    }
}

/**
 * Mini cyber modifier button
 */
@Composable
fun CyberMiniButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width = 54.dp, height = 36.dp)
            .background(CyberSurfaceVariant, CutCornerShape(4.dp))
            .border(1.dp, CyberGreenMuted, CutCornerShape(4.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            color = CyberGreenBright,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Cyber Diagnostic Output Console
 */
@Composable
fun CyberDiagnosticReport(
    subjectName: String,
    heightCm: Int,
    weightKg: Int,
    bmiValue: Double
) {
    // Determine category specs
    val (statusText, statusColor, asciiMeter, recommendation) = when {
        bmiValue < 18.5 -> Quadruple(
            "STATUS: UNDERWEIGHT // MASS_DEFICIT",
            CyberCyan,
            "[███░░░░░░░░░░░░░░░░]",
            "Caloric surplus recommended. Increase structural density."
        )
        bmiValue in 18.5..24.99 -> Quadruple(
            "STATUS: OPTIMAL // SYSTEM_BALANCED",
            CyberGreenBright,
            "[█████████░░░░░░░░░░]",
            "Optimal mass specs detected. System parameters fully aligned."
        )
        bmiValue in 25.0..29.99 -> Quadruple(
            "STATUS: OVERWEIGHT // ELEVATED_MASS",
            CyberYellow,
            "[██████████████░░░░░]",
            "Elevated kinetic load. Cardiovascular tuning advised."
        )
        else -> Quadruple(
            "ALERT: OBESE // CRITICAL_MASS_OVERFLOW",
            CyberRed,
            "[███████████████████]",
            "Critical threshold exceeded. Urgent biometric reset required."
        )
    }

    Surface(
        color = CyberBlack,
        shape = CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp),
        border = BorderStroke(1.5.dp, statusColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "=== [ DIAGNOSTIC_REPORT ] ===",
                    color = statusColor,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "EVAL_OK",
                    color = CyberGreenBright,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subject Info
            Text(
                text = "> SUBJECT: $subjectName",
                color = CyberTextSecondary,
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp
            )
            Text(
                text = "> HEIGHT: $heightCm CM | WEIGHT: $weightKg KG",
                color = CyberTextSecondary,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // BMI Output Display
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "> BMI_INDEX: ",
                    color = CyberGreen,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = String.format(Locale.US, "%.2f", bmiValue),
                    color = statusColor,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " kg/m²",
                    color = CyberTextDim,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Status Badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(statusColor.copy(alpha = 0.15f), CutCornerShape(4.dp))
                    .border(1.dp, statusColor, CutCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = statusText,
                    color = statusColor,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ASCII Gauge
            Column {
                Text(
                    text = "MASS_DENSITY_METER:",
                    color = CyberTextDim,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp
                )
                Text(
                    text = asciiMeter,
                    color = statusColor,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Recommendation
            Text(
                text = "> DIAGNOSTIC_LOG: $recommendation",
                color = CyberTextSecondary,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

@Preview(showBackground = true, backgroundColor = 0xFF080C09)
@Composable
fun GreetingPreview() {
    ImcATheme {
        Box(modifier = Modifier.fillMaxSize()) {
            ScanlineBackground()
            BMIScreen()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF080C09)
@Composable
fun EvaluatedDiagnosticPreview() {
    ImcATheme {
        Box(modifier = Modifier.fillMaxSize()) {
            ScanlineBackground()
            Column(modifier = Modifier.padding(16.dp)) {
                CyberDiagnosticReport(
                    subjectName = "NEO_ZERO",
                    heightCm = 180,
                    weightKg = 80,
                    bmiValue = 24.69
                )
            }
        }
    }
}