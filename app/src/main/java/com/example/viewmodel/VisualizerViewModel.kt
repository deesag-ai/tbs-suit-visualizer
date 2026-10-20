package com.example.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.model.*
import com.example.service.GeminiSuitService
import com.example.util.WhatsAppHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppScreen {
  WELCOME,
  CONSENT,
  PHOTO,          // 01
  STYLE,          // 02
  DESIGN,         // 03
  COLORS,         // 04
  LOADING,
  PREVIEW,        // 05
  COMPARE_AND_SEND,
  SENT_CONFIRMATION
}

data class VisualizerUiState(
  val currentScreen: AppScreen = AppScreen.WELCOME,
  val clientName: String = "",
  val consentGiven: Boolean = false,
  val verbalConsentChecked: Boolean = false,
  val clientPhoto: Bitmap? = null,
  val selection: ClientSuitSelection = ClientSuitSelection(),
  val generatedLooks: List<GeneratedLook> = emptyList(),
  val activeLookIndex: Int = 0,
  val isGenerating: Boolean = false,
  val generationError: String? = null,
  val whatsAppNumber: String = "+971 ",
  val showConsultantSuggestions: Boolean = false,
  /** True while the app is in the background because WhatsApp was opened for sharing. */
  val isSharingActive: Boolean = false
) {
  val activeLook: GeneratedLook?
    get() = generatedLooks.getOrNull(activeLookIndex)
}

class VisualizerViewModel(application: Application) : AndroidViewModel(application) {

  private val _uiState = MutableStateFlow(VisualizerUiState())
  val uiState: StateFlow<VisualizerUiState> = _uiState.asStateFlow()

  private val geminiService = GeminiSuitService()

  fun navigateTo(screen: AppScreen) {
    _uiState.value = _uiState.value.copy(currentScreen = screen)
  }

  fun handleBackNavigation() {
    val state = _uiState.value
    when (state.currentScreen) {
      AppScreen.WELCOME -> { /* Top level */ }
      AppScreen.CONSENT -> navigateTo(AppScreen.WELCOME)
      AppScreen.PHOTO -> navigateTo(AppScreen.CONSENT)
      AppScreen.STYLE -> navigateTo(AppScreen.PHOTO)
      AppScreen.DESIGN -> navigateTo(AppScreen.STYLE)
      AppScreen.COLORS -> navigateTo(AppScreen.DESIGN)
      AppScreen.LOADING -> { /* Generating */ }
      AppScreen.PREVIEW -> navigateTo(AppScreen.COLORS)
      AppScreen.COMPARE_AND_SEND -> navigateTo(AppScreen.PREVIEW)
      AppScreen.SENT_CONFIRMATION -> startNewClient()
    }
  }

  fun setClientName(name: String) {
    _uiState.value = _uiState.value.copy(clientName = name)
  }

  fun setVerbalConsentChecked(checked: Boolean) {
    _uiState.value = _uiState.value.copy(verbalConsentChecked = checked)
  }

  fun acceptConsent() {
    if (_uiState.value.verbalConsentChecked) {
      val context = getApplication<Application>()
      // Pre-load the uploaded full-length test client photo so user can immediately test the app
      val initialPhoto = _uiState.value.clientPhoto ?: run {
        BitmapFactory.decodeResource(context.resources, R.drawable.img_client_test)
      }
      _uiState.value = _uiState.value.copy(
        consentGiven = true,
        clientPhoto = initialPhoto,
        currentScreen = AppScreen.PHOTO
      )
    }
  }

  fun cancelConsent() {
    _uiState.value = _uiState.value.copy(
      verbalConsentChecked = false,
      currentScreen = AppScreen.WELCOME
    )
  }

  fun setClientPhoto(bitmap: Bitmap?) {
    _uiState.value = _uiState.value.copy(clientPhoto = bitmap)
  }

  fun loadTestClientPhoto() {
    val context = getApplication<Application>()
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.img_client_test)
    if (bitmap != null) {
      setClientPhoto(bitmap)
    }
  }

  fun loadClientPhotoFromUri(uri: Uri) {
    viewModelScope.launch {
      try {
        val context = getApplication<Application>()
        context.contentResolver.openInputStream(uri)?.use { stream ->
          val bitmap = BitmapFactory.decodeStream(stream)
          if (bitmap != null) {
            setClientPhoto(bitmap)
          }
        }
      } catch (e: Exception) {
        // error reading image
      }
    }
  }

  fun updateStyle(style: SuitStyle) {
    val current = _uiState.value.selection
    // Smart defaults based on style:
    val updated = when (style) {
      SuitStyle.TUXEDO -> current.copy(
        style = style,
        lapel = LapelStyle.SHAWL,
        buttons = ButtonCount.ONE,
        pockets = PocketStyle.JETTED,
        vents = VentStyle.NONE,
        tieColor = SuitColorPalettes.tieColors.first { it.id == "black" }
      )
      SuitStyle.DOUBLE_BREASTED -> current.copy(
        style = style,
        lapel = LapelStyle.PEAK,
        buttons = ButtonCount.TWO,
        vents = VentStyle.DOUBLE
      )
      SuitStyle.THREE_PIECE -> current.copy(
        style = style,
        buttons = ButtonCount.TWO
      )
      SuitStyle.SINGLE_BREASTED -> current.copy(
        style = style
      )
    }
    _uiState.value = _uiState.value.copy(selection = updated)
  }

  fun updateLapel(lapel: LapelStyle) {
    _uiState.value = _uiState.value.copy(
      selection = _uiState.value.selection.copy(lapel = lapel)
    )
  }

  fun updateButtons(buttons: ButtonCount) {
    _uiState.value = _uiState.value.copy(
      selection = _uiState.value.selection.copy(buttons = buttons)
    )
  }

  fun updatePockets(pockets: PocketStyle) {
    _uiState.value = _uiState.value.copy(
      selection = _uiState.value.selection.copy(pockets = pockets)
    )
  }

  fun updateVents(vents: VentStyle) {
    _uiState.value = _uiState.value.copy(
      selection = _uiState.value.selection.copy(vents = vents)
    )
  }

  fun updateJacketColor(color: ColorOption) {
    val current = _uiState.value.selection
    val updated = if (current.matchJacketWithTrousers) {
      current.copy(jacketColor = color, trouserColor = color)
    } else {
      current.copy(jacketColor = color)
    }
    _uiState.value = _uiState.value.copy(selection = updated)
  }

  fun setMatchJacketWithTrousers(match: Boolean) {
    val current = _uiState.value.selection
    val updated = if (match) {
      current.copy(matchJacketWithTrousers = true, trouserColor = current.jacketColor)
    } else {
      current.copy(matchJacketWithTrousers = false)
    }
    _uiState.value = _uiState.value.copy(selection = updated)
  }

  fun updateTrouserColor(color: ColorOption) {
    _uiState.value = _uiState.value.copy(
      selection = _uiState.value.selection.copy(trouserColor = color, matchJacketWithTrousers = false)
    )
  }

  fun updateShirtColor(color: ColorOption) {
    _uiState.value = _uiState.value.copy(
      selection = _uiState.value.selection.copy(shirtColor = color)
    )
  }

  fun updateTieColor(color: ColorOption) {
    _uiState.value = _uiState.value.copy(
      selection = _uiState.value.selection.copy(tieColor = color)
    )
  }

  fun updateShoeColor(color: ColorOption) {
    _uiState.value = _uiState.value.copy(
      selection = _uiState.value.selection.copy(shoeColor = color)
    )
  }

  fun setShowConsultantSuggestions(show: Boolean) {
    _uiState.value = _uiState.value.copy(showConsultantSuggestions = show)
  }

  fun applySuggestion(suggestion: ConsultantSuggestion) {
    _uiState.value = _uiState.value.copy(
      selection = suggestion.selection,
      showConsultantSuggestions = false
    )
  }

  fun generateSuitLook() {
    val photo = _uiState.value.clientPhoto
    if (photo == null) {
      _uiState.value = _uiState.value.copy(
        generationError = "Please take or upload a client photo first.",
        currentScreen = AppScreen.PHOTO
      )
      return
    }
    val selection = _uiState.value.selection

    _uiState.value = _uiState.value.copy(
      isGenerating = true,
      generationError = null,
      currentScreen = AppScreen.LOADING
    )

    viewModelScope.launch {
      val result = geminiService.editSuitPhoto(photo, selection)

      result.fold(
        onSuccess = { generatedBitmap ->
          // Real AI generation directly from client's photo using Gemini image editing.
          // Never use sample, placeholder or stock image.
          val lookNumber = _uiState.value.generatedLooks.size + 1
          val newLook = GeneratedLook(
            id = "look_${System.currentTimeMillis()}",
            label = "Look $lookNumber · ${selection.style.displayName}",
            bitmap = generatedBitmap,
            selection = selection
          )

          // Keep up to 3 generated looks in memory
          val currentList = _uiState.value.generatedLooks.toMutableList()
          if (currentList.size >= 3) {
            currentList.removeAt(0)
          }
          currentList.add(newLook)

          _uiState.value = _uiState.value.copy(
            isGenerating = false,
            generationError = null,
            generatedLooks = currentList,
            activeLookIndex = currentList.size - 1,
            currentScreen = AppScreen.PREVIEW
          )
        },
        onFailure = { error ->
          _uiState.value = _uiState.value.copy(
            isGenerating = false,
            generationError = error.message ?: "Unable to tailor look with Gemini. Please tap Try Again.",
            currentScreen = AppScreen.PREVIEW
          )
        }
      )
    }
  }

  fun tryAnotherLook() {
    // Navigates back to Style screen while keeping the client's photo in memory
    _uiState.value = _uiState.value.copy(currentScreen = AppScreen.STYLE)
  }

  fun selectLook(index: Int) {
    if (index in _uiState.value.generatedLooks.indices) {
      _uiState.value = _uiState.value.copy(activeLookIndex = index)
    }
  }

  fun setWhatsAppNumber(number: String) {
    _uiState.value = _uiState.value.copy(whatsAppNumber = number)
  }

  fun sendToWhatsApp(context: android.content.Context) {
    val state = _uiState.value
    val look = state.activeLook ?: return
    val success = WhatsAppHelper.sendLookViaWhatsApp(
      context = context,
      rawNumber = state.whatsAppNumber,
      clientName = state.clientName,
      look = look
    )
    if (success) {
      // Mark that we are waiting for the user to return from WhatsApp.
      // The confirmation screen is shown in onReturnedFromWhatsApp() (onResume),
      // not immediately, so that "✓ Sent. Photo deleted." appears only after
      // the consultant comes back from WhatsApp.
      _uiState.value = _uiState.value.copy(isSharingActive = true)
    }
  }

  /**
   * Called from MainActivity.onResume when [isSharingActive] is true.
   * Deletes the cached share image, clears the flag, and navigates to the
   * confirmation screen so the consultant sees "✓ Sent. Photo deleted."
   */
  fun onReturnedFromWhatsApp(context: android.content.Context) {
    WhatsAppHelper.deleteCachedImages(context)
    _uiState.value = _uiState.value.copy(
      isSharingActive = false,
      currentScreen = AppScreen.SENT_CONFIRMATION
    )
  }

  fun startNewClient() {
    // Clear cached image files and reset all state with test photo ready for session
    val context = getApplication<Application>()
    WhatsAppHelper.deleteCachedImages(context)
    val testPhoto = BitmapFactory.decodeResource(context.resources, R.drawable.img_client_test)
    _uiState.value = VisualizerUiState(clientPhoto = testPhoto)
  }
}

