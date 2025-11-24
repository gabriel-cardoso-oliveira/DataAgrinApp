# DataAgrinApp

Este é um projeto de exemplo que demonstra um aplicativo de gerenciamento agrícola construído com Kotlin Multiplatform. Ele inclui funcionalidades como visualização do clima, gerenciamento de tarefas.

### Recursos:
*   **Clima em Tempo Real:** Obtém a previsão do tempo com base na localização do dispositivo.
*   **Gerenciamento de Tarefas:** Crie e acompanhe tarefas agrícolas.
*   **Sincronização Offline:** Os dados são salvos localmente, permitindo que o app funcione mesmo sem internet.
*   **Multiplataforma:** Código compartilhado entre Android e iOS.

---

## Começando

### 1. Como Executar o Aplicativo

#### Android

Para compilar e executar a versão de desenvolvimento do aplicativo Android, use a configuração de execução na barra de ferramentas do seu Android Studio ou execute o comando diretamente no terminal:

*   No macOS/Linux:
    ```shell
    ./gradlew :composeApp:assembleDebug
    ```
*   No Windows:
    ```shell
    .\gradlew.bat :composeApp:assembleDebug
    ```

#### iOS

Para compilar e executar a versão de desenvolvimento do aplicativo iOS, use a configuração de execução na barra de ferramentas do seu IDE ou abra o diretório `/iosApp` no Xcode e execute por lá.

---

### Arquitetura e Estrutura do Projeto

Este projeto segue a arquitetura **MVVM (Model-View-ViewModel)**, que promove uma clara separação de responsabilidades, facilitando a manutenção e a testabilidade do código.

*   `./composeApp/src`: Contém o código Kotlin compartilhado entre Android e iOS.
    *   `commonMain`: Código comum para todos os alvos.
        *   `model`: **(Model)** Contém as classes de dados (`Task`, `WeatherEntity`, etc.) que representam a informação do aplicativo.
        *   `ui`: **(View)** Contém os Composables do Jetpack Compose que formam a interface do usuário (`DashboardScreen`, `WeatherScreen`, etc.). As telas são reativas e observam as mudanças nos ViewModels.
        *   `viewmodel`: **(ViewModel)** Contém as classes (`TaskViewModel`, `WeatherViewModel`, etc.) que preparam e gerenciam os dados para a UI. Eles sobrevivem a mudanças de configuração e expõem os dados através de `StateFlow`.
        *   `repository`: Contém as classes responsáveis por buscar e gerenciar os dados, decidindo se eles devem vir do banco de dados local (Room) ou da API remota (Supabase/Weather API).
        *   `di`: Contém a configuração de injeção de dependência (Koin), que conecta todas as camadas.
        *   `data`: Contém a configuração do banco de dados local (`AppDatabase`) e dos clientes de rede (`WeatherApi`).
    *   `androidMain` e `iosMain`: Contém código específico da plataforma, como a inicialização do banco de dados e a implementação do `LocationTracker`.

*   `./iosApp`: Contém o projeto Xcode para o aplicativo iOS.

Saiba mais sobre [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html).


## Próximos passos:
*   Sincronizar os dados utilizando BaaS
*   Testes unitários e de integração
