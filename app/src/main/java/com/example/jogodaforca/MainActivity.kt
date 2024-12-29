    package com.example.jogodaforca

    import android.media.MediaPlayer
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import com.example.jogodaforca.databinding.ActivityMainBinding

    // Definição da classe MainActivity que herda de AppCompatActivity
    class MainActivity : AppCompatActivity() {
        // Variável de ligação para acessar os elementos da interface do usuário
        private lateinit var binding: ActivityMainBinding
        private lateinit var mediaPlayer: MediaPlayer

        // Lista de palavras possíveis para o jogo
        private val palavras = listOf("tucano", "macaco", "girafa", "elefante", "cavalo", "cachorro", "papagaio",
            "capivara", "tubarão", "leopardo", "carrapato", "borboleta", "formiga", "gafanhoto", "caramujo", "zebra",
            "polvo", "baleia", "aguia", "carrapato", "panda", "camelo", "golfinho", "canguru", "tartaruga", "raposa")

        // Seleciona uma palavra aleatória da lista e a converte para maiúsculas
        private var palavraSelecionada = palavras.random().uppercase()

        // Cria um array de caracteres para representar a palavra oculta, inicialmente preenchida com '_'
        private var palavraOculta = CharArray(palavraSelecionada.length) { '_' }

        // Variável para contar o número de tentativas restantes
        private var tentativas = 8

        // Lista para armazenar as letras que já foram adivinhadas
        private val letrasAdivinhadas = mutableSetOf<Char>()

        /**
         * Função onCreate, chamada quando a atividade é criada.
         * @param savedInstanceState Instância salva do estado anterior da atividade (pode ser nulo).
         * @return Unit (não retorna nenhum valor).
         */
        override fun onCreate(savedInstanceState: Bundle?) {
            // Infla o layout usando o binding
            binding = ActivityMainBinding.inflate(layoutInflater)
            super.onCreate(savedInstanceState)
            setContentView(binding.root)

            mediaPlayer = MediaPlayer.create(this, R.raw.success)

            iniciarNovoJogo()


            // Atualiza a tela inicial com os valores padrão
            atualizarTela()

            // Define o comportamento do botão de adivinhar quando é clicado
            binding.buttonAdivinhar.setOnClickListener {
                // Obtém a entrada do usuário e a converte para maiúsculas
                val entrada = binding.editEntrada.text.toString().uppercase()
                // Verifica se a entrada não está vazia
                if (entrada.isNotEmpty()) {
                    // Pega a primeira letra da entrada
                    val letra = entrada[0]
                    // Verifica se a letra ainda não foi adivinhada
                    if (!letrasAdivinhadas.contains(letra)) {
                        // Adiciona a letra à lista de letras adivinhadas
                        letrasAdivinhadas.add(letra)
                        // Verifica se a palavra selecionada contém a letra
                        if (palavraSelecionada.contains(letra)) {
                            // Atualiza o array da palavra oculta com a letra correta nas posições certas
                            for (i in palavraSelecionada.indices) {
                                if (palavraSelecionada[i] == letra) {
                                    palavraOculta[i] = letra
                                }
                            }
                        } else {
                            // Se a letra não estiver na palavra, decrementa o número de tentativas
                            tentativas--
                        }
                        // Limpa o campo de entrada
                        binding.editEntrada.text.clear()
                        // Atualiza a tela com os novos valores
                        atualizarTela()
                    }
                }
            }

            binding.buttonNovoJogo.setOnClickListener {
                iniciarNovoJogo()
            }
        }

        private fun iniciarNovoJogo() {
            palavraSelecionada = palavras.random().uppercase()
            palavraOculta = CharArray(palavraSelecionada.length) { '_' }
            tentativas = 8
            letrasAdivinhadas.clear()
            binding.buttonAdivinhar.isEnabled = true
            binding.textStatus.text = ""
            atualizarTela()
        }

        /**
         * Função que atualiza a interface com a palavra oculta e o número de tentativas restantes.
         * Não possui parâmetros de entrada.
         * @return Unit (não retorna nenhum valor).
         */
        private fun atualizarTela() {
            // Exibe a palavra oculta com as letras adivinhadas
            binding.textOculto.text = palavraOculta.joinToString(" ")

            // Exibe o número de tentativas restantes
            binding.textTentativas.text = "Restam: $tentativas"

            // Exibe as letras chutadas
            binding.textLetrasChutadas.text = "Letras chutadas: ${letrasAdivinhadas.joinToString(", ")}"

            // Verifica se o jogador ganhou ou perdeu e atualiza o status
            if (!palavraOculta.contains('_')) {
                binding.textStatus.text = "Parabéns! $palavraSelecionada"
                binding.buttonAdivinhar.isEnabled = false
                tocarSomSucesso()
            } else if (tentativas <= 0) {
                binding.textStatus.text = "Perdeu! Era $palavraSelecionada"
                binding.buttonAdivinhar.isEnabled = false
            }
        }
        private fun tocarSomSucesso() {
            mediaPlayer.start()
        }
        override fun onDestroy() {
            super.onDestroy()
            mediaPlayer.release()
        }

    }
