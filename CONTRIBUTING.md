# Como contribuir

## Branches
- `main`: sempre compila e roda. Ninguém commita direto nela.
- `feature/<assunto>`: uma branch por tarefa (ex.: `feature/tela-favoritos`).
- `fix/<assunto>`: correções.

## Commits
Mensagens curtas no imperativo, com prefixo:

| Prefixo | Quando usar |
|---|---|
| `feat:` | funcionalidade nova |
| `fix:` | correção de bug |
| `refactor:` | mudança interna sem mudar o comportamento |
| `test:` | testes |
| `docs:` | README e documentos |
| `chore:` | build, dependências, configuração |

Exemplo: `feat: filtrar locais por barreira zero`

## Pull Requests
1. Abra a PR da sua branch para a `main` usando o modelo.
2. Outra pessoa da equipe revisa e comenta (code review).
3. Só faz merge com a revisão aprovada e o CI verde.

## Organização do código
- `domain/`: regras e modelos, sem Android.
- `data/`: Room, Retrofit, DataStore e o repositório.
- `ui/`: telas Compose e ViewModels. A tela só mostra estado e envia eventos.
- `di/`: módulos do Hilt.
