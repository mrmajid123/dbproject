# Train Management System

This is a Java-based Train Management System developed for a database lab project using Eclipse IDE. The system provides a graphical user interface (GUI) to manage train schedules, station information, ticket bookings, and passenger details, with data stored in a MySQL database.

## Prerequisites
- **Java Development Kit (JDK)**: Version 8 or higher.
- **Eclipse IDE**: For running the Java application.
- **MySQL Server**: For the database schema.
- **MySQL JDBC Driver**: Download `mysql-connector-java-x.x.xx.jar` and add it to the Eclipse build path.

## Setup Instructions
1. **Clone the Repository**:
   - Clone this repository to your local machine:
     ```
     git clone https://github.com/yourusername/TrainManagementSystem.git
     ```

2. **Set Up the Database**:
   - Install MySQL and open MySQL Workbench.
   - Create a database named `orange_line_db`.
   - Run the SQL script `schema.sql` in MySQL Workbench to create the necessary tables and insert initial data.

3. **Configure the Project in Eclipse**:
   - Import the project into Eclipse (File > Import > Existing Projects into Workspace).
   - Add the MySQL JDBC driver to the build path:
     - Right-click the project > Build Path > Configure Build Path > Libraries > Add External JARs > Select `mysql-connector-java-x.x.xx.jar`.
   - Update the database connection string in `TrainManagementSystem.java` (line 183) if your MySQL username or password differs:
     ```
     DriverManager.getConnection("jdbc:mysql://localhost:3306/orange_line_db", "root", "khan1223")
     ```

4. **Run the Application**:
   - Click "Run As > Java Application" in Eclipse.
   - Log in with:
     - Username: `admin`
     - Password: `admin123`

## Features
- **Login Panel**: Authenticate with admin credentials.
- **Menu Dashboard**: Navigate to Train Schedule, Station Info, Ticket Booking, and Passenger sections.
- **CRUD Operations**: Add, View, Edit, and Delete records for trains, stations, bookings, and passengers.
- **Database Integration**: Data is stored and retrieved from the `orange_line_db` MySQL database.

## Notes
- Ensure MySQL server is running on `localhost:3306` with the username `root` and password `khan1223` (or modify the code accordingly).
- The interface uses Swing for GUI and connects to the SQL schema provided in `schema.sql`.

## Author
Abdul Majid

