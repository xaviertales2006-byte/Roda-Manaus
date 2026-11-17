# Roda Manaus: roteiro do pitch (5 minutos)

Equipe: Tales Silva Xavier, Guilherme Chaves Pinheiro, Lincoln Figueiredo da Silva e João Gabriel de Aguiar Negreiros.

| Tempo | Quem | Conteúdo |
|---|---|---|
| 0:00 a 0:45 | João Gabriel | **O problema.** "Tem rampa nesse museu?" Pessoas com deficiência, famílias com carrinho de bebê e pessoas com mobilidade reduzida não têm informação confiável antes de sair de casa. |
| 0:45 a 1:30 | Guilherme | **A solução.** Mapa de locais avaliados por inclusão, com checklist de 6 critérios, nota de 0 a 5 e selo "Barreira zero". |
| 1:30 a 3:30 | Guilherme (com o celular) | **Demo ao vivo.** Abrir o app, filtrar por categoria, abrir um local, mostrar o checklist e "Como chegar". Fazer uma avaliação com foto. Ativar alto contraste e aumentar o texto. |
| 3:30 a 4:15 | Tales | **Como foi feito.** MVVM, Hilt, Retrofit com a API Overpass do OpenStreetMap, Room (funciona offline) e DataStore (ajustes). Mostrar o histórico de commits e PRs. |
| 4:15 a 5:00 | Lincoln | **Aprendizados e próximos passos.** O que deu certo, o que foi difícil, e o que vem depois (enviar avaliações para um servidor, mapa dentro do app). |

## Preparação
- [ ] Rodar o app uma vez com internet antes, para os dados ficarem salvos.
- [ ] Ter um celular com TalkBack ligado para mostrar a leitura de tela, se der tempo.
- [ ] Plano B: gravar a demo em vídeo.
- [ ] Ensaiar com cronômetro.

## Perguntas que a banca pode fazer
- De onde vêm os dados? (OpenStreetMap, API pública Overpass.)
- Por que o score de alguns locais é baixo ou vazio? (Muitos locais ainda não têm tags de acessibilidade no mapa; a comunidade completa avaliando.)
- O que acontece sem internet? (Os locais já baixados ficam no Room.)
- Por que Hilt? (Injeção de dependência, ViewModels e repositório testáveis.)
