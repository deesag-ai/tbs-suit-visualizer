package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("TBS Suit Visualizer", appName)
  }

  @Test
  fun `verify prompt builder follows exact template`() {
    val service = com.example.service.GeminiSuitService()
    val selection = com.example.model.ClientSuitSelection()
    val prompt = service.buildTailorPrompt(selection)
    org.junit.Assert.assertTrue(prompt.contains("Keep the person's face, skin tone, hair, beard, body shape, height, pose and background EXACTLY the same"))
    org.junit.Assert.assertTrue(prompt.contains("Change ONLY his clothing to:"))
    org.junit.Assert.assertTrue(prompt.contains("Suit:"))
    org.junit.Assert.assertTrue(prompt.contains("Jacket color:"))
    org.junit.Assert.assertTrue(prompt.contains("Shirt:"))
    org.junit.Assert.assertTrue(prompt.contains("Tie:"))
    org.junit.Assert.assertTrue(prompt.contains("Shoes:"))
  }

  @Test
  fun `verify consent loads test photo for testing`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val vm = com.example.viewmodel.VisualizerViewModel(app)
    vm.setVerbalConsentChecked(true)
    vm.acceptConsent()
    org.junit.Assert.assertNotNull(vm.uiState.value.clientPhoto)
  }
}
