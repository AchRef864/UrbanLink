# UrbanLink 
# date 20-02-2025 Last update for the project - on - feature/Users + Transport

## Project Structure

```
#urbanlink/

├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── tn/
│   │   │   │   ├── esprit/
│   │   │   │   │   ├── jdbc/
│   │   │   │   │   │   ├── controllers/
│   │   │   │   │   │   │   ├── AdminReclamationResponseController.java
│   │   │   │   │   │   │   ├── AjouterMaintenanceController.java
│   │   │   │   │   │   │   ├── AjouterUserController.java
│   │   │   │   │   │   │   ├── AjouterVehicleController.java
│   │   │   │   │   │   │   ├── ClientPageController.java
│   │   │   │   │   │   │   ├── ClientReclamationResponseController.java
│   │   │   │   │   │   │   ├── CreateNewAccountController.java
│   │   │   │   │   │   │   ├── DashboardClientController.java
│   │   │   │   │   │   │   ├── DashboardController.java
│   │   │   │   │   │   │   ├── DetailController.java
│   │   │   │   │   │   │   ├── EditMaintenanceController.java
│   │   │   │   │   │   │   ├── EditUserController.java
│   │   │   │   │   │   │   ├── EditVehicleController.java
│   │   │   │   │   │   │   ├── HelloAdminController.java
│   │   │   │   │   │   │   ├── Home.java
│   │   │   │   │   │   │   ├── HomeClientController.java
│   │   │   │   │   │   │   ├── HomeController.java
│   │   │   │   │   │   │   ├── ListerMaintenanceController.java
│   │   │   │   │   │   │   ├── ListerVehicleController.java
│   │   │   │   │   │   │   ├── LoginController.java
│   │   │   │   │   │   │   ├── ReclamationController.java
│   │   │   │   │   ├── entities/
│   │   │   │   │   │   ├── Maintenance.java
│   │   │   │   │   │   ├── Reclamation.java  
│   │   │   │   │   │   ├── User.java
│   │   │   │   │   │   ├── Vehicle.java
│   │   │   │   │   │   ├── VehicleType.java
│   │   │   │   │   ├── services/
│   │   │   │   │   │   ├── CRUD.java
│   │   │   │   │   │   ├── MaintenanceService.java
│   │   │   │   │   │   ├── ReclamationService.java
│   │   │   │   │   │   ├── UserService.java
│   │   │   │   │   │   ├── VehicleService.java
│   │   │   │   │   ├── tests/
│   │   │   │   │   │   ├── MainTest.java
│   │   │   │   │   ├── utils/
│   │   │   │   │   │   ├── MyDatabase.java
│   ├── resources/
│   │   ├── images/
│   │   │   ├── login-background.jpg
│   │   │   ├── login-background2.jpg
│   │   ├── AdminReclamationResponse.fxml
│   │   ├── AjouterMaintenance.fxml
│   │   ├── AjouterUser.fxml
│   │   ├── AjouterVehicle.fxml
│   │   ├── ClientPage.fxml
│   │   ├── ClientReclamationResponse.fxml
│   │   ├── CreateNewAccount.fxml
│   │   ├── Dashboard.fxml
│   │   ├── DashboardClient.fxml
│   │   ├── Detail.fxml
│   │   ├── EditMaintenance.fxml
│   │   ├── EditUser.fxml
│   │   ├── EditVehicle.fxml
│   │   ├── HelloAdmin.fxml
│   │   ├── Home.fxml
│   │   ├── HomeClient.fxml
│   │   ├── ListerMaintenance.fxml
│   │   ├── ListerVehicle.fxml
│   │   ├── Login.fxml
│   │   ├── Reclamation.fxml
│   │   ├── styles.css
├── pom.xml

```

## Explanation of Changes

### New Files Added
#### Controllers:
- `LoginController.java`: Handles the login page logic.
- `ClientPageController.java`: Handles the client page logic.
- `AdminReclamationResponseController.java`: Handles the admin reclamation response logic.
- `ClientReclamationResponseController.java`: Handles the client reclamation response logic.

#### FXML Files:
- `Login.fxml`: UI for the login page.
- `ClientPage.fxml`: UI for the client page.
- `AdminReclamationResponse.fxml`: UI for the admin reclamation response page.
- `ClientReclamationResponse.fxml`: UI for the client reclamation response page.

#### Entities:
- `Reclamation.java`: Updated to include the `reponseReclamation` field.

#### Services:
- `ReclamationService.java`: Handles database operations for reclamations.

### Updated Files
#### Controllers:
- `HelloAdminController.java`: Added navigation to the admin reclamation response page.
- `ClientPageController.java`: Added navigation to the client reclamation response page.

#### FXML Files:
- `HelloAdmin.fxml`: Added a button to navigate to the admin reclamation response page.
- `ClientPage.fxml`: Added a button to navigate to the client reclamation response page.

#### Entities:
- `Reclamation.java`: Added the `reponseReclamation` field and updated constructors, getters, and setters.

#### Services:
- `ReclamationService.java`: Added methods to fetch and update reclamations.

## Summary of Changes
1. Added new controllers and FXML files for the login, client page, and reclamation response pages.
2. Updated the `Reclamation` entity and `ReclamationService` to handle reclamation responses.
3. Updated the `HelloAdminController` and `ClientPageController` to include navigation to the new pages.

## Getting Started
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/urbanlink.git
   ```
2. Navigate to the project directory:
   ```bash
   cd urbanlink
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn javafx:run
   ```

## Contributing
Feel free to fork the project, create a feature branch, and submit a pull request!

---
 🔥 🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥


----------------------------------------------------------------------
## Some Notes 
```
Node
 ├── Parent
 │    ├── Region
 │    │    ├── Control (Button, Label, TextField, etc.)
 │    │    ├── Pane (VBox, HBox, GridPane, etc.)
 │    ├── Group
 ├── Shape (Rectangle, Circle, etc.)
 ├── ImageView
 ├── Text
```
--------------------------------------------------------------------
#LoginPage
![image](https://github.com/user-attachments/assets/3b68a1f9-3956-4c1c-b939-a0160fe3d8f5)

