package com.aneury1.biblia

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class SearchMenuTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun menuSearchOpensMatchingPassageAndKeepsQuery() {
        compose.waitUntil(timeoutMillis = 30000) {
            compose.onAllNodesWithText("Un momento en la Palabra").fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNodeWithContentDescription("Abrir menú").performClick()
        compose.onNodeWithText("Buscar en la Biblia").performClick()
        compose.onNode(hasSetTextAction()).performTextInput("principio creó")
        compose.waitUntil(timeoutMillis = 10000) {
            compose.onAllNodesWithText("Génesis 1:1").fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNodeWithText("Génesis 1:1").performClick()
        compose.onAllNodesWithText("Capítulo 1").onFirst().assertExists()
        compose.onNodeWithContentDescription("Volver").performClick()
        compose.onNode(hasSetTextAction()).assertTextContains("principio creó")
    }
}
