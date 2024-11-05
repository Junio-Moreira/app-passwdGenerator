package br.com.moreira.passwordgenerator

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import android.os.Build


class MainActivity : AppCompatActivity() {

    private lateinit var passwordTextView: TextView
    private lateinit var uppercaseSwitch: SwitchMaterial
    private lateinit var numbersSwitch: SwitchMaterial
    private lateinit var specialCharsSwitch: SwitchMaterial
    private lateinit var excludeSimilarCharsSwitch: SwitchMaterial
    private lateinit var lengthSeekBar: SeekBar
    private lateinit var lengthLabelTextView: TextView
    private lateinit var generateButton: Button
    private lateinit var copyButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar a cor da barra de status para coincidir com a cor do aplicativo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        }

        // Inicializando as views
        passwordTextView = findViewById(R.id.passwordTextView)
        uppercaseSwitch = findViewById(R.id.uppercaseSwitch)
        numbersSwitch = findViewById(R.id.numbersSwitch)
        specialCharsSwitch = findViewById(R.id.usespecialcharsSwitch)
        excludeSimilarCharsSwitch = findViewById(R.id.excludesimilarcharsSwitch)
        lengthSeekBar = findViewById(R.id.lengthSeekBar)
        lengthLabelTextView = findViewById(R.id.lengthLabelTextView)
        generateButton = findViewById(R.id.generateButton)
        copyButton = findViewById(R.id.copyButton)

        // Configurações iniciais
        lengthSeekBar.max = 30  // Configura o máximo para 30 caracteres
        lengthSeekBar.progress = 10  // Valor inicial
        lengthLabelTextView.text = getString(R.string.length_label, lengthSeekBar.progress)

        // Listener para atualizar o TextView com o valor da SeekBar
        lengthSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Atualize o TextView com o valor da SeekBar
                lengthLabelTextView.text = getString(R.string.length_label, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Função para o botão de gerar senha
        generateButton.setOnClickListener {
            val length = lengthSeekBar.progress
            val includeUppercase = uppercaseSwitch.isChecked
            val includeNumbers = numbersSwitch.isChecked
            val includeSpecialChars = specialCharsSwitch.isChecked
            val excludeSimilarChars = excludeSimilarCharsSwitch.isChecked

            // Gerar a senha com as configurações selecionadas
            val password = generatePassword(length, includeUppercase, includeNumbers, includeSpecialChars, excludeSimilarChars)
            passwordTextView.text = password
        }

        // Função para o botão de copiar
        copyButton.setOnClickListener {
            val password = passwordTextView.text.toString()
            copyToClipboard(password)
            Toast.makeText(this, "Senha copiada para a área de transferência", Toast.LENGTH_SHORT).show()
        }
    }

    // Função para gerar a senha
    private fun generatePassword(length: Int, includeUppercase: Boolean, includeNumbers: Boolean, includeSpecialChars: Boolean, excludeSimilarChars: Boolean): String {
        val lowercaseChars = "abcdefghijklmnopqrstuvwxyz"
        val uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val numberChars = "0123456789"
        val specialChars = "!@#\$%^&*()-_=+[]{}|;:'\",.<>?/`~"
        val similarChars = "il1Lo0O"

        var charPool = lowercaseChars
        if (includeUppercase) charPool += uppercaseChars
        if (includeNumbers) charPool += numberChars
        if (includeSpecialChars) charPool += specialChars

        if (excludeSimilarChars) {
            charPool = charPool.filterNot { it in similarChars }
        }

        return (1..length).map { charPool.random() }.joinToString("")
    }

    // Função para copiar texto para a área de transferência
    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("password", text)
        clipboard.setPrimaryClip(clip)
    }
}
