package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.ui.components.*
import com.example.ui.theme.*
import java.util.concurrent.Executors

// Shared padding for the small action buttons so text has room on any phone font size
private val ActionButtonPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp)

@Composable
fun PhotoScreen(
  clientPhoto: Bitmap?,
  clientName: String,
  onClientNameChange: (String) -> Unit,
  onPhotoCaptured: (Bitmap?) -> Unit,
  onBack: () -> Unit,
  onContinue: () -> Unit,
  onLoadTestPhoto: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val scrollState = rememberScrollState()

  var hasCameraPermission by remember {
    mutableStateOf(
      ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    )
  }

  val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    hasCameraPermission = isGranted
  }

  val galleryLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri ->
    if (uri != null) {
      try {
        context.contentResolver.openInputStream(uri)?.use { stream ->
          val bitmap = android.graphics.BitmapFactory.decodeStream(stream)
          if (bitmap != null) {
            onPhotoCaptured(bitmap)
          }
        }
      } catch (e: Exception) {
        // error handling
      }
    }
  }

  var imageCapture: ImageCapture? by remember { mutableStateOf(null) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(TbsIvory)
  ) {
    TbsHeader(
      showBackArrow = true,
      onBackClick = onBack
    )

    TbsStepBar(currentStep = FlowStep.PHOTO)

    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Guidance Badge
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(6.dp))
          .background(TbsNavy.copy(alpha = 0.06f))
          .border(1.dp, TbsBorder, RoundedCornerShape(6.dp))
          .padding(horizontal = 14.dp, vertical = 10.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(6.dp)
              .clip(CircleShape)
              .background(TbsBrassGold)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Stand straight, arms relaxed, full body in frame.",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.5.sp,
            color = TbsNavy
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Viewfinder / Photo Container (520dp full vertical height, ContentScale.Fit so entire body is in frame)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(520.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFF14171E))
          .border(1.5.dp, if (clientPhoto != null) TbsBrassGold else TbsNavy, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
      ) {
        if (clientPhoto != null) {
          // Display captured / selected client photo in full using ContentScale.Fit so head and feet are completely visible
          Image(
            bitmap = clientPhoto.asImageBitmap(),
            contentDescription = "Client full-length photograph",
            contentScale = ContentScale.Fit,
            modifier = Modifier
              .fillMaxSize()
              .padding(6.dp)
          )

          // Photo Captured Pill
          Box(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(12.dp)
              .clip(RoundedCornerShape(20.dp))
              .background(TbsNavy.copy(alpha = 0.88f))
              .border(0.8.dp, TbsBrassGold.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = TbsBrassGold,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "PHOTO READY",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 1.0.sp,
                maxLines = 1,
                color = Color.White
              )
            }
          }
        } else if (hasCameraPermission) {
          // Live CameraX Preview
          AndroidView(
            factory = { ctx ->
              val previewView = PreviewView(ctx)
              val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
              cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                  it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val capture = ImageCapture.Builder()
                  .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                  .build()
                imageCapture = capture

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                try {
                  cameraProvider.unbindAll()
                  cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    capture
                  )
                } catch (exc: Exception) {
                  // camera bind failed
                }
              }, ContextCompat.getMainExecutor(ctx))
              previewView
            },
            modifier = Modifier.fillMaxSize()
          )

          // Silhouette overlay on top of camera preview
          BodySilhouetteGuide(guideColor = TbsBrassGold.copy(alpha = 0.8f))
        } else {
          // Permission request state or Camera placeholder
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
          ) {
            Icon(
              imageVector = Icons.Default.CameraAlt,
              contentDescription = null,
              tint = TbsBrassGold,
              modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "Camera Access Required",
              fontFamily = CormorantGaramond,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Enable camera to frame client head-to-toe, or upload a photo directly from device gallery.",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.Normal,
              fontSize = 12.sp,
              color = Color.White.copy(alpha = 0.7f),
              textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
              colors = ButtonDefaults.buttonColors(containerColor = TbsBrassGold, contentColor = TbsNavy),
              shape = RoundedCornerShape(4.dp),
              modifier = Modifier.testTag("enable_camera_button")
            ) {
              Text(
                text = "ENABLE CAMERA",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Action Buttons (Take Photo / Retake / Gallery / Test Photo)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (clientPhoto != null) {
          OutlinedButton(
            onClick = {
              // Clear photo to return to live camera or switch image
              onPhotoCaptured(null)
            },
            shape = RoundedCornerShape(6.dp),
            contentPadding = ActionButtonPadding,
            modifier = Modifier
              .weight(1f)
              .heightIn(min = 48.dp)
              .testTag("retake_photo_button")
          ) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = null,
              tint = TbsNavy,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "RETAKE",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.5.sp,
              letterSpacing = 0.6.sp,
              lineHeight = 15.sp,
              textAlign = TextAlign.Center,
              maxLines = 2,
              color = TbsNavy
            )
          }

          OutlinedButton(
            onClick = {
              galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
              )
            },
            shape = RoundedCornerShape(6.dp),
            contentPadding = ActionButtonPadding,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TbsNavy),
            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
              brush = androidx.compose.ui.graphics.SolidColor(TbsBorder)
            ),
            modifier = Modifier
              .weight(1f)
              .heightIn(min = 48.dp)
              .testTag("upload_gallery_button")
          ) {
            Icon(
              imageVector = Icons.Default.PhotoLibrary,
              contentDescription = null,
              tint = TbsNavy,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "FROM GALLERY",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.sp,
              letterSpacing = 0.6.sp,
              lineHeight = 15.sp,
              textAlign = TextAlign.Center,
              maxLines = 2
            )
          }
        } else {
          if (hasCameraPermission && imageCapture != null) {
            Button(
              onClick = {
                val capture = imageCapture ?: return@Button
                val executor = Executors.newSingleThreadExecutor()
                capture.takePicture(
                  executor,
                  object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(imageProxy: ImageProxy) {
                      val rotation = imageProxy.imageInfo.rotationDegrees
                      val buffer = imageProxy.planes[0].buffer
                      val bytes = ByteArray(buffer.remaining())
                      buffer.get(bytes)
                      val rawBitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                      val finalBitmap = if (rotation != 0 && rawBitmap != null) {
                        val matrix = android.graphics.Matrix().apply { postRotate(rotation.toFloat()) }
                        Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height, matrix, true)
                      } else {
                        rawBitmap
                      }
                      imageProxy.close()
                      if (finalBitmap != null) {
                        ContextCompat.getMainExecutor(context).execute {
                          onPhotoCaptured(finalBitmap)
                        }
                      }
                    }

                    override fun onError(exception: ImageCaptureException) {
                      // capture error
                    }
                  }
                )
              },
              shape = RoundedCornerShape(6.dp),
              contentPadding = ActionButtonPadding,
              colors = ButtonDefaults.buttonColors(containerColor = TbsNavy),
              modifier = Modifier
                .weight(1f)
                .heightIn(min = 48.dp)
                .testTag("take_photo_button")
            ) {
              Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "TAKE PHOTO",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                letterSpacing = 0.4.sp,
                lineHeight = 14.sp,
                textAlign = TextAlign.Center,
                maxLines = 2
              )
            }
          }

          OutlinedButton(
            onClick = {
              galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
              )
            },
            shape = RoundedCornerShape(6.dp),
            contentPadding = ActionButtonPadding,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TbsNavy),
            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
              brush = androidx.compose.ui.graphics.SolidColor(TbsBorder)
            ),
            modifier = Modifier
              .weight(1f)
              .heightIn(min = 48.dp)
              .testTag("upload_gallery_button")
          ) {
            Icon(
              imageVector = Icons.Default.PhotoLibrary,
              contentDescription = null,
              tint = TbsNavy,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "GALLERY",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.sp,
              letterSpacing = 0.4.sp,
              lineHeight = 14.sp,
              textAlign = TextAlign.Center,
              maxLines = 2
            )
          }

          OutlinedButton(
            onClick = onLoadTestPhoto,
            shape = RoundedCornerShape(6.dp),
            contentPadding = ActionButtonPadding,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TbsNavy),
            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
              brush = androidx.compose.ui.graphics.SolidColor(TbsBrassGold)
            ),
            modifier = Modifier
              .weight(1f)
              .heightIn(min = 48.dp)
              .testTag("load_test_photo_button")
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = TbsBrassGold,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "TEST PHOTO",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.sp,
              letterSpacing = 0.4.sp,
              lineHeight = 14.sp,
              textAlign = TextAlign.Center,
              maxLines = 2,
              color = TbsNavy
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Client Name Field (Optional, for session only)
      OutlinedTextField(
        value = clientName,
        onValueChange = onClientNameChange,
        label = {
          Text(
            text = "Client Name (Optional, for this session only)",
            fontFamily = InterFontFamily,
            fontSize = 12.sp,
            color = TbsCharcoalMuted
          )
        },
        placeholder = {
          Text(
            text = "e.g., Sheikh Ahmed / Hamdan",
            fontFamily = InterFontFamily,
            fontSize = 13.sp,
            color = TbsCharcoalMuted.copy(alpha = 0.5f)
          )
        },
        singleLine = true,
        shape = RoundedCornerShape(6.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = TbsNavy,
          unfocusedBorderColor = TbsBorder,
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("client_name_input")
      )

      Spacer(modifier = Modifier.height(16.dp))
    }

    // Sticky Bottom Bar
    TbsBottomBar(
      onBack = onBack,
      onContinue = onContinue,
      continueEnabled = clientPhoto != null,
      continueText = if (clientPhoto != null) "CONTINUE TO STYLE" else "TAKE PHOTO TO CONTINUE"
    )
  }
}