# GUÍA DE DESARROLLO, PRUEBAS Y CAPTURAS: PARTE 2
**Responsable:** Erick Arturo Gamarra Mundaca  
**Evaluación 02 – Desarrollo de Aplicaciones Web Avanzado**  
**Docente:** Coello Palomino, Ricardo  
**Repositorio:** `https://github.com/Jesus-Rocha-B/Eva02_DesarrolloWeb.git`

---

## 1. Contexto de Integración (Estado del Proyecto)
El integrante **Jesús Rocha** ya completó con éxito la **Parte 1**:
- Configuración de Spring Security 6 y Bean Validation en `pom.xml`.
- Modelado JPA de `Usuario` y `Rol` con relación `@ManyToMany` en la tabla `usuario_roles`.
- Encriptación de contraseñas con BCrypt y precarga de usuarios semilla (`admin`, `medico1`, `recepcion1`).
- Interfaces web de Usuarios (`usuarios.html`) y Roles (`roles.html`) con Bootstrap 5.
- Formulario de Login personalizado (`login.html`), pantalla de Acceso Denegado (`403.html`) y menú condicional `sidebar.html`.
- Infraestructura de Auditoría Automática orientada a aspectos (AOP) con `@Auditable` y `AuditoriaAspect`.

👉 **Tu objetivo en la Parte 2:**
1. Consolidar y evidenciar las **Relaciones JPA de Negocio** (`RecetaMedica` $\leftrightarrow$ `DetalleReceta` y `Medicamento` $\leftrightarrow$ `Lote`) para la **Pregunta 1**.
2. Extender la **Auditoría Automática AOP** al módulo de Farmacia y Recetas para la **Pregunta 2**.
3. Demostrar el **Control de Acceso y Redirección** de los perfiles **MÉDICO** y **RECEPCIONISTA** para la **Pregunta 5**.

---

## PASO 7 (Erick 1): Consolidación de Relaciones JPA de Negocio (Pregunta 1)

### ¿Qué debes hacer a nivel técnico?
Verificar y documentar en el código las dos relaciones relacionales de tu responsabilidad:
1. **Relación Uno a Muchos Bidireccional (`@OneToMany` / `@ManyToOne`):**  
   - `RecetaMedica.java` $\leftrightarrow$ `DetalleReceta.java`.
   - Asegurar que `RecetaMedica` tenga:
     ```java
     @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<DetalleReceta> detalles = new ArrayList<>();
     ```
   - Y `DetalleReceta` tenga:
     ```java
     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "receta_id", nullable = false)
     private RecetaMedica receta;
     ```
   - *Garantía de Integridad:* La cascada asegura que al guardar una receta se persistan o eliminen sus renglones de prescripción sin dejar registros huérfanos.
2. **Relación Uno a Muchos (`@OneToMany` / `@ManyToOne`):**  
   - `Medicamento.java` $\leftrightarrow$ `Lote.java`.
   - En `Lote.java`: `@ManyToOne @JoinColumn(name = "medicamento_id", nullable = false)`.
   - *Garantía de Integridad:* Clave foránea con restricción para evitar la eliminación física de medicamentos que cuenten con stock físico en almacén.

### 📸 Capturas para el Informe (Paso 7):
* **Captura 7.1:** Código de `RecetaMedica.java` y `DetalleReceta.java` mostrando las anotaciones `@OneToMany(cascade = CascadeType.ALL)` y `@ManyToOne`.
* **Captura 7.2:** Diagrama relacional o estructura de tablas en MySQL Workbench (`DESCRIBE detalle_receta;` y `DESCRIBE lote;`) mostrando las columnas `receta_id` y `medicamento_id` como claves foráneas (FK).

### 📝 Párrafo listo para tu documento (Paso 7 - en un solo párrafo):
> Para el desarrollo de las relaciones de negocio correspondientes a la Pregunta 1, consolidamos la estructura relacional entre las entidades `RecetaMedica` y `DetalleReceta` aplicando una asociación bidireccional mediante `@OneToMany` y `@ManyToOne` con configuración de cascada completa (`CascadeType.ALL`) y `orphanRemoval = true`, lo cual asegura la integridad referencial al persistir la cabecera de la prescripción junto con todos sus renglones de medicamentos de forma atómica y evita la existencia de detalles huérfanos en la base de datos. De igual forma, enlazamos la entidad `Medicamento` con `Lote` a través de `@ManyToOne` definiendo la clave foránea `medicamento_id` en la tabla física de MySQL, manteniendo la regla de negocio que prohíbe la eliminación de medicamentos que registren existencias físicas o movimientos en el inventario hospitalario.

### 💻 Comando para tu Commit:
```bash
git add src/main/java/com/tecsup/consultamedica/model/RecetaMedica.java src/main/java/com/tecsup/consultamedica/model/DetalleReceta.java src/main/java/com/tecsup/consultamedica/model/Lote.java
git commit -m "Se consolidaron las relaciones JPA entre RecetaMedica, DetalleReceta y Lote aplicando cascada e integridad referencial"
git push origin main
```

---

## PASO 8 (Erick 2): Extensión de la Auditoría AOP a Farmacia y Recetas (Pregunta 2)

### ¿Qué debes hacer a nivel técnico?
Aprovechar la infraestructura AOP ya creada (`@Auditable` y `AuditoriaAspect`) y colocar la anotación `@Auditable` en los métodos de servicio de tu módulo:

1. En `MedicamentoService.java`:
   ```java
   @Auditable(operacion = "REGISTRO", entidad = "Medicamento")
   public Medicamento registrarMedicamento(Medicamento medicamento) { ... }
   ```
2. En `RecetaMedicaService.java` (o el servicio de emisión/despacho de recetas):
   ```java
   @Auditable(operacion = "REGISTRO", entidad = "RecetaMedica")
   public RecetaMedica guardarReceta(RecetaMedica receta) { ... }
   ```

### 📸 Capturas para el Informe (Paso 8):
* **Captura 8.1:** Código fuente en IntelliJ mostrando el método de servicio de medicamentos o recetas con la anotación `@Auditable`.
* **Captura 8.2 (Comprobación en vivo):** Ingresar a la aplicación web, registrar un nuevo medicamento o receta médica, y abrir la pantalla `/web/auditoria` mostrando la nueva fila generada automáticamente con la operación `REGISTRO` sobre la entidad `Medicamento` o `RecetaMedica`.
* **Captura 8.3 (En Base de Datos):** Consulta en MySQL Workbench (`SELECT * FROM auditoria WHERE entidad IN ('Medicamento', 'RecetaMedica');`) demostrando que los datos se guardaron en la tabla física.

### 📝 Párrafo listo para tu documento (Paso 8 - en un solo párrafo):
> Para dar cumplimiento a la Pregunta 2 sobre el mecanismo de auditoría en los módulos de negocio, integramos la anotación personalizada `@Auditable` sobre los métodos transaccionales de `MedicamentoService` y `RecetaMedicaService`, permitiendo que el componente `AuditoriaAspect` intercepte automáticamente mediante `@AfterReturning` la emisión de nuevas prescripciones y el alta de productos farmacéuticos. Esta integración extrae en segundo plano el usuario autenticado (médico o recepcionista) desde `SecurityContextHolder`, la fecha y hora de la transacción y el identificador generado, almacenando de forma transparente el evento en la tabla `auditoria` de MySQL y reflejándolo de inmediato en la vista web de bitácora sin necesidad de código invasivo dentro de la lógica del catálogo ni de las recetas médicas.

### 💻 Comando para tu Commit:
```bash
git add src/main/java/com/tecsup/consultamedica/service/
git commit -m "Se integró la anotación @Auditable en los servicios de Medicamento y RecetaMedica para registrar automáticamente operaciones mediante Spring AOP"
git push origin main
```

---

## PASO 9 (Erick 3): Pruebas del Rol Médico y Recepcionista (Pregunta 5)

### ¿Qué debes hacer a nivel técnico?
Verificar la matriz de permisos configurada en `SecurityConfig`:
1. **Prueba del Perfil MÉDICO:**
   - Iniciar sesión en `/login` con el usuario: `medico1` y contraseña `medico123`.
   - Comprobar que Spring Security lo redirige de inmediato al módulo de recetas (`/web/recetas`).
   - Comprobar que en el menú lateral ([`sidebar.html`](file:///c:/Users/enriq/Downloads/Eval2_DesarrolloWeb/Eva02_DesarrolloWeb/src/main/resources/templates/fragments/sidebar.html)) las opciones de Usuarios, Roles y Auditoría están **completamente ocultas**.
2. **Prueba del Perfil RECEPCIONISTA:**
   - Iniciar sesión en `/login` con el usuario: `recepcion1` y contraseña `recep123`.
   - Comprobar que lo redirige al módulo de inventario de lotes (`/web/lotes`).
   - Comprobar que tiene acceso a Medicamentos, Lotes y Kardex, pero no a la administración de usuarios.

### 📸 Capturas para el Informe (Paso 9):
* **Captura 9.1:** Inicio de sesión con las credenciales de `medico1` en `/login`.
* **Captura 9.2:** Pantalla de `/web/recetas` tras la redirección automática, destacando el sidebar filtrado donde solo se muestra el *"Área Médica"* y abajo el badge `ROLE_MEDICO`.
* **Captura 9.3:** Pantalla de `/web/lotes` con la sesión de `recepcion1` mostrando el acceso al stock y el badge `ROLE_RECEPCIONISTA`.

### 📝 Párrafo listo para tu documento (Paso 9 - en un solo párrafo):
> Para evidenciar el control de acceso según el rol establecido en la Pregunta 5, realizamos las pruebas funcionales de autenticación y autorización con los perfiles operativos del hospital: al ingresar con las credenciales del usuario `medico1`, el manejador `CustomAuthenticationSuccessHandler` validó el rol `ROLE_MEDICO` y lo redirigió de manera directa a la interfaz de recetas médicas (`/web/recetas`), mientras que las directivas `sec:authorize` de Thymeleaf ocultaron del menú lateral las opciones administrativas de usuarios, roles y bitácora; por su parte, al iniciar sesión con el usuario `recepcion1`, el sistema concedió acceso al control de inventario, stock por lotes y kardex en `/web/lotes`, garantizando tanto a nivel de backend con `SecurityFilterChain` como a nivel visual en las plantillas que cada empleado hospitalario interactúe exclusivamente con los módulos autorizados para sus funciones.

### 💻 Comando para tu Commit:
```bash
git commit --allow-empty -m "Se validaron las pruebas funcionales de redirección y control de acceso por roles para Médico y Recepcionista"
git push origin main
```

---

## 4. Checklist Resumen de Capturas para Erick (Documento Final)

| N° | Sección del Informe | Qué abrir en pantalla | Qué debe verse claramente |
| :---: | :--- | :--- | :--- |
| **7.1** | Pregunta 1: Relaciones JPA | `RecetaMedica.java` y `DetalleReceta.java` en IntelliJ | `@OneToMany(cascade = CascadeType.ALL)` y `@ManyToOne` |
| **7.2** | Pregunta 1: Relaciones BD | MySQL Workbench | Tablas `detalle_receta` y `lote` con columnas FK `receta_id` y `medicamento_id` |
| **8.1** | Pregunta 2: Auditoría AOP | `RecetaMedicaService.java` en IntelliJ | El método con la anotación `@Auditable(operacion = "REGISTRO", ...)` |
| **8.2** | Pregunta 2: Auditoría Web | Navegador en `http://localhost:8086/web/auditoria` | Tabla de bitácora mostrando el registro de una receta o medicamento |
| **8.3** | Pregunta 2: Auditoría BD | MySQL Workbench | `SELECT * FROM auditoria;` con los registros físicos |
| **9.1** | Pregunta 5: Login Médico | Navegador en `http://localhost:8086/login` | Formulario completado con `medico1` / `medico123` |
| **9.2** | Pregunta 5: Redirección Médico | Navegador en `http://localhost:8086/web/recetas` | Pantalla de recetas con el sidebar mostrando solo el Área Médica y el badge de rol |
| **9.3** | Pregunta 5: Recepcionista | Navegador en `http://localhost:8086/web/lotes` | Pantalla de lotes con la sesión de `recepcion1` |
