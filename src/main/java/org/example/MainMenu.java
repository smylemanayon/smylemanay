
package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.Optional;
import java.util.Scanner;

public class MainMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final UserDao userDao = new UserDao();

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\n========================================");
            System.out.println("       VOTING MANAGEMENT SYSTEM");
            System.out.println("========================================");
            System.out.println("[1] Register Account");
            System.out.println("[2] Login");
            System.out.println("[3] Exit");
            System.out.print("Choose: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> {
                    System.out.println("Thank you for using the Voting System!");
                    running = false;
                }
                default -> System.out.println(
                        "Invalid choice. Please choose 1, 2, or 3."
                );
            }
        }
    }

    private void register() {
        System.out.println("\n---------- REGISTER ACCOUNT ----------");

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (username.isEmpty() || password.isBlank()) {
            System.out.println("Username and password are required.");
            return;
        }

        if (userDao.usernameExists(username)) {
            System.out.println("Username already exists. Try another.");
            return;
        }

        String role = chooseRole();

        if (role == null) {
            System.out.println("Invalid role. Registration cancelled.");
            return;
        }

        if (userDao.register(username, password, role)) {
            System.out.println("\nRegistration successful!");
            System.out.println("Username: " + username);
            System.out.println("Role: " + role);
            System.out.println("Status: PENDING");
            System.out.println("Please wait for administrator approval.");
        }
    }

    private String chooseRole() {
        System.out.println("\nSelect Account Role:");
        System.out.println("[1] VOTER");
        System.out.print("Choose: ");

        return switch (scanner.nextLine().trim()) {
            case "1" -> "VOTER";
            default -> null;
        };
    }

    private void login() {
        System.out.println("\n-------------- LOGIN ----------------");

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Please enter your username and password.");
            return;
        }

        Optional<User> result = userDao.login(username, password);

        if (result.isPresent()) {
            User user = result.get();

            System.out.println("\nLogin successful!");
            System.out.println("Welcome, " + user.username() + "!");
            System.out.println("Role: " + user.roles());

            if ("ADMIN".equalsIgnoreCase(user.roles())) {
                System.out.println("Administrator account detected.");
                // Administrator menu can be connected here.
            } else if ("VOTER".equalsIgnoreCase(user.roles())) {
                System.out.println("Voter account detected.");
                // Voter menu can be connected here.
            } else {
                System.out.println("Unknown account role.");
            }

        } else {
            System.out.println(
                    "Login failed. Your credentials may be incorrect, "
                            + "or your account may still be pending approval."
            );
        }
    }
}
