package dev.brunofelix.lumina.designsystem.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertLeftPositionInRootIsEqualTo
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.width
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.brunofelix.lumina.core.designsystem.R
import dev.brunofelix.lumina.designsystem.theme.LuminaTheme
import dev.brunofelix.lumina.designsystem.theme.OnGlass
import dev.brunofelix.lumina.designsystem.theme.size24
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LuminaComponentsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun luminaGlassTextField_showsPlaceholderWhenEmpty() {
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = "Email address",
                    leadingIcon = painterResource(R.drawable.ic_mail)
                )
            }
        }

        composeTestRule.onNodeWithText("Email address").assertIsDisplayed()
    }

    @Test
    fun luminaGlassTextField_hidesPlaceholderWhenFilled() {
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassTextField(
                    value = "nova@lumina.dev",
                    onValueChange = {},
                    placeholder = "Email address",
                    leadingIcon = painterResource(R.drawable.ic_mail)
                )
            }
        }

        composeTestRule.onNodeWithText("nova@lumina.dev").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email address").assertDoesNotExist()
    }

    @Test
    fun luminaGlassTextField_updatesValueCorrectly() {
        var textValue = ""
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassTextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    placeholder = "Email address",
                    leadingIcon = painterResource(R.drawable.ic_mail)
                )
            }
        }

        composeTestRule.onNodeWithText("Email address").performTextInput("nova@lumina.dev")
        textValue shouldBe "nova@lumina.dev"
    }

    @Test
    fun luminaGlassTextField_hazeStyle_updatesValueCorrectly() {
        var textValue = ""
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassTextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    placeholder = "Full Name",
                    leadingIcon = painterResource(R.drawable.ic_person_semibold),
                    style = LuminaGlassTextFieldStyle.Haze
                )
            }
        }

        composeTestRule.onNodeWithText("Full Name").performTextInput("Nova Star")
        textValue shouldBe "Nova Star"
    }

    @Test
    fun luminaGlassButton_primary_clicksCorrectly() {
        var clicked = false
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassButton(onClick = { clicked = true }) {
                    Text(text = "Login")
                }
            }
        }

        composeTestRule.onNodeWithText("Login").assertIsEnabled().performClick()
        clicked shouldBe true
    }

    @Test
    fun luminaGlassButton_disabled_ignoresClicks() {
        var clicked = false
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassButton(onClick = { clicked = true }, enabled = false) {
                    Text(text = "Login")
                }
            }
        }

        composeTestRule.onNodeWithText("Login").assertIsNotEnabled().performClick()
        clicked shouldBe false
    }

    @Test
    fun luminaGlassButton_secondary_clicksCorrectly() {
        var clicked = false
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassButton(
                    onClick = { clicked = true },
                    style = LuminaGlassButtonStyle.Secondary
                ) {
                    Text(text = "Sign in with Google")
                }
            }
        }

        composeTestRule.onNodeWithText("Sign in with Google").performClick()
        clicked shouldBe true
    }

    @Test
    fun luminaGlassButton_danger_clicksCorrectly() {
        var clicked = false
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassButton(
                    onClick = { clicked = true },
                    style = LuminaGlassButtonStyle.Danger
                ) {
                    Text(text = "Log Out")
                }
            }
        }

        composeTestRule.onNodeWithText("Log Out").assertIsEnabled().performClick()
        clicked shouldBe true
    }

    @Test
    fun luminaGlassButton_dangerDisabled_ignoresClicks() {
        var clicked = false
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassButton(
                    onClick = { clicked = true },
                    style = LuminaGlassButtonStyle.Danger,
                    enabled = false
                ) {
                    Text(text = "Log Out")
                }
            }
        }

        composeTestRule.onNodeWithText("Log Out").assertIsNotEnabled().performClick()
        clicked shouldBe false
    }

    @Test
    fun luminaGlassButton_loading_showsSpinnerAndIgnoresClicks() {
        var clicked = false
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassButton(
                    onClick = { clicked = true },
                    isLoading = true,
                    loadingContentDescription = "Logging in"
                ) {
                    Text(text = "Login")
                }
            }
        }

        composeTestRule.onNodeWithText("Login").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Logging in")
            .assertIsDisplayed()
            .assertIsNotEnabled()
            .performClick()
        clicked shouldBe false
    }

    @Test
    fun luminaSnackbar_showsMessage() {
        composeTestRule.setContent {
            LuminaTheme {
                LuminaSnackbar(message = "No internet connection")
            }
        }

        composeTestRule.onNodeWithText("No internet connection").assertIsDisplayed()
    }

    @Test
    fun luminaSnackbarHost_showsMessagesFromHostState() {
        val hostState = SnackbarHostState()
        composeTestRule.setContent {
            LuminaTheme {
                LuminaSnackbarHost(hostState = hostState)
                LaunchedEffect(Unit) {
                    hostState.showSnackbar(
                        message = "An account with this email already exists",
                        duration = SnackbarDuration.Indefinite
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("An account with this email already exists").assertIsDisplayed()
    }

    @Test
    fun luminaFloatingActionButton_exposesContentDescription() {
        composeTestRule.setContent {
            LuminaTheme {
                LuminaFloatingActionButton(
                    onClick = {},
                    icon = painterResource(R.drawable.ic_add_medium),
                    contentDescription = "Create New Deck"
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Create New Deck").assertIsDisplayed()
    }

    @Test
    fun luminaFloatingActionButton_clicksCorrectly() {
        var clicks = 0
        composeTestRule.setContent {
            LuminaTheme {
                LuminaFloatingActionButton(
                    onClick = { clicks++ },
                    icon = painterResource(R.drawable.ic_add_medium),
                    contentDescription = "Create New Deck"
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Create New Deck").performClick()
        clicks shouldBe 1
    }

    @Test
    fun luminaTopBar_showsBackButtonAndTitle() {
        composeTestRule.setContent {
            LuminaTheme {
                LuminaTopBar(
                    title = "Profile",
                    onBackClick = {},
                    backContentDescription = "Go back"
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Go back").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
    }

    @Test
    fun luminaTopBar_backClick_invokesCallback() {
        var clicks = 0
        composeTestRule.setContent {
            LuminaTheme {
                LuminaTopBar(
                    title = "Profile",
                    onBackClick = { clicks++ },
                    backContentDescription = "Go back"
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Go back").performClick()

        clicks shouldBe 1
    }

    @Test
    fun luminaTopBar_alignsBackArrowWithContentStartEdge() {
        composeTestRule.setContent {
            LuminaTheme {
                LuminaTopBar(
                    title = "Profile",
                    onBackClick = {},
                    backContentDescription = "Go back"
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Go back", useUnmergedTree = true)
            .assertLeftPositionInRootIsEqualTo(0.dp)
    }

    @Test
    fun luminaTopBar_actionClick_invokesCallback() {
        var clicks = 0
        setTopBarWithSaveAction(onSaveClick = { clicks++ })

        composeTestRule.onNodeWithContentDescription("Save deck").performClick()

        clicks shouldBe 1
    }

    @Test
    fun luminaTopBar_alignsActionGlyphWithContentEndEdge() {
        setTopBarWithSaveAction(onSaveClick = {})

        val rootWidth = composeTestRule.onRoot().getUnclippedBoundsInRoot().width
        composeTestRule
            .onNodeWithContentDescription("Save deck", useUnmergedTree = true)
            .assertLeftPositionInRootIsEqualTo(rootWidth - size24)
    }

    @Test
    fun luminaGlassTextField_customLeadingIconTint_rendersPlaceholder() {
        composeTestRule.setContent {
            LuminaTheme {
                LuminaGlassTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = "Deck name",
                    leadingIcon = painterResource(R.drawable.ic_style),
                    style = LuminaGlassTextFieldStyle.Haze,
                    leadingIconTint = OnGlass
                )
            }
        }

        composeTestRule.onNodeWithText("Deck name").assertIsDisplayed()
    }

    private fun setTopBarWithSaveAction(onSaveClick: () -> Unit) {
        composeTestRule.setContent {
            LuminaTheme {
                LuminaTopBar(
                    title = "Create Deck",
                    onBackClick = {},
                    backContentDescription = "Go back",
                    actions = {
                        IconButton(onClick = onSaveClick) {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = "Save deck",
                                modifier = Modifier.size(size24)
                            )
                        }
                    }
                )
            }
        }
    }
}
