package KalKul

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class KalKul : JFrame() {
    private val display: JTextField = JTextField()

    init {
        // Настройка окна калькулятора
        title = "Calculator"
        setSize(400, 600)
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null)

        // Создание панелей
        val panel = JPanel()
        panel.layout = BorderLayout()
        display.isEditable = false
        display.font = Font("Arial", Font.BOLD, 36)
        panel.add(display, BorderLayout.NORTH)

        val buttonPanel = JPanel()
        buttonPanel.layout = GridLayout(4, 4, 5, 5)

        // Создание кнопок
        val buttons = arrayOf(
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            ".", "0", "=", "+"
        )

        for (text in buttons) {
            val button = JButton(text)
            button.addActionListener(ButtonClickListener())
            buttonPanel.add(button)
        }

        panel.add(buttonPanel, BorderLayout.CENTER)
        contentPane.add(panel)

        // Инициализация состояния
        reset()
    }

    private var currentOperation: String? = null
    private var result: Double = 0.0
    private var isNewInput = true

    private inner class ButtonClickListener : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            val command = (e.source as JButton).text

            when {
                command.matches(Regex("\\d")) -> handleDigit(command)
                command == "." -> handleDot()
                command == "=" -> handleEquals()
                command in setOf("+", "-", "*", "/") -> handleOperation(command)
                else -> reset()
            }
        }
    }

    private fun handleDigit(digit: String) {
        if (isNewInput) {
            display.text = digit
            isNewInput = false
        } else {
            display.text += digit
        }
    }

    private fun handleDot() {
        if (isNewInput) {
            display.text = "0."
            isNewInput = false
        } else if (!display.text.contains(".")) {
            display.text += "."
        }
    }

    private fun handleOperation(operation: String) {
        if (currentOperation != null) {
            handleEquals()
        }
        result = display.text.toDouble()
        currentOperation = operation
        isNewInput = true
    }

    private fun handleEquals() {
        if (currentOperation != null) {
            val operand = display.text.toDouble()
            result = when (currentOperation) {
                "+" -> result + operand
                "-" -> result - operand
                "*" -> result * operand
                "/" -> result / operand
                else -> result
            }
            display.text = result.toString()
            currentOperation = null
            isNewInput = true
        }
    }

    private fun reset() {
        display.text = ""
        currentOperation = null
        result = 0.0
        isNewInput = true
    }
}

fun main() {
    SwingUtilities.invokeLater {
        KalKul().isVisible = true
    }
}