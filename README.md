# JustChat-kotlin #

Esta es una librería la cual tiene la finalidad de abstraer la parte visual de un chat en una aplicación.<br>
Contiene tanto las funcionalidades para abrir un chat como una lista de chats.

### Información básica ###

* Esta es una librería la cual tiene la finalidad de abstraer la parte visual de un chat en una aplicación.<br>
  En esta guía se explicará la configuración básica para implementar e inicializar la librería.
* Versión: 1.0
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### Requisitos ###
* Android Studio 4.0 o superior.
* Android 6.0 o superior.

### Implementación ###

* Descargar la librería, y desde Android Studio pulsar en <b>File > Project Structure > Dependencies > All Dependencies > + > Add Jar/Aar Dependency > 'Seleccionar la librería a incluir'</b>
* Implementarla en el <b>gradle.app</b>:
<pre><code>implementation project(path: ':just-chat')</code></pre>

### Inicialización ###

Para inicializar la librería JustChat, utilizaremos la clase <b>JustChat</b> de la librería de la siguiente forma:
<pre><code>JustChat.Builder()
    .provideContext(this)
    .setUserId(firebaseAuth.currentUser?.uid)
    .setEventsImplementation(events)
    .build()</code></pre>

Donde <code>setUserId()</code> le pasaremos el id del usuario actual.

En <code>setEventsImplementation()</code> le pasaremos la implementación con las llamadas que pide la librería y que son necesarios para funcionar. IMPORTANTE: Esta implementación tiene que extender de la interfaz <code>Events</code> de la librería:
<pre><code>class EventsImpl @Inject constructor(
    private val context: Context,
    private val eventsUseCase: EventsUseCase,
    private val notificationsUseCase: NotificationsUseCase
) : Events {

    override fun initFlowGetChats(userId: String): Flow<MutableList<Chat>> {
        return eventsUseCase.getChats(context.isNetworkAvailable, userId)
    }

    override fun getChat(userId: String, chatId: String): Flow<Chat> {
        return eventsUseCase.getChat(context.isNetworkAvailable, userId, chatId)
    }

    override fun getGroups(userId: String): Flow<MutableList<Group>> {
        return eventsUseCase.getGroups(context.isNetworkAvailable, userId)
    }

    override fun getChatMessages(
        userId: String,
        chatId: String,
        page: Int
    ): Flow<MutableList<ChatMessageItem>> {
        return eventsUseCase.getChatMessages(context.isNetworkAvailable, userId, chatId, page)
    }

    override fun getCurrentUser(userId: String): Flow<UserData> {
        return eventsUseCase.getCurrentUser(context.isNetworkAvailable, userId)
    }

    override fun sendMessage(
        chatInfo: ChatInfo,
        message: ChatMessageItem
    ): Flow<ResultInfo> {
        return eventsUseCase.sendMessage(context.isNetworkAvailable, chatInfo, message)
    }

    override fun initFlowReceiveMessage(
        userId: String,
        chatId: String
    ): Flow<ChatMessageItem> {
        return eventsUseCase.initFlowReceiveMessage(context.isNetworkAvailable, userId, chatId)
    }

    override suspend fun sendNotification(
        userId: String,
        chat: Chat?,
        message: String?
    ) {
        
      notificationsUseCase.sendNotification(notification)
    }
}</pre></code>

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact