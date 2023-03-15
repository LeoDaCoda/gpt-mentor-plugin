package com.github.jcraane.gptmentorplugin.configuration

import com.github.jcraane.gptmentorplugin.security.GptMentorCredentialsManager
import com.intellij.openapi.options.Configurable
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.layout.panel
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.JPanel

class GptMentorConfigurable : Configurable {
    private lateinit var settingsPanel: JPanel
    private var openAiApiKey = JBPasswordField()

    private var explainCodePrompt = createTextArea()
    private var createUnitTestPrompt = createTextArea()
    private var improveCodePrompt = createTextArea()
    private var reviewCodePrompt = createTextArea()
    private var addDocsPrompt = createTextArea()
    private var chatPrompt = createTextArea()

    private val config: GptMentorSettingsState = GptMentorSettingsState.getInstance()

    private fun createTextArea() = JBTextArea().apply {
        lineWrap = true
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(JBColor(Color.BLACK, Color.WHITE), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        )
    }


    override fun createComponent(): JComponent {
        settingsPanel = JPanel()
        return createFromConfig()
    }

    private fun createFromConfig(): JComponent {
        openAiApiKey.text = getPassword()
        explainCodePrompt.text = config.systemPromptExplainCode
        createUnitTestPrompt.text = config.systemPromptCreateUnitTest
        improveCodePrompt.text = config.systemPromptImproveCode
        reviewCodePrompt.text = config.systemPromptReviewCode
        addDocsPrompt.text = config.systemPromptAddDocs
        chatPrompt.text = config.systemPromptChat

        settingsPanel = panel {
            row("OpenAI API Key") {
                component(openAiApiKey)
            }
            row("Explain Code Prompt") {
                component(explainCodePrompt)
            }
            row("Create Unit Test Prompt") {
                component(createUnitTestPrompt)
            }
            row("Improve Code Prompt") {
                component(improveCodePrompt)
            }
            row("Review Code Prompt") {
                component(reviewCodePrompt)
            }
            row("Add Docs Prompt") {
                component(addDocsPrompt)
            }
            row("Chat Prompt") {
                component(chatPrompt)
            }
        }
        return settingsPanel
    }

    private fun getPassword() = GptMentorCredentialsManager.getPassword() ?: "YOUR_API_KEY"

    override fun isModified(): Boolean {
        var modified = false
        val text = openAiApiKey.text
        if (text.isNotBlank()) {
            modified = modified || text != getPassword()
        }

        modified = modified || explainCodePrompt.text != config.systemPromptExplainCode
        modified = modified || createUnitTestPrompt.text != config.systemPromptCreateUnitTest
        modified = modified || improveCodePrompt.text != config.systemPromptImproveCode
        modified = modified || reviewCodePrompt.text != config.systemPromptReviewCode
        modified = modified || addDocsPrompt.text != config.systemPromptAddDocs
        modified = modified || chatPrompt.text != config.systemPromptChat

        return modified
    }

    override fun apply() {
        if (isModified.not()) {
            return
        }

        config.systemPromptExplainCode = explainCodePrompt.text
        config.systemPromptCreateUnitTest = createUnitTestPrompt.text
        config.systemPromptImproveCode = improveCodePrompt.text
        config.systemPromptImproveCode = reviewCodePrompt.text
        config.systemPromptAddDocs = addDocsPrompt.text
        config.systemPromptChat = chatPrompt.text

        GptMentorCredentialsManager.setPassword(openAiApiKey.text)
    }

    override fun getDisplayName(): String {
        return "GPT-Mentor"
    }
}
