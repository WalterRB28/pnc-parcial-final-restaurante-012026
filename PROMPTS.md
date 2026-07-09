Uso de Github Copilot

Prompt:
1. Sistema de Pedidos de Restaurante  
Desarrollar un proyecto backend de un sistema donde distintos usuarios interactúan con las mesas y los pedidos de una cadena de restaurantes con varias sucursales.  

Entidades mínimas:  

Restaurante/Sucursal (el sistema maneja más de una sucursal)  
Mesa (pertenece a una sucursal, tiene capacidad y estado)  
Pedido/Orden (asociado a un cliente, una mesa y una lista de productos)  
Usuario (con rol asignado)  
Quiero que la arquitectura respete el enfoque N-Capas que hemos visto en clase (Presentación / Lógica de Negocio / Acceso a Datos, como mínimo).

La IA genero el proyecto base para la implementacion de el sistema del restaurante, no se descarto nada

Prompt:
Description:  

Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.  

Reason: Failed to determine a suitable driver class  


Action:  

Consider the following:  
	If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.  
	If you have database settings to be loaded from a particular profile you may need to activate it (no profiles are currently active).  


Process finished with exit code 1

La IA me ayudo a debuggear el error, fue porque no tenia activo pgAdmin

Prompt:
2.1 Autenticación  
Login con usuario y contraseña, que devuelva un Access Token (JWT) y un Refresh Token.  
El Access Token debe expirar en un tiempo corto (por ejemplo, 15 minutos) y el Refresh Token en un tiempo mayor (por ejemplo, 7 días).  
Endpoint para renovar el Access Token usando el Refresh Token.  

ahora intentemos hacer esto, ya no tengo errores a la jora de levantar el proyecto

La IA genero toda la base para la autenticacion para entrar al servidor, lo unico que cambie fue el uso de settere y getters pues no ocupo la tool de Lombok

Prompt:
Como hago para realizar las siguientes pruebas?

La IA me ayudo a probar con los endpoints y tests de crar cosas por medio de Bruno, todo funciona correcto

Prompt:
2.2 Roles y Autorización  
Mínimo estos tres roles, con permisos claramente diferenciados:  

Rol	Permisos  
Administrador	Acceso total: gestiona restaurantes, mesas, usuarios y pedidos de todas las sucursales  
Encargado de turno	Gestiona pedidos y mesas, pero únicamente de la sucursal a la que pertenece  
Cliente	Solo puede crear, ver y cancelar sus propios pedidos  
2.3 Regla de negocio no trivial (obligatoria)  
Además de la autorización básica por rol, deberan implementar una de estas reglas (o me propongan una equivalente):  

Opción A — Invalidación de tokens por cambio de contraseña: si un usuario cambia su contraseña, todos los tokens emitidos previamente deben quedar inválidos de inmediato, aunque no hayan expirado. Quiero que me expliquen y justifiquen el mecanismo elegido (versión de token, blacklist, etc.).  

hagamos esta parte ahora

El resultado de la IA fue implementar correctamente todos los datos para hacer la autorizacioon y permisos correctos, haciendo priuebas en vbruno con la misma IA me ayudo bastante a comprender el proceso
