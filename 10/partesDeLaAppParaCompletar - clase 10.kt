# PRIMERA PARTE "NAVEGACION"
JetpackRoomTheme {
    // Declaración de la variable que representa el ítem seleccionado en el menú lateral.
    var selectedItem by remember { mutableStateOf("home") }

    // Se crea un controlador de navegación.
    val navController = rememberNavController()

    // Se crea un estado para el cajón de navegación.
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Se crea un ámbito de coroutine para gestionar operaciones asíncronas.
    val scope = rememberCoroutineScope()

    // Se utiliza un ModalNavigationDrawer para el menú lateral.
    ModalNavigationDrawer(
        drawerState = drawerState,

        // Contenido del cajón de navegación.
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                content = {
                    // Elementos del menú lateral.
                    Text("Menu", modifier = Modifier.padding(10.dp))
                    Divider()

                    // Elemento de navegación para la pantalla "Home".
                    NavigationDrawerItem(
                        label = { Text(text = "Home") },
                        icon = { Icon(Icons.Filled.Home, contentDescription = "") },
                        selected = selectedItem == "home",
                        onClick = {
                            navController.navigate("home")
                            selectedItem = "home"
                            // Controla el estado del cajón de navegación (abierto/cerrado).
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        },
                    )

                    // Elemento de navegación para la pantalla "Lista de tareas".
                    NavigationDrawerItem(
                        label = { Text(text = "Lista de tareas") },
                        icon = { Icon(Icons.Filled.List, contentDescription = "") },
                        selected = selectedItem == "taskList",
                        onClick = {
                            navController.navigate("taskList")
                            selectedItem = "taskList"
                            // Controla el estado del cajón de navegación (abierto/cerrado).
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }
                    )

                    // Elemento de navegación para la pantalla "Tiempo en tu ciudad".
                    NavigationDrawerItem(
                        label = { Text(text = "Tiempo en tu ciudad") },
                        icon = { Icon(Icons.Filled.Star, contentDescription = "") },
                        selected = selectedItem == "weather",
                        onClick = {
                            navController.navigate("weather")
                            selectedItem = "weather"
                            // Controla el estado del cajón de navegación (abierto/cerrado).
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }
                    )
                }
            )
        }
    ) {
        // Contenido principal de la aplicación.
        Surface() {
            // Definición de las pantallas y rutas en el NavHost.
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(navController = navController)
                }
                composable("taskList") {
                    MainScreen(todoList = todoList)
                }
                composable("weather") {
                    LocationScreen(this@MainActivity, currentLocation)
                }
            }
        }
    }
}

// Se carga la lista de tareas al iniciar la actividad.
loadToDo()


# SEGUNDA PARTE - PRIMERA VISTA - VISTA PRINCIPAL
// Esta es una función composable que representa la pantalla principal (Home).
@Composable
fun HomeScreen(navController: NavHostController) {

    // Columna que ocupa toda la pantalla, centrando su contenido vertical y horizontalmente.
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Texto de bienvenida.
        Text(
            text = "Deslizar para abrir el menú",
            style = MaterialTheme.typography.titleSmall
        )

        // Espacio vertical entre elementos.
        Spacer(modifier = Modifier.height(16.dp))

        // Imagen de bienvenida centrada.
        Image(
            painter = painterResource(id = R.drawable.ap),
            contentDescription = null,
            modifier = Modifier.size(200.dp) // Tamaño de la imagen
        )

        // Espacio vertical entre elementos.
        Spacer(modifier = Modifier.height(16.dp))

        // Botón para navegar a la lista de tareas.
        Button(
            onClick = {
                navController.navigate("taskList")
            }
        ) {
            Text(text = "Ver Lista de Tareas")
        }
    }
}

#TERCERA PARTE - SEGUNDA VISTA- LISTA DE TAREAS

private fun loadToDo() {
    scope.launch {
        withContext(Dispatchers.Default) {
            dao.getAll().forEach { todo ->
                todoList.add(todo)
            }
        }
    }
}

private fun postTodo(title: String) {
    scope.launch {
        withContext(Dispatchers.Default) {
            dao.post(Todo(title = title))

            todoList.clear()
            loadToDo()
        }
    }
}

private fun updateTodo(todo: Todo) {
    scope.launch {
        withContext(Dispatchers.Default) {
            dao.update(todo)

            runOnUiThread {
                Toast.makeText(this@MainActivity, "Tarea Actualizada", Toast.LENGTH_SHORT).show()
            }

            todoList.clear()
            loadToDo()
        }
    }
}

private fun deleteTodo(todo: Todo) {
    scope.launch {
        withContext(Dispatchers.Default) {
            dao.delete(todo)
            runOnUiThread {
                Toast.makeText(this@MainActivity, "Tarea Eliminada", Toast.LENGTH_SHORT).show()
            }
            todoList.clear()
            loadToDo()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(todoList: SnapshotStateList<Todo>) {
    //    val context = LocalContext.current
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    var text: String by remember {
        mutableStateOf("")
    }
    var isEditDialogVisible by remember { mutableStateOf(false) }
    var editingTodo by remember { mutableStateOf<Todo?>(null) }


    Column(
        modifier = Modifier.clickable {
            keyboardController?.hide()
        }
    ) {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.main_title)) },
            modifier = Modifier.background(Color.Magenta)

        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            items(todoList) { todo ->
                key(todo.id) {
                    TodoItem(
                        todo = todo,
                        onClick = {
                            deleteTodo(todo)
                        },
                        onEditClick = {
                            editingTodo = todo
                            isEditDialogVisible = true

                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .border(
                        BorderStroke(2.dp, Color.Blue)
                    ),
                //                    .background(Color.White),


                label = { Text(text = stringResource(id = R.string.main_new_todo)) }
            )

            Spacer(modifier = Modifier.size(18.dp))

            IconButton(
                onClick = {
                    if (text.isEmpty()) return@IconButton

                    postTodo(text)
                    text = ""
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(56.dp)
                    .background(Color.Magenta)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.main_add_todo),
                    tint = Color.White
                )
            }

        }

        if (isEditDialogVisible) {
            EditTodoDialog(
                todo = editingTodo!!,
                onEditTodo = { editedTodo ->
                    // Lógica para guardar la tarea editada
                    // Puedes actualizar la lista de tareas o realizar otras acciones necesarias
                    // En este ejemplo, simplemente imprimo la tarea editada
                    println("Tarea editada: ${editedTodo.title}")

                    // Actualizar la tarea en la base de datos
                    updateTodo(editedTodo)

                    // Actualizar la lista observable
                    val updatedList = todoList.toMutableList()
                    val index = updatedList.indexOfFirst { it.id == editedTodo.id }
                    if (index != -1) {
                        updatedList[index] = editedTodo
                        todoList.clear()
                        todoList.addAll(updatedList)
                    }


                    // Cerrar el diálogo de edición
                    isEditDialogVisible = false
                    editingTodo = null
                },
                onDismiss = {
                    // Cerrar el diálogo de edición
                    isEditDialogVisible = false
                    editingTodo = null
                }
            )
        }
    }
}


@Composable
fun EditTodoDialog(
    todo: Todo,
    onEditTodo: (Todo) -> Unit,
    onDismiss: () -> Unit
) {
    var editedTitle by remember { mutableStateOf(todo.title) }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = editedTitle,
                    onValueChange = { editedTitle = it },
                    label = { Text("Nuevo título") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = {
                        onEditTodo(todo.copy(title = editedTitle))
                        onDismiss()
                    }) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

#CUARTA PARTE - TERCERA VISTA - INTEGRACION DE CLIMA

// Esta función se encarga de obtener la información meteorológica,
// toma como parámetros un objeto MainViewModel y la ubicación actual (MyLatLng).
private fun fetchWeatherInformation(mainViewModel: MainViewModel, currentLocation: MyLatLng) {
    // Establece el estado del ViewModel como LOADING (cargando).
    mainViewModel.state = STATE.LOADING

    // Obtiene la información del clima mediante la ubicación actual.
    mainViewModel.getWeatherByLocation(currentLocation)

    // Obtiene el pronóstico del clima mediante la ubicación actual.
    mainViewModel.getForecastByLocation(currentLocation)

    // Establece el estado del ViewModel como SUCCESS (éxito) después de obtener la información.
    mainViewModel.state = STATE.SUCCESS
}

// Esta función se encarga de inicializar el ViewModel.
private fun initViewModel() {
    // Crea una instancia de MainViewModel utilizando ViewModelProvider.
    mainViewModel = ViewModelProvider(this@MainActivity)[MainViewModel::class.java]
}

// Esta es una función composable que muestra una sección de carga.
@Composable
fun LoadingSection() {
    // Devuelve una columna que ocupa toda la pantalla, centrando un indicador de carga.
    return Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

// Esta es una función composable que muestra una sección de error con un mensaje proporcionado.
@Composable
fun ErrorSection(errorMessage: String) {
    // Devuelve una columna que ocupa toda la pantalla, centrando un texto con el mensaje de error.
    return Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = errorMessage, color = Color.White)
    }
}

// Esta función se encarga de inicializar el proveedor de ubicación.
private fun initLocationClient() {
    // Obtiene una instancia del proveedor de ubicación fusionada mediante LocationServices.
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
}

// Esta es una función composable que representa la pantalla relacionada con la ubicación.
@Composable
private fun LocationScreen(context: Context, currentLocation: MyLatLng) {

    // Se crea un lanzador para la solicitud de múltiples permisos.
    val lanchuerMultiplePermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        // Verifica si todos los permisos están concedidos.
        val areGranted = permissionsMap.values.reduce { accepted, next ->
            accepted && next
        }
        // Si todos los permisos están concedidos, se actualiza la ubicación y se muestra un mensaje.
        if (areGranted) {
            locationRequired = true
            startLocationUpdate()
            Toast.makeText(context, "Permiso aceptado", Toast.LENGTH_SHORT).show()
        } else {
            // Si algún permiso está denegado, se muestra un mensaje de denegación.
            Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    // Se obtiene el controlador de interfaz de usuario del sistema y se ocultan las barras del sistema.
    val systemUiController = rememberSystemUiController()
    DisposableEffect(key1 = true, effect = {
        systemUiController.isSystemBarsVisible = false
        onDispose {
            systemUiController.isSystemBarsVisible = true
        }
    })

    // Se lanza un efecto cuando cambia la ubicación actual para solicitar permisos y actualizar la ubicación.
    LaunchedEffect(key1 = currentLocation, block = {
        coroutineScope {
            if (permissions.all {
                    ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
                }) {
                startLocationUpdate()
            } else {
                lanchuerMultiplePermission.launch(permissions)
            }
        }
    })

    // Se lanza un efecto cuando se crea la pantalla para obtener información meteorológica.
    LaunchedEffect(key1 = true, block = {
        fetchWeatherInformation(mainViewModel, currentLocation)
    })

    // Se define un degradado para el fondo.
    val gradient = Brush.linearGradient(
        colors = listOf(Color(colorBg1), Color(colorBg2)),
        start = Offset(1000f, -1000f),
        end = Offset(1000f, -1000f)
    )

    // Se utiliza un contenedor Box que ocupa toda la pantalla y tiene un fondo de degradado.
    Box(
        modifier = Modifier.fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Calcula la altura de la pantalla y el margen superior.
        val screenHeigth = LocalConfiguration.current.screenHeightDp.dp
        val marginTop = screenHeigth * 0.1f
        val marginTopPx = with(LocalDensity.current) { marginTop.toPx() }

        // Se crea una columna con scroll vertical y un espacio adicional en la parte superior.
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(
                        placeable.width,
                        placeable.height + marginTopPx.toInt()
                    ) {
                        placeable.placeRelative(0, marginTopPx.toInt())
                    }
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Selecciona la sección a mostrar según el estado del ViewModel.
            when (mainViewModel.state) {
                STATE.LOADING -> {
                    LoadingSection()
                }

                STATE.FAILED -> {
                    ErrorSection(mainViewModel.errorMessage)
                }

                else -> {
                    WeatherSection(mainViewModel.weatherResponse)
                    ForecastSection(mainViewModel.forecastResponse)
                }
            }
        }

        // Se agrega un botón de FloatingActionButton para actualizar la información meteorológica.
        FloatingActionButton(
            onClick = {
                fetchWeatherInformation(mainViewModel, currentLocation)
            },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(
                Icons.Default.Refresh, contentDescription = "Actualizar"
            )
        }
    }
}
