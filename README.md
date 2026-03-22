# BRPG - Basic Role Playing Game

## Project Modernization (2026)
This project has been migrated from a legacy NetBeans/Ant structure to a standard Maven project targeting Java 21.

### Prerequisites
- **Java 21** or later
- **Maven 3.9+**
- **MySQL Database** (configured in `src/main/java/clases/dbDelegate.java`)

### Setup Instructions
1.  **Dependencies**: This project requires the `JGame` library.
    -   Download `JGame` (version 3.5 recommended).
    -   Rename the jar to `jgame-all.jar`.
    -   Place it in the `lib` directory:
        ```
        brpg/
        ├── lib/
        │   └── jgame-all.jar
        ```
    -   *Note: If you have a different version or name, update the `systemPath` in `pom.xml`.*

2.  **Build**:
    Run the following command to compile and package the project:
    ```bash
    mvn clean package
    ```

3.  **Run**:
    Execute the application (ensure database is running):
    ```bash
    mvn exec:java
    ```

### Structure
- `src/main/java`: Source code (Packages: `clases`, `extensiones`)
- `src/main/resources`: Assets (`media`) and SQL scripts (`BBDD`)
- `lib`: Local dependencies (JGame)

### Database
Update `src/main/java/clases/dbDelegate.java` with your MySQL credentials before running.
