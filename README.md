# Aluguel de Carros - Projeto N1

Projeto desenvolvido para a disciplina de Desenvolvimento de Sistemas para Dispositivos Móveis do curso de Análise e Desenvolvimento de Sistemas.

O aplicativo foi desenvolvido para auxiliar pequenos locadores de veículos no gerenciamento da frota, clientes e locações, utilizando recursos nativos do Android, persistência local, integração com contatos do dispositivo e consumo de API REST.

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
- Validação de valor da diária
- Acesso aos contatos do celular
- Pesquisa de contatos por nome
- Seleção de cliente através dos contatos do dispositivo
- Solicitação da permissão READ_CONTACTS em tempo de execução
- Tratamento de permissão de contatos
- Cadastro de nova locação
- Seleção apenas de veículos disponíveis
- Seleção visual da data de saída utilizando DatePicker
- Seleção visual da data prevista de entrega utilizando DatePicker
- Cálculo automático da quantidade de diárias
- Cálculo automático do valor total da locação
- Listagem das locações ativas
- Visualização dos dados do cliente e do veículo
- Cálculo dinâmico dos dias faltantes para devolução
- Destaque de locações em atraso
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

O projeto utiliza o padrão MVVM com separação de responsabilidades entre as camadas da aplicação.

Fluxo simplificado:

```text
Jetpack Compose
      ↓
ViewModel
      ↓
Repository
   ↙      ↘
Room    Retrofit