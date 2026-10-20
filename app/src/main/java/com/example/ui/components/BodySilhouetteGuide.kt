package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.TbsBrassGold

@Composable
fun BodySilhouetteGuide(
  modifier: Modifier = Modifier,
  guideColor: Color = TbsBrassGold.copy(alpha = 0.85f)
) {
  Canvas(modifier = modifier.fillMaxSize()) {
    val w = size.width
    val h = size.height

    val centerX = w / 2f
    val strokeWidth = 2.dp.toPx()
    val dashedEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)

    val strokeStyle = Stroke(
      width = strokeWidth,
      pathEffect = dashedEffect,
      cap = StrokeCap.Round
    )

    // 1. Head guide oval
    val headCenterX = centerX
    val headCenterY = h * 0.16f
    val headRadiusX = w * 0.13f
    val headRadiusY = h * 0.08f

    drawOval(
      color = guideColor,
      topLeft = Offset(headCenterX - headRadiusX, headCenterY - headRadiusY),
      size = Size(headRadiusX * 2, headRadiusY * 2),
      style = strokeStyle
    )

    // 2. Neck line
    val neckTopY = headCenterY + headRadiusY
    val neckBottomY = neckTopY + (h * 0.035f)

    // 3. Shoulders and Torso Path
    val path = Path().apply {
      // Shoulders
      val shoulderHalfWidth = w * 0.32f
      val shoulderY = neckBottomY + (h * 0.02f)

      moveTo(centerX - shoulderHalfWidth, shoulderY)
      // Collar contour
      quadraticTo(centerX, neckBottomY, centerX + shoulderHalfWidth, shoulderY)

      // Arms down (relaxed at sides)
      val elbowY = shoulderY + (h * 0.18f)
      val wristY = elbowY + (h * 0.16f)

      // Right arm outer
      lineTo(centerX + shoulderHalfWidth + (w * 0.04f), elbowY)
      lineTo(centerX + shoulderHalfWidth + (w * 0.02f), wristY)

      // Waist taper
      val waistY = shoulderY + (h * 0.22f)
      val waistHalfWidth = w * 0.22f

      // Trouser legs
      val hipY = shoulderY + (h * 0.32f)
      val hipHalfWidth = w * 0.24f

      val kneeY = hipY + (h * 0.20f)
      val ankleY = h * 0.90f
      val feetY = h * 0.94f

      // Right leg outer
      lineTo(centerX + hipHalfWidth, hipY)
      lineTo(centerX + (w * 0.18f), kneeY)
      lineTo(centerX + (w * 0.15f), ankleY)
      lineTo(centerX + (w * 0.16f), feetY)

      // Right shoe
      lineTo(centerX + (w * 0.06f), feetY)
      lineTo(centerX + (w * 0.04f), ankleY)

      // Inseam
      lineTo(centerX + (w * 0.03f), kneeY)
      lineTo(centerX, hipY + (h * 0.04f))

      // Left leg inner
      lineTo(centerX - (w * 0.03f), kneeY)
      lineTo(centerX - (w * 0.04f), ankleY)
      lineTo(centerX - (w * 0.06f), feetY)

      // Left shoe
      lineTo(centerX - (w * 0.16f), feetY)
      lineTo(centerX - (w * 0.15f), ankleY)
      lineTo(centerX - (w * 0.18f), kneeY)
      lineTo(centerX - hipHalfWidth, hipY)

      // Left arm outer
      lineTo(centerX - shoulderHalfWidth - (w * 0.02f), wristY)
      lineTo(centerX - shoulderHalfWidth - (w * 0.04f), elbowY)
      lineTo(centerX - shoulderHalfWidth, shoulderY)
    }

    drawPath(path = path, color = guideColor, style = strokeStyle)

    // Eye level horizontal subtle guide mark
    val eyeLevelY = headCenterY
    drawLine(
      color = guideColor.copy(alpha = 0.5f),
      start = Offset(centerX - headRadiusX * 1.4f, eyeLevelY),
      end = Offset(centerX + headRadiusX * 1.4f, eyeLevelY),
      strokeWidth = 1.5.dp.toPx()
    )

    // Floor ground guide line
    val floorY = h * 0.95f
    drawLine(
      color = guideColor.copy(alpha = 0.7f),
      start = Offset(w * 0.15f, floorY),
      end = Offset(w * 0.85f, floorY),
      strokeWidth = 2.dp.toPx()
    )
  }
}
