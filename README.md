# Aluguel de Carros - Projeto N1

Projeto desenvolvido para a disciplina de Desenvolvimento de Sistemas para Dispositivos Móveis do curso de Análise e Desenvolvimento de Sistemas.

O aplicativo permite realizar o controle básico de aluguel de veículos, utilizando recursos nativos do Android, persistência local e integração com os contatos do dispositivo.

## Funcionalidades

- Cadastro de veículos
- Listagem de veículos cadastrados
- Controle de status do veículo
    - DISPONIVEL
    - ALUGADO
- Acesso aos contatos do celular
- Pesquisa de contatos por nome
- Seleção de cliente através dos contatos do dispositivo
- Cadastro de nova locação
- Seleção de veículo disponível
- Definição da data de saída
- Definição da data prevista de entrega
- Cálculo automático da quantidade de diárias
- Cálculo automático do valor total da locação
- Listagem das locações ativas
- Visualização dos dados do cliente e do veículo
- Finalização da locação
- Liberação automática do veículo após a devolução

## Tecnologias utilizadas

- Kotlin
- Android Studio
- Jetpack Compose
- Material Design 3
- Room 3
- KSP
- Coroutines
- Flow
- Navigation Compose
- ViewModel
- Repository
- ContentResolver
- ContactsContract

## Arquitetura

O projeto foi organizado utilizando separação de responsabilidades entre as camadas da aplicação.

### UI

Responsável pelas telas desenvolvidas com Jetpack Compose.

Principais telas:

- VeiculosScreen
- CadastroVeiculoScreen
- ContatosScreen
- NovaLocacaoScreen
- LocacoesAtivasScreen

### ViewModel

Responsável por manter os dados utilizados pelas telas e executar as ações da aplicação.

- VeiculoViewModel
- LocacaoViewModel

### Repository

Responsável por intermediar a comunicação entre ViewModel e banco de dados.

- VeiculoRepository
- LocacaoRepository

### Banco de dados

A persistência local é realizada com Room.

Entidades:

- Veiculo
- Cliente
- Locacao

DAOs:

- VeiculoDao
- ClienteDao
- LocacaoDao

## Fluxo de uma locação

1. Um veículo é cadastrado com status DISPONIVEL.
2. O usuário acessa Nova Locação.
3. Um contato do celular é selecionado como cliente.
4. Um veículo disponível é selecionado.
5. São informadas as datas de saída e entrega.
6. O sistema calcula a quantidade de diárias.
7. O valor total é calculado automaticamente.
8. A locação é salva com status ATIVA.
9. O veículo passa para o status ALUGADO.
10. A locação aparece na tela de Locações Ativas.
11. Ao finalizar a locação, ela deixa de ser ativa.
12. O veículo retorna automaticamente para o status DISPONIVEL.

## Permissão de contatos

O aplicativo utiliza a permissão:

```xml
<uses-permission android:name="android.permission.READ_CONTACTS" />