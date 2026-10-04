# Aluguel de Carros - Projeto N1

Projeto desenvolvido para a disciplina de Desenvolvimento de Sistemas para Dispositivos Móveis do curso de Análise e Desenvolvimento de Sistemas.

O aplicativo foi desenvolvido para auxiliar pequenos locadores de veículos no gerenciamento da frota, clientes e locações, utilizando recursos nativos do Android, persistência local, integração com os contatos do dispositivo e consumo de API REST.

## Funcionalidades

- Dashboard inicial com locações ativas
- Cadastro de veículos
- Listagem de veículos cadastrados
- Controle de status do veículo:
  - DISPONIVEL
  - ALUGADO
  - MANUTENCAO
- Validação de placa brasileira:
  - Padrão antigo: AAA-1234
  - Padrão Mercosul: AAA1A23
- Validação do valor da diária
- Acesso aos contatos do dispositivo
- Pesquisa de contatos por nome
- Seleção de cliente através dos contatos do celular
- Solicitação da permissão READ_CONTACTS em tempo de execução
- Tratamento da permissão de contatos
- Cadastro de nova locação
- Seleção apenas de veículos disponíveis
- Seleção visual das datas utilizando DatePicker
- Cálculo automático da quantidade de diárias
- Cálculo automático do valor total da locação
- Listagem das locações ativas
- Visualização dos dados do cliente e do veículo
- Cálculo dinâmico dos dias faltantes para devolução
- Destaque visual de locações em atraso
- Finalização da locação
- Liberação automática do veículo após a devolução
- Sincronização de locações com API REST utilizando Retrofit 2

## Tecnologias utilizadas

- Kotlin
- Android Studio
- Jetpack Compose
- Material Design 3
- Room 3
- KSP
- Kotlin Coroutines
- Flow
- StateFlow
- Navigation Compose
- ViewModel
- SavedStateHandle
- Repository
- ContentResolver
- ContactsContract
- Retrofit 2
- Gson Converter

## Arquitetura

O projeto utiliza o padrão MVVM, separando as responsabilidades entre interface, gerenciamento de estado e fontes de dados.

Fluxo simplificado:

```text
Jetpack Compose
      ↓
ViewModel
      ↓
Repository
   ↙      ↘
Room    Retrofit
```

### UI

Responsável pelas telas desenvolvidas utilizando Jetpack Compose.

Principais telas:

- LocacoesAtivasScreen
- VeiculosScreen
- CadastroVeiculoScreen
- ContatosScreen
- NovaLocacaoScreen

### ViewModel

Responsável pelo gerenciamento dos estados utilizados pelas telas e pela comunicação com os repositórios.

Principais ViewModels:

- VeiculoViewModel
- LocacaoViewModel

O projeto também utiliza `SavedStateHandle` para preservar os dados do contato selecionado durante o fluxo de uma locação.

### Repository

Responsável por intermediar a comunicação entre os ViewModels e as fontes de dados.

Principais repositórios:

- VeiculoRepository
- LocacaoRepository

As fontes de dados utilizadas são:

- Room para persistência local
- Retrofit para comunicação com API REST

## Banco de dados

A persistência local é realizada utilizando Room Database.

O sistema possui três entidades principais:

- Veiculo
- Cliente
- Locacao

DAOs:

- VeiculoDao
- ClienteDao
- LocacaoDao

A entidade `Locacao` possui relacionamentos com veículo e cliente utilizando chaves estrangeiras.

Também é utilizado `@Relation` para recuperar os dados completos de uma locação.

## Dashboard

O aplicativo inicia diretamente no Dashboard de Locações.

O Dashboard apresenta apenas locações com status `ATIVA`.

Cada locação apresenta:

- Marca do veículo
- Modelo do veículo
- Placa
- Nome do cliente
- Telefone
- Data de saída
- Data prevista de entrega
- Quantidade de dias faltantes
- Valor total
- Status

Quando a data prevista de entrega é ultrapassada, a locação é destacada visualmente como atrasada.

O Dashboard também possui uma ação para iniciar uma nova locação e acesso ao gerenciamento de veículos.

## Gestão de veículos

Os veículos podem possuir os seguintes status:

- `DISPONIVEL`
- `ALUGADO`
- `MANUTENCAO`

Ao cadastrar um novo veículo, ele inicia automaticamente com status `DISPONIVEL`.

Um veículo disponível pode ser colocado em manutenção.

Um veículo em manutenção pode retornar para o status disponível.

Quando uma locação é realizada, o veículo passa automaticamente para `ALUGADO`.

Ao finalizar a locação, o veículo retorna para `DISPONIVEL`.

Veículos alugados ou em manutenção não são disponibilizados para uma nova locação.

## Validação de placas

O cadastro de veículo aceita os dois padrões brasileiros definidos no projeto.

Padrão antigo:

```text
AAA-1234
```

Padrão Mercosul:

```text
AAA1A23
```

Também são realizadas validações para:

- campos obrigatórios
- ano numérico
- valor da diária numérico
- valor da diária maior que zero

## Integração com contatos

O aplicativo utiliza `ContentResolver` e `ContactsContract` para consultar os contatos cadastrados no dispositivo Android.

A permissão utilizada é:

```xml
<uses-permission android:name="android.permission.READ_CONTACTS" />
```

A autorização é solicitada em tempo de execução.

A integração permite:

- listar contatos
- pesquisar contatos pelo nome
- selecionar um contato
- recuperar o nome
- recuperar o telefone
- recuperar o ID do contato

O contato selecionado é utilizado como cliente da locação.

## Fluxo de uma locação

1. O aplicativo inicia no Dashboard.
2. O usuário seleciona Nova Locação.
3. Um contato do celular é selecionado como cliente.
4. O sistema apresenta apenas veículos com status `DISPONIVEL`.
5. O usuário seleciona um veículo.
6. A data de saída é selecionada através de um DatePicker.
7. A data prevista de entrega é selecionada através de um DatePicker.
8. O sistema calcula automaticamente a quantidade de diárias.
9. O valor total é calculado automaticamente.

Cálculo utilizado:

```text
Quantidade de dias x Valor da diária
```

10. A locação é salva no Room com status `ATIVA`.
11. O veículo passa automaticamente para o status `ALUGADO`.
12. A navegação retorna ao Dashboard.
13. A nova locação aparece na lista de locações ativas.
14. Ao finalizar a locação, seu status passa para `FINALIZADA`.
15. O veículo retorna automaticamente para `DISPONIVEL`.

## Comunicação com API REST

O projeto utiliza Retrofit 2 para realizar comunicação com uma API REST de teste.

A estrutura de comunicação remota possui:

- ApiService
- RetrofitClient
- SincronizacaoRequest
- SincronizacaoResponse

As chamadas são executadas de forma assíncrona utilizando Kotlin Coroutines.

Também foi implementada a opção de sincronização de uma locação através do Dashboard.

Quando a comunicação é realizada corretamente, o aplicativo informa:

```text
Sincronização realizada com sucesso.
```

## Permissão de Internet

Para realizar chamadas de rede, o aplicativo utiliza:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## Navegação

A aplicação utiliza Navigation Compose seguindo o padrão Single Activity.

A `MainActivity` funciona como contêiner das telas composáveis.

Principais rotas:

- locacoes_ativas
- veiculos
- cadastro_veiculo
- nova_locacao
- selecionar_contato
- contatos

## Gerenciamento de estado

O projeto utiliza:

- ViewModel
- StateFlow
- Flow
- collectAsStateWithLifecycle
- SavedStateHandle

O `SavedStateHandle` é utilizado para preservar os dados do contato selecionado durante o fluxo de criação de uma locação.

## Execução do projeto

1. Clone o repositório.
2. Abra o projeto no Android Studio.
3. Aguarde a sincronização do Gradle.
4. Conecte um dispositivo Android ou utilize um emulador.
5. Execute o aplicativo.
6. Autorize o acesso aos contatos quando solicitado.

Para verificar a compilação:

```powershell
.\gradlew.bat build
```

Para instalar a versão de desenvolvimento em um dispositivo conectado:

```powershell
.\gradlew.bat installDebug
```

Para gerar o APK de release:

```powershell
.\gradlew.bat assembleRelease
```

O APK de release é gerado em:

```text
app/build/outputs/apk/release/
```

## Testes realizados

O aplicativo foi testado em dispositivo Android físico.

Foram validados:

- cadastro de veículo
- validação de placa
- valor positivo da diária
- alteração do veículo para manutenção
- retorno do veículo para disponível
- leitura de contatos
- pesquisa de contatos
- permissão de contatos
- seleção de cliente
- seleção de datas com DatePicker
- criação de locação
- cálculo de diárias
- cálculo do valor total
- alteração automática do veículo para alugado
- Dashboard de locações ativas
- cálculo dos dias faltantes
- finalização da locação
- liberação do veículo
- sincronização utilizando Retrofit

## Capturas de Tela

### Dashboard de Locações

![Dashboard](screenshots/Tela%20Dashboard.png)

### Gestão de Veículos

![Veículos](screenshots/Tela%20Veiculos.jpeg)

### Integração com Contatos

![Contatos](screenshots/Tela%20Contatos.jpeg)

### Nova Locação

![Nova Locação](screenshots/Tela%20Locacao.jpeg)

### Seleção de Data com DatePicker

![DatePicker](screenshots/Tela%20Nova%20Locacao.jpeg)

## APK

O projeto possui APK compilado na configuração Release.

Arquivo gerado:

```text
app-release-unsigned.apk
```

Localização:

```text
app/build/outputs/apk/release/
```

## Autor

Wesley Souza

Análise e Desenvolvimento de Sistemas