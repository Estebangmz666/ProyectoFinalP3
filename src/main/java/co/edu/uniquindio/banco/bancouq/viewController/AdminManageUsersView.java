package co.edu.uniquindio.banco.bancouq.viewController;

import co.edu.uniquindio.banco.bancouq.controller.BancoController;
import co.edu.uniquindio.banco.bancouq.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AdminManageUsersView {
     @FXML
    private ListView<User> usersListView;
    @FXML
    private TextField searchUserField;
    @FXML
    private Label statusLabel;

    private final BancoController bancoController;

    public AdminManageUsersView() {
        this.bancoController = BancoController.getInstance();
    }

    @FXML
    public void initialize() {
        // Load the list of users into the ListView
        usersListView.getItems().setAll(bancoController.getUserService().getAllUsers());
    }

    @FXML
    public void handleSearchUser(ActionEvent event) {
        String searchQuery = searchUserField.getText().toLowerCase();
        usersListView.getItems().setAll(
                bancoController.getUserService().getAllUsers().stream()
                        .filter(user -> user.getName().toLowerCase().contains(searchQuery))
                        .toList()
        );
    }

    @FXML
    public void handleDeleteUser(ActionEvent event) {
        User selectedUser = usersListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            bancoController.getUserService().deleteUser(String.valueOf(selectedUser.getUserId()));
            usersListView.getItems().remove(selectedUser);
            statusLabel.setText("User deleted successfully!");
        } else {
            statusLabel.setText("No user selected to delete.");
        }
    }
}
