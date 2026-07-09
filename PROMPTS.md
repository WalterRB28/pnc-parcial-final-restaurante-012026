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

