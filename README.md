# UrbanLink

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
│   │   │   │   │   │   │   ├── AjouterUserController.java
│   │   │   │   │   │   │   ├── DetailController.java
│   │   │   │   │   │   │   ├── EditUserController.java
│   │   │   │   │   │   │   ├── HelloAdminController.java
│   │   │   │   │   │   │   ├── HelloClientController.java
│   │   │   │   │   │   │   ├── Home.java
│   │   │   │   │   │   │   ├── ReclamationController.java
│   │   │   │   │   │   │   ├── UpdateUserController.java
│   │   │   │   │   │   │   ├── LoginController.java  <-- New
│   │   │   │   │   │   │   ├── ClientPageController.java  <-- New
│   │   │   │   │   │   │   ├── AdminReclamationResponseController.java  <-- New
│   │   │   │   │   │   │   ├── ClientReclamationResponseController.java  <-- New
│   │   │   │   │   ├── entities/
│   │   │   │   │   │   ├── User.java
│   │   │   │   │   │   ├── Reclamation.java  <-- Updated
│   │   │   │   │   ├── services/
│   │   │   │   │   │   ├── CRUD.java
│   │   │   │   │   │   ├── UserService.java
│   │   │   │   │   │   ├── ReclamationService.java  <-- New
│   │   │   │   │   ├── tests/
│   │   │   │   │   │   ├── MainTest.java
│   │   │   │   │   ├── utils/
│   │   │   │   │   │   ├── MyDatabase.java
│   ├── resources/
│   │   ├── AjouterUser.fxml
│   │   ├── Detail.fxml
│   │   ├── EditUser.fxml
│   │   ├── HelloAdmin.fxml
│   │   ├── HelloClient.fxml
│   │   ├── Reclamation.fxml
│   │   ├── UpdateUser.fxml
│   │   ├── Login.fxml  <-- New
│   │   ├── ClientPage.fxml  <-- New
│   │   ├── AdminReclamationResponse.fxml  <-- New
│   │   ├── ClientReclamationResponse.fxml  <-- New
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
**Let me know if you need further modifications! 😊**


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

