# CuentaFlow

CuentaFlow es una API REST reactiva para consultar y registrar cuentas bancarias. El proyecto integra Spring Boot 4, Spring WebFlux, RxJava 3 y Spring Data MongoDB Reactive. Admite cuentas de ahorro y corrientes en PEN o USD, valida las solicitudes y aplica reglas de negocio para generar números de cuenta, evitar duplicados y exigir el saldo mínimo de apertura.

Al arrancar, la aplicación carga tres cuentas de ejemplo si la colección `cuentas` está vacía.

## Requisitos

- Java 17
- Apache Maven instalado y disponible como `mvn`
- Docker con Docker Compose, o una instancia local de MongoDB

## Iniciar MongoDB

La configuración por defecto se encuentra en `src/main/resources/application.yml`:

```yaml
spring:
  mongodb:
    uri: mongodb://localhost:27017/cuentaflowdb
```

La aplicación necesita MongoDB accesible en `localhost:27017`. Puedes iniciarlo con Docker Compose desde la raíz del proyecto:

```powershell
docker compose up -d
docker compose ps
```

Compose crea el contenedor `cuentaflow-mongo` y conserva los datos en el volumen `cuentaflow-data`. Para detener MongoDB sin borrar los datos:

```powershell
docker compose down
```

Como alternativa, instala e inicia MongoDB Community Server localmente y asegúrate de que escuche en el puerto `27017`. La base de datos `cuentaflowdb` se crea cuando la aplicación persiste sus primeros documentos.

## Compilar, probar y ejecutar

Desde la raíz del proyecto:

```powershell
mvn clean test
```

Las pruebas del taller usan un repositorio simulado y no necesitan una instancia de MongoDB. Para iniciar la API:

```powershell
mvn spring-boot:run
```

También puedes abrir la carpeta en IntelliJ IDEA, configurar un JDK 17 y ejecutar `com.nttdata.cuentaflow.CuentaflowApplication`.

La API queda disponible en `http://localhost:8085`. Para detenerla, usa `Ctrl+C` en la terminal donde se está ejecutando.

## Endpoints

| Método | Ruta | Comportamiento |
|---|---|---|
| `GET` | `/api/v1/cuentas` | Lista todas las cuentas. |
| `GET` | `/api/v1/clientes/{dni}/cuentas` | Lista las cuentas del DNI, ordenadas por número. |
| `GET` | `/api/v1/cuentas/{numero}` | Consulta una cuenta por su número. |
| `POST` | `/api/v1/cuentas` | Registra una cuenta nueva; responde `201 Created`. |

Ejemplo de solicitud para registrar una cuenta:

```json
{
  "dni": "11111111",
  "titular": "Ana Torres",
  "tipo": "AHORRO",
  "moneda": "PEN",
  "saldoInicial": 100.00
}
```

El DNI debe tener exactamente ocho dígitos y todos los campos son obligatorios. El saldo inicial no puede ser negativo; las cuentas `CORRIENTE` requieren un mínimo de `500.00`. El número se genera con el prefijo del tipo, el DNI y el código de moneda: por ejemplo, `191-11111111-01` para una cuenta `AHORRO` en PEN.

Los errores de validación y de negocio se devuelven en el formato `ErrorRespuesta`, con un código y un mensaje. Entre las respuestas previstas están `400 Bad Request` (`VALIDACION` o `SALDO_MINIMO`), `404 Not Found` (`CUENTA_NO_ENCONTRADA`) y `409 Conflict` (`CUENTA_DUPLICADA`).

## Postman

Importa `CuentaFlow.postman_collection.json` en Postman. La colección usa `http://localhost:8085` como URL base e incluye ejemplos de consulta, registro y respuestas de error.
