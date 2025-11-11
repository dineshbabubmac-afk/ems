
# 🛠️ Setting Up the `reward_system` Database

1. **Create a PostgreSQL database** named `reward_system`.

2. **Default credentials are hardcoded** as:
    - **Username**: `postgres`
    - **Password**: `password`

3. **To change the credentials** to match your local PostgreSQL setup:
    - Open the `pom.xml` file (if needed for plugin/database version)
    - Update the database properties in `application.dev.yaml`

4. **Build the project** using Maven:

   ```bash
   mvn clean install
   ```

5. **Run the project** with your preferred IDE or using the command line:

   ```bash
   mvn spring-boot:run
   ```
