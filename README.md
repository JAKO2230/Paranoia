# Cofres y Sombras

> 8 cofres. 1 salida. Algo te persigue.

Juego 2D de vista superior para **2 jugadores** en el mismo teclado, desarrollado en **Java (Swing)** con el patrón **MVC**. Un jugador es el **Humano**, que debe abrir cofres resolviendo puzzles dentro de un laberinto. El otro es el **Monstruo**, que lo persigue para atraparlo.

Este proyecto es una recreación en Java del juego web [Cofres y Sombras](https://jueguito-pixel.lovable.app/).

## Cómo se juega

| Jugador | Rol | Objetivo |
|---------|-----|----------|
| Jugador 1 | Humano | Abrir los 8 cofres y llegar a la salida |
| Jugador 2 | Monstruo | Atrapar al humano antes de que escape |

- El **Humano** abre un cofre al caminar sobre él. Se muestra un puzzle y, si lo resuelve, el cofre queda abierto.
- Cuando los **8 cofres** están abiertos, el humano puede escapar por la salida.
- El **Monstruo** gana si toca al humano en cualquier momento.

### Condiciones de fin de partida

- **Victoria (Humano):** llegar a la salida con los 8 cofres abiertos.
- **Derrota (Humano):** ser alcanzado por el monstruo.

## Controles

| Acción | Jugador 1 (Humano) | Jugador 2 (Monstruo) |
|--------|--------------------|----------------------|
| Arriba | `W` | `↑` |
| Abajo | `S` | `↓` |
| Izquierda | `A` | `←` |
| Derecha | `D` | `→` |

Los puzzles se responden escribiendo en el cuadro de diálogo que aparece al abrir un cofre.

## Requisitos

- Java **17** o superior (se usan `record` y `switch` con flechas)
- Maven o Gradle (opcional; también se puede compilar con `javac`)

## Compilar y ejecutar

Con `javac`, desde la raíz del proyecto:

```bash
javac -d out $(find src -name "*.java")
java -cp out cofresysombras.controller.Main
```

## Arquitectura (MVC)

```
src/cofresysombras/
├── model/        Reglas y estado del juego (sin dependencias de Swing)
├── view/         Ventanas y dibujo del laberinto
└── controller/   Entrada de teclado y coordinación modelo-vista
```

### Modelo (`model`)

| Clase | Responsabilidad |
|-------|-----------------|
| `Juego` | Fachada del modelo: aplica las reglas, controla el estado y notifica cambios |
| `Laberinto` | Matriz de celdas (`PARED`, `PISO`, `SALIDA`) y validación de movimiento |
| `Entidad` | Clase base con posición |
| `Humano` | Jugador 1; lleva la cuenta de cofres abiertos |
| `Monstruo` | Jugador 2 |
| `Cofre` | Posición, estado (abierto o cerrado) y puzzle asociado |
| `Puzzle` | Interfaz con enunciado y verificación de respuesta |
| `EstadoJuego` | `MENU`, `JUGANDO`, `RESOLVIENDO_PUZZLE`, `VICTORIA`, `DERROTA` |
| `Posicion`, `Direccion` | Tipos auxiliares de coordenadas y movimiento |

### Vista (`view`)

| Clase | Responsabilidad |
|-------|-----------------|
| `VentanaPrincipal` | Contenedor de los paneles |
| `PanelMenu` | Botones JUGAR, INSTRUCCIONES y SALIR |
| `PanelJuego` | Dibuja laberinto, cofres, humano y monstruo |
| `DialogoPuzzle` | Muestra el enunciado y recoge la respuesta |

La vista solo lee el modelo para dibujar; no contiene reglas del juego.

### Controlador (`controller`)

| Clase | Responsabilidad |
|-------|-----------------|
| `JuegoController` | Traduce teclas y clics en llamadas al modelo y reacciona a los cambios de estado |
| `TecladoListener` | Captura las teclas y las envía al controlador |
| `Main` | Punto de entrada: crea el modelo, la vista y el controlador |

El modelo notifica los cambios mediante observadores, y el controlador actualiza la vista.

## Añadir nuevos puzzles

Cada puzzle es una clase que implementa `Puzzle`:

```java
public class PuzzleSuma implements Puzzle {
    public String getEnunciado() { return "¿Cuánto es 7 + 5?"; }
    public boolean verificar(String respuesta) { return respuesta.trim().equals("12"); }
}
```

Luego se asigna a un `Cofre` al construir el nivel.

## Hoja de ruta

- [ ] Movimiento por casillas y colisiones
- [ ] 8 cofres con puzzles distintos
- [ ] Pantalla de instrucciones
- [ ] Sprites y sonido
- [ ] Varios laberintos o niveles
- [ ] Modo de un jugador con monstruo controlado por IA

## Créditos

Juego original: *Cofres y Sombras*, creado con [Lovable](https://lovable.dev). Esta versión en Java es una recreación con fines educativos.
