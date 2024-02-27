
# Teste Declink 

Repositório utilizado para o desenvolvimento do teste para a empresa Declink. 
O aplicativo foi feito em Kotlin e XML, utilizando de conceitos básicos como Activities, View, Lifecycle, Services, etc. 
O objetivo da aplicação foi exemplificar meu conhecimento em determinadas áreas do desenvolvimento Android.
O Simulador usado foi Pixel 3a API 28.


## Tecnologias Utilizadas
- Minimo SDK Level 25
- <a href="https://kotlinlang.org/">Kotlin</a>
- Activities: Representam visualmente e logicamente a interface das telas que interagem com o usuário.
- Lifecycle: Observa o ciclo de vida do Android e manipula estados da UI após cada mudança no ciclo de vida.
- ViewModel: Usado como uma camada que recupera dados da camada do Model e aplica na View.
- ViewBinding: Gerencia Views de layouts XML em Kotlin. 



- [Jetpack](https://developer.android.com/jetpack?hl=pt-br)
  - Live Data: Usado para observar dados de maneira reativa, permitindo que os dados utilizados da camada de view sejam atualizados de maneira automática.
  - Corrotinas: Padrão de projeto utilizado para executar tarefas assincronas simultaneamente. 
  - Room: Biblioteca de persistencia que oferece uma camada de abstração do SQLite do sistema, permitindo um acesso mais robusto ao banco de dados.
  - CameraX: Ferramenta da Google para uma melhor utilização da Camera de dispositivos Android

    
- Architecture
  - MVVM (Model-View-ViewModel) + Clean Architecture: O primeiro item é utilizado para separar a logica da UI do aplicativo, ajutando com testagem e organização de codigo, Somado a Clean Architecture que ajuda a separar o sistema em camadas bem definidas, que funcionam com independencia, facilitando a escalabilidades e manutenção.
  - Repository pattern: Esse padrão de projeto ajuda com a abstração da camada de Data.

- Libraries
  - [Retrofit & OkHttp](https://square.github.io/retrofit/): Utilizado para se comunicar com uma API RESTful e desserializar os dados.
  - [Koin](https://insert-koin.io/): Utilizado para Injeção de Dependência
  - [SDP](https://github.com/intuit/sdp): Uma biblioteca Android que providencia um novo tipo de unidade de tamanho, que é escalavel com o tamanho das telas, abrangendo diversos tipos de dispositivos.
  - [Mockk](https://mockk.io/): Biblioteca de Mock para Kotlin, utilizado em Testes Unitários
  - [JUnit](https://junit.org/junit5/): Framework de testes em Java e JVM

## Arquitetura: 

A aplicação foi desenvolvida utilizando MVVM, Clean Architecture e o Repository pattern. Seguindo a [Recomendação oficial da Google](https://developer.android.com/topic/architecture).
Nesse caso, atuei com quatro camadas, sendo elas modulos independentes entre si, com seus proprios Android Manifests, resources, testes, etc.
As camadas são:
- App: Onde se encontram os dados da View e ViewModel e implementação da DI 
- Domain: Camada intermediaria que fornece abstrações e UseCases para as comunicações de dados entre a Data e Database com o App
- Database: Camada onde ficam todos os dados e implementações da Database, nesse local é implementado o Local Repository, Local DataSource e o DAO (junto da Database)   
- Data: Cama onde ficam todos os dados e implementações relativos à comunicação com APIs, nesse local é implementado o Remote Repository, Remote Data Source e a API (que ainda não está em funcionamento)

## Funcionalidades

### Tirar Fotos

Com a ajuda com CameraX, a aplicação consegue tirar fotos da maneira que o usuário desejar. A primeira activity a ser aberta é a da camera, onde é solicitada permissões e, com o aval do usuário, a aplicação já registra as fotos. 
Após tirar as fotos, o usuário é levado para uma nova atividade, onde tem a escolha de tirar uma nova foto, case deseje. 

<b>Importante ressaltar que todas as fotos, sejam pela camera frontal ou traseira, já contam com a marca d'agua especificada na descrição do desafio tecnico</b>. 

Observação: Em poucos dispositivos, a foto aparece rotacionada. Com versões mais recentes do android, as fotos tem aparecido da maneira ideal com a marca dagua.
### Recuperação de dados do aparelho

Após as fotos serem batidas e o usuário ser levado à uma nova activity. A aplicação irá guardar todos os 14 dados, em sua maioria oriundos da Android SDK, no banco de dados local.
O envio é automático e logo após o usuário sair da activity da camera para a activity que mostra a foto batida. 


### Envio de dados para Servidor

Os dados ficam guardados no banco de dados local até que o usuário escolha enviá-los para um servidor de aplicação. 
Como essa API ainda não foi implementada, o aplicativo está com a funcionalidade engatilhada, ou seja, toda a lógica de criação da API, uso do Retrofit, preparo dos dados é feito, porém, ainda não é funcional.
Sobre a ordem de funcionamento, os passos são os seguintes: 
  - Recupera o item no banco de dados com seu ID unico (PK)
  - Envia através de UseCase, Repository e RemoteDataSource
  - No RemoteDataSource o dado é convertido para JSON
  - Após a conversão o dado é enviado, retornando uma resposta
  - Resposta exibida através de um Toast na Activity. 

Importante ressaltar que foram feitos [Testes unitários](https://github.com/N0stalgiaUltra/TesteDeclink/blob/833da476620927477c8488167c8d0c9483fe1dac/androidDeclinkTest/data/src/test/java/com/n0stalgiaultra/data/PhotoApiUnitTest.kt) para validar o funcionamento dessa feature. 
Primeiro, na camada de Data, foi mockado um servidor para responder as chamadas da API. 
Após o retorno, existem dois testes para validarem quando a resposta for bem ou mal sucedida. 

Ainda sobre os testes, por se tratar de uma feature que atua em mais de um módulo, foram realizados [Testes Instrumentados](https://github.com/N0stalgiaUltra/TesteDeclink/blob/833da476620927477c8488167c8d0c9483fe1dac/androidDeclinkTest/database/src/androidTest/java/com/n0stalgiaultra/database/DatabaseTest.kt)  para validar quando alterar o campo 'TRANSMITIDO' nos dados locais. 

### Exportação do banco de dados 

Por fim, a aplicação também exporta seu banco de dados em um arquivo JSON. Ao clicar no botão "Exportar dados do BD", O banco de dados é convertido para JSON e exportado para a aplicação nativa/padrão do celular, chamada "Files".
Ao entrar no Files, é necessário o uso de um SD, busque por 'backup-declink' e o arquivo JSON deve aparecer lá. 

Observações: 
1- Caso seu celular não tenha um SD instalado, essa exportação dará erro. 
2- Existem casos onde a Files não exibe os arquivos diretamente no celular, sendo necessário o uso do computador para realizar essas tarefas
3- Minha sugestão é testar essa funcionalidade no computador, onde é possivel baixar o json via Android Studio, através da aba Device Explorer, com o seguinte caminho: (/sdcard/Android/data/com.n0stalgiaultra.androidtest/files/Documents/backup-declink/bd-declink.json). 


 


