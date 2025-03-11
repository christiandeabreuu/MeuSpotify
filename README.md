# Spotify App 🎵

## Descrição
Este é um aplicativo Android que utiliza a API do Spotify para oferecer funcionalidades personalizadas, como criação de playlists, exibição de artistas favoritos e muito mais. O objetivo principal é entregar uma experiência fluida e eficiente para os amantes de música.

## Funcionalidades Principais
- **Login e autenticação**: Integração com a API do Spotify para autenticar o usuário de forma segura, utilizando o protocolo **OAuth2**.
- **Top Artistas**: Exibição dos artistas mais ouvidos, com suporte à paginação para navegação fluida.
- **Playlists**: Visualização e gerenciamento de playlists do usuário.
- **Criação de Playlists**: Crie playlists diretamente no app com apenas alguns toques.
- **Banco de Dados Local**:
  - **Perfil do Usuário**: Armazenamento de dados localmente para carregamento rápido.
  - **Playlists**: Integração com banco de dados local para otimização de desempenho.
- **Atualização de Token**: Renova automaticamente o token de acesso para manter o usuário conectado.

## Tecnologias Utilizadas
### Linguagem e Frameworks
- **Kotlin**: Linguagem utilizada para o desenvolvimento do app.
- **Android Jetpack**: Incluindo Paging para suporte a grandes listas de dados.
- **LiveData**: Utilizado para observar e reagir a mudanças nos dados de maneira eficiente e sincronizada com o ciclo de vida das atividades e fragments.

### Rede e API
- **Retrofit**: Biblioteca para comunicação com a API do Spotify.
- **OkHttp**: Cliente HTTP para lidar com as requisições de forma eficiente.
- **Coroutines**: Framework para programação assíncrona, garantindo uma interface responsiva.
- **Gson** : Ferramenta de parsing para trabalhar com objetos JSON.

### Armazenamento Local
- **SharedPreferences**: Utilizado para armazenar tokens de autenticação (AccessToken e RefreshToken).
- **Room Database**: Solução de banco de dados local para o perfil do usuário e playlists.

### Testes
- **MockK**: Framework utilizado para simular comportamentos e dependências nas funções e classes.

### Arquitetura e Design
- **Clean Architecture**: Separação de responsabilidades em camadas bem definidas, garantindo maior manutenibilidade e organização.
- **Model-View-ViewModel (MVVM)**: Para organizar o código de forma eficiente e reativa.
- **Material Design**: Interface seguindo as diretrizes modernas do Material Design.

### Integrações
- **Spotify API**: Fonte principal de dados do app, como artistas, playlists e informações do usuário.

## Funcionalidades Avançadas
- **Testes Unitários**: Cobertura utilizando o MockK para simular dependências e validar a lógica das funcionalidades.
- **Paginação**: Implementada na tela de *Top Artistas* para melhorar a experiência do usuário.
- **Banco de Dados Local**:
  - **Tela de Perfil do Usuário**: Dados do perfil armazenados localmente para acesso rápido.
  - **Tela de Playlists**: Banco de dados local para gerenciar as playlists do usuário.

## Como Executar
1. Clone este repositório em sua máquina local.
2. Crie uma conta no [Spotify Developer Console](https://developer.spotify.com/dashboard/) para obter suas credenciais (Client ID e Client Secret).
3. Configure as credenciais no app (por exemplo, na lógica de autenticação ou de refresh de tokens).
4. Compile o projeto e execute no emulador ou em um dispositivo físico Android.

## Escolhas Tecnológicas e Padrões Arquiteturais
### Linguagem de Programação
- **Kotlin**: Escolhida por sua concisão, segurança (null safety) e suporte a corrotinas para operações assíncronas.

### Frameworks e Bibliotecas
- **Retrofit**: Para consumo eficiente da API do Spotify.
- **OkHttp**: Cliente HTTP para gerenciar requisições e respostas.
- **Room Database**: Para gerenciamento de banco de dados local.
- **LiveData**: Para reatividade e sincronização com o ciclo de vida das atividades e fragments.
- **Paging Library**: Para paginação eficiente em grandes conjuntos de dados.
- **Coroutines**: Para simplificar e otimizar operações assíncronas.

### Arquitetura
- **Clean Architecture**: Modularidade e separação de responsabilidades em camadas:
  - **Domínio**: Lógica de negócios pura, sem dependências externas.
  - **Dados**: Integração com API e banco de dados local.
  - **UI**: Interface do usuário e ViewModels reativos.
- **MVVM (Model-View-ViewModel)**: Separação clara entre lógica de negócios e interface do usuário.

### Racionalidade das Escolhas
- **Manutenibilidade**: Tecnologias e padrões arquiteturais escolhidos para facilitar a manutenção e escalabilidade.
- **Segurança**: Uso do OAuth2 para garantir autenticação segura.
- **Performance**: Uso de Paging e Coroutines para garantir uma experiência fluida em grandes conjuntos de dados.

