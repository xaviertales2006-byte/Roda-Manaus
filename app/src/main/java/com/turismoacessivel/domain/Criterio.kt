package com.turismoacessivel.domain

/** Os seis critérios de acessibilidade avaliados em cada local. */
enum class Criterio(val rotulo: String, val descricao: String) {
    RAMPA("Rampa de acesso", "Entrada sem degraus para cadeira de rodas e carrinho de bebê"),
    BANHEIRO_ADAPTADO("Banheiro adaptado", "Banheiro com espaço e barras de apoio"),
    PISO_TATIL("Piso tátil", "Piso de orientação para pessoas cegas ou com baixa visão"),
    BRAILLE("Cardápio ou sinalização em braille", "Informações em braille para leitura tátil"),
    LIBRAS("Funcionários com Libras", "Atendimento na Língua Brasileira de Sinais"),
    ILUMINACAO("Iluminação para baixa visão", "Ambiente bem iluminado e com bom contraste"),
}
