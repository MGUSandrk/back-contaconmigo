# Reglas base para agentes

Este proyecto es un backend Spring Boot organizado por capas. Al agregar o modificar modulos, mantener el estilo actual del codigo y el flujo de datos existente. No tomar la carpeta `model` como referencia principal para estas reglas: los modelos son clases Java basicas con comportamiento propio de dominio.

## Flujo de datos

El flujo normal es:

1. `resources`: recibe HTTP, lee headers/path/query/body, autoriza, mapea DTOs a modelos, delega al servicio y devuelve `ResponseEntity`.
2. `services`: contiene reglas de negocio, validaciones, busquedas, calculos, configuracion de relaciones entre modelos y llamadas a repositorios.
3. `repository`: concentra acceso a base de datos con `JpaRepository`, query methods, JPQL y SQL nativo cuando hace falta.
4. `dto`: define objetos de entrada/salida sin logica de negocio. El mapeo simple usa `ModelMapper`; el mapeo complejo se centraliza en `dto.Mapper`.
5. `exceptions`: define errores de dominio que extienden `ModelExceptions` y exponen el `HttpStatus`.
6. `util`: contiene utilidades transversales como JWT, password, fechas y carga inicial.

Los `resources` no deben acceder a repositorios. Los repositorios no deben contener reglas de negocio. Las validaciones de dominio pertenecen al servicio.

## Resources

Ubicacion: `src/main/java/com/sistema_contable/sistema/contable/resources`.

Convenciones actuales:

- Cada recurso usa `@RestController`, `@RequestMapping` y `@CrossOrigin`.
- El origen CORS normal es `@CrossOrigin(origins = "${FRONT_URL}")`.
- Las dependencias se inyectan con campos `@Autowired`.
- Los endpoints devuelven `ResponseEntity<?>`.
- El token se recibe con `@RequestHeader("Authorization") String token`.
- Para endpoints protegidos usar `authService.authorize(token)`.
- Para endpoints admin usar `authService.adminAuthorize(token)`.
- El controller debe hacer el minimo trabajo necesario: autorizar, convertir datos de entrada, llamar al servicio, mapear respuesta.
- Las respuestas exitosas usan `new ResponseEntity<>(body, HttpStatus.X)` o `new ResponseEntity<>(null, HttpStatus.X)`.
- Para errores de dominio capturar `ModelExceptions`, imprimir `modelError.getMessage()` y responder `modelError.getHttpStatus()`.
- Para errores inesperados capturar `Exception` y responder `HttpStatus.INTERNAL_SERVER_ERROR`.
- Cuando se arma una respuesta especifica, usar metodos privados al final del resource, por ejemplo `accountResponse`, `entryResponse` o `responseDTOS`.

Estructura esperada:

```java
@RestController
@RequestMapping("/module")
@CrossOrigin(origins = "${FRONT_URL}")
public class ModuleResource {

    //dependencies
    @Autowired
    private ModuleService service;
    @Autowired
    private AuthorizationService authService;
    @Autowired
    private ModelMapper mapper;

    //endpoints
    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token) {
        try {
            authService.authorize(token);
            return new ResponseEntity<>(response(service.getAll()), HttpStatus.OK);
        } catch (ModelExceptions modelError) {
            System.out.println(modelError.getMessage());
            return new ResponseEntity<>(null, modelError.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //secondary methods
}
```

## Services

Ubicacion:

- Interfaces generales: `src/main/java/com/sistema_contable/sistema/contable/services`.
- Servicios contables: `src/main/java/com/sistema_contable/sistema/contable/services/accounting`.
- Interfaces contables: `src/main/java/com/sistema_contable/sistema/contable/services/accounting/interfaces`.
- Seguridad: `src/main/java/com/sistema_contable/sistema/contable/services/security`.
- Interfaces de seguridad: `src/main/java/com/sistema_contable/sistema/contable/services/security/interfaces`.

Convenciones actuales:

- Crear una interfaz `XService` y una implementacion `XServiceImp`.
- La implementacion lleva `@Service`.
- La implementacion usa `implements XService`.
- Las dependencias se inyectan con `@Autowired`.
- Los metodos publicos del service suelen declarar `throws Exception`.
- Usar comentarios de seccion como `//dependencies`, `//CRUD`, `//GETTERS`, `//SEARCHES`, `//SECONDARY METHODS` cuando ayuden a mantener el formato.
- Las reglas de negocio y validaciones se hacen en el service.
- Cuando no existe un recurso esperado, lanzar una excepcion especifica que extienda `ModelExceptions`.
- Usar metodos privados para pasos auxiliares, calculos internos y configuracion de relaciones.
- Usar `@Transactional` en operaciones que modifican datos de forma especial, por ejemplo deletes con query manual.

Patron esperado:

```java
public interface ModuleService {
    void create(Module module) throws Exception;
    List<Module> getAll() throws Exception;
    Module searchById(Long id) throws Exception;
}
```

```java
@Service
public class ModuleServiceImp implements ModuleService {

    //dependencies
    @Autowired
    private ModuleRepository repository;

    //CRUD
    @Override
    public void create(Module module) throws Exception {
        repository.save(module);
    }

    //GETTERS
    @Override
    public List<Module> getAll() throws Exception {
        return repository.findAll();
    }

    //SEARCHES
    @Override
    public Module searchById(Long id) throws Exception {
        Module module = repository.searchById(id);
        if (module == null) {
            throw new ResourceNotFindException("ERROR : Module not found by id");
        }
        return module;
    }
}
```

## Repository

Ubicacion: `src/main/java/com/sistema_contable/sistema/contable/repository`.

Convenciones actuales:

- Cada repositorio es una interfaz que extiende `JpaRepository<Entity, Long>`.
- Usar `@Repository` cuando el archivo existente equivalente lo usa.
- Para busquedas explicitas usar `@Query`.
- Para parametros usar `@Param("name")`.
- Usar JPQL cuando se trabaja sobre entidades y propiedades Java.
- Usar `nativeQuery = true` cuando la consulta depende de tablas/columnas concretas o `LIMIT`.
- Para deletes manuales usar `@Modifying(clearAutomatically = true, flushAutomatically = true)`.
- Los repositorios devuelven entidades, listas, escalares o booleanos; no DTOs.

Ejemplo:

```java
@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    @Query("SELECT m FROM Module m WHERE m.id = :id")
    Module searchById(@Param("id") Long id);

    @Query("SELECT m FROM Module m WHERE m.name = :name")
    Module searchByName(@Param("name") String name);
}
```

## DTOs y mapeo

Ubicacion: `src/main/java/com/sistema_contable/sistema/contable/dto`.

Convenciones actuales:

- Los DTOs son clases Java simples.
- Usar nombres `XRequestDTO` para entrada y `XResponseDTO` para salida.
- Los campos son privados.
- Exponer getters y setters.
- No poner reglas de negocio en DTOs.
- No acceder a repositorios o servicios desde DTOs.
- Para mapeo simple, inyectar `ModelMapper`.
- Para mapeo con relaciones o campos derivados, agregar configuracion en `dto.Mapper`, que extiende `ModelMapper` y esta anotado con `@Component`.
- Si un request necesita referenciar otra entidad, el DTO puede llevar el `Long id` de esa entidad y el mapper puede cargar ese id en una instancia parcial del modelo.

Ejemplo de DTO:

```java
public class ModuleRequestDTO {

    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
```

Ejemplo de mapeo complejo:

```java
private void configModule(){
    this.createTypeMap(ModuleRequestDTO.class, Module.class)
            .addMapping(dto -> dto.getRelatedId(), (module, v) -> module.getRelated().setId((Long) v));
}
```

## Exceptions

Ubicacion: `src/main/java/com/sistema_contable/sistema/contable/exceptions`.

Convenciones actuales:

- Todas las excepciones de dominio extienden `ModelExceptions`.
- `ModelExceptions` extiende `Exception`.
- Cada excepcion implementa `getHttpStatus()`.
- Los servicios lanzan estas excepciones; los resources las traducen a HTTP.
- Usar `HttpStatus.BAD_REQUEST` para validaciones de dominio, `HttpStatus.NOT_FOUND` para recursos no encontrados cuando corresponda, `HttpStatus.UNAUTHORIZED` para auth y `HttpStatus.CONFLICT` para conflictos.

Ejemplo:

```java
public class ModuleNotFindException extends ModelExceptions {

    public ModuleNotFindException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
```

## Util

Ubicacion: `src/main/java/com/sistema_contable/sistema/contable/util`.

Convenciones actuales:

- Utilidades transversales pueden ser clases simples y exponerse como `@Bean` en `SistemaContableApplication`.
- `DataInitializer` es `@Component` e implementa `CommandLineRunner`.
- `DateFormatter` convierte strings `yyyy-MM-dd` a `Date` de inicio o fin de dia.
- `JwtTokenUtil` genera/verifica tokens con prefijo `Bearer ` y obtiene subject, role y expiracion.
- `PasswordEncoder` encapsula hash y verificacion de passwords con `password4j`.
- Si se agrega una utilidad que sera inyectada, registrarla como `@Bean` o marcarla como componente siguiendo el estilo ya usado.

## Seguridad

Reglas de uso:

- Login usa `AuthenticationService.authenticate(User user)` y devuelve un mapa con key `token`.
- Los endpoints protegidos deben recibir `Authorization`.
- `authorize(token)` valida token y devuelve el usuario de base.
- `adminAuthorize(token)` valida token y exige rol `Role.ADMIN`.
- Si el endpoint necesita guardar auditoria del usuario, usar el `User` devuelto por `authorize(token)` y pasarlo al servicio.

## Checklist para agregar un modulo

1. Crear DTOs necesarios en `dto`: `XRequestDTO` y/o `XResponseDTO`.
2. Si el mapeo no es plano, agregar reglas en `dto.Mapper`.
3. Crear `XRepository` en `repository`, extendiendo `JpaRepository<X, Long>`.
4. Crear `XService` en el paquete de interfaces correspondiente.
5. Crear `XServiceImp` con `@Service`, inyectar repositorios/servicios necesarios y ubicar ahi reglas de negocio.
6. Crear excepciones especificas en `exceptions` si las existentes no expresan bien el error.
7. Crear `XResource` en `resources`, con autorizacion, try/catch de `ModelExceptions` y conversion DTO/modelo.
8. Si hace falta una utilidad inyectable, crearla en `util` y registrarla como bean o componente.
9. Mantener respuestas, rutas y nombres consistentes con el modulo existente mas parecido.

## Preferencias de estilo del proyecto

- Mantener package base `com.sistema_contable.sistema.contable`.
- Mantener imports explicitos y clases por capa.
- Mantener nombres existentes aunque haya typos historicos en modulos viejos; no renombrar metodos publicos o rutas sin revisar impacto.
- Evitar refactors globales cuando se agrega un modulo nuevo.
- No mover logica al controller.
- No devolver entidades directamente si ya existe o corresponde un `ResponseDTO`.
- No crear nuevas formas de manejo de errores sin migrar conscientemente el patron completo.
