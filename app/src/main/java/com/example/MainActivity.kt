package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.TbsIvory
import com.example.ui.theme.TbsTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.VisualizerViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      TbsTheme {
        Scaffold(
          contentWindowInsets = WindowInsets.safeDrawing,
          modifier = Modifier
            .fillMaxSize()
            .background(TbsIvory)
        ) { innerPadding ->
          TbsApp(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
          )
        }
      }
    }
  }
}

@Composable
fun TbsApp(
  viewModel: VisualizerViewModel = viewModel(),
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current

  // Detect when the consultant returns from WhatsApp (onResume).
  // If sharing was in progress, delete the cached image and show the
  // "✓ Sent. Photo deleted." confirmation screen at that point.
  DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == Lifecycle.Event.ON_RESUME && viewModel.uiState.value.isSharingActive) {
        viewModel.onReturnedFromWhatsApp(context)
      }
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
  }

  // Mandatory BackHandler for custom state-based navigation
  if (uiState.currentScreen != AppScreen.WELCOME) {
    BackHandler {
      viewModel.handleBackNavigation()
    }
  }

  Box(modifier = modifier) {
    when (uiState.currentScreen) {
      AppScreen.WELCOME -> {
        WelcomeScreen(
          onStartNewClient = {
            viewModel.navigateTo(AppScreen.CONSENT)
          }
        )
      }

      AppScreen.CONSENT -> {
        ConsentScreen(
          verbalConsentChecked = uiState.verbalConsentChecked,
          onVerbalConsentChanged = { viewModel.setVerbalConsentChecked(it) },
          onClientAgrees = { viewModel.acceptConsent() },
          onCancel = { viewModel.cancelConsent() }
        )
      }

      AppScreen.PHOTO -> {
        PhotoScreen(
          clientPhoto = uiState.clientPhoto,
          clientName = uiState.clientName,
          onClientNameChange = { viewModel.setClientName(it) },
          onPhotoCaptured = { viewModel.setClientPhoto(it) },
          onBack = { viewModel.navigateTo(AppScreen.CONSENT) },
          onContinue = { viewModel.navigateTo(AppScreen.STYLE) },
          onLoadTestPhoto = { viewModel.loadTestClientPhoto() }
        )
      }

      AppScreen.STYLE -> {
        StyleScreen(
          selectedStyle = uiState.selection.style,
          onStyleSelected = { viewModel.updateStyle(it) },
          onBack = { viewModel.navigateTo(AppScreen.PHOTO) },
          onContinue = { viewModel.navigateTo(AppScreen.DESIGN) }
        )
      }

      AppScreen.DESIGN -> {
        DesignScreen(
          selection = uiState.selection,
          onLapelSelected = { viewModel.updateLapel(it) },
          onButtonsSelected = { viewModel.updateButtons(it) },
          onPocketsSelected = { viewModel.updatePockets(it) },
          onVentsSelected = { viewModel.updateVents(it) },
          onBack = { viewModel.navigateTo(AppScreen.STYLE) },
          onContinue = { viewModel.navigateTo(AppScreen.COLORS) }
        )
      }

      AppScreen.COLORS -> {
        ColorsScreen(
          selection = uiState.selection,
          onJacketColorSelected = { viewModel.updateJacketColor(it) },
          onMatchJacketToggled = { viewModel.setMatchJacketWithTrousers(it) },
          onTrouserColorSelected = { viewModel.updateTrouserColor(it) },
          onShirtColorSelected = { viewModel.updateShirtColor(it) },
          onTieColorSelected = { viewModel.updateTieColor(it) },
          onShoeColorSelected = { viewModel.updateShoeColor(it) },
          showConsultantSuggestions = uiState.showConsultantSuggestions,
          onShowSuggestionsChanged = { viewModel.setShowConsultantSuggestions(it) },
          onApplySuggestion = { viewModel.applySuggestion(it) },
          onBack = { viewModel.navigateTo(AppScreen.DESIGN) },
          onContinue = { viewModel.generateSuitLook() }
        )
      }

      AppScreen.LOADING -> {
        LoadingScreen()
      }

      AppScreen.PREVIEW -> {
        PreviewScreen(
          look = uiState.activeLook,
          selection = uiState.selection,
          generationError = uiState.generationError,
          lookCount = uiState.generatedLooks.size,
          onTryAnotherLook = { viewModel.tryAnotherLook() },
          onCompareLooks = { viewModel.navigateTo(AppScreen.COMPARE_AND_SEND) },
          onRetryGeneration = { viewModel.generateSuitLook() },
          onBack = { viewModel.navigateTo(AppScreen.COLORS) }
        )
      }

      AppScreen.COMPARE_AND_SEND -> {
        CompareAndSendScreen(
          looks = uiState.generatedLooks,
          selectedLookIndex = uiState.activeLookIndex,
          onSelectLook = { viewModel.selectLook(it) },
          whatsAppNumber = uiState.whatsAppNumber,
          onWhatsAppNumberChange = { viewModel.setWhatsAppNumber(it) },
          onSendToWhatsApp = { viewModel.sendToWhatsApp(context) },
          onBack = { viewModel.navigateTo(AppScreen.PREVIEW) }
        )
      }

      AppScreen.SENT_CONFIRMATION -> {
        SentConfirmationScreen(
          onNewClient = { viewModel.startNewClient() }
        )
      }
    }
  }
}
