package Client;


import java.util.List;
import java.util.Scanner;

import Dao.OrderProcessor;
import Exception.OrderNotFoundException;
import Exception.UserNotFoundException;
import Models.Clothing;
import Models.Electronics;
import Models.Product;
import Models.User;

public class OrderManagement {

    static OrderProcessor orderProcessor = new OrderProcessor();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
       
    

        while (true) {
            System.out.println("Order Management System");
            System.out.println("1. Create User");
            System.out.println("2. Create Product");
            System.out.println("3. Cancel Order");
            System.out.println("4. Get All Products");
            System.out.println("5. Get Order by User");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            

            if (choice == 1) {
                createUser();
            } else if (choice == 2) {
                createProduct();
            } else if (choice == 3) {
                cancelOrder();
            } else if (choice == 4) {
                getAllProducts();
            } else if (choice == 5) {
                getOrderbyUser();
            } else if (choice == 6) {
               
                System.out.println("Exiting... Thank you!");
                break;
            } else {
                System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }

        scanner.close();
    }

    private static void createUser() {
        System.out.println("Create User");
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role (Admin/User): ");
        String role = scanner.nextLine();

        User user = new User(userId, username, password, role);
        orderProcessor.createUser(user);
        System.out.println("User created successfully.");
    }

    private static void createProduct() {
        System.out.println("\nCreate Product");
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter quantity in stock: ");
        int quantityInStock = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter type (Electronics/Clothing): ");
        String type = scanner.nextLine();

        Product product = new Product(productId, productName, description, price, quantityInStock, type);

       if (type.equals("Electronics")) {
    System.out.print("Enter brand: ");
    String brand = scanner.nextLine();
    System.out.print("Enter warranty period: ");
    int warrantyPeriod = scanner.nextInt();
    Electronics electronics = new Electronics();
    electronics.setBrand(brand);
    electronics.setWarrantyPeriod(warrantyPeriod);
    product = electronics;
} else if (type.equals("Clothing")) {
    System.out.print("Enter size: ");
    String size = scanner.nextLine();
    System.out.print("Enter color: ");
    String color = scanner.nextLine();
    Clothing clothing = new Clothing();
    clothing.setSize(size);
    clothing.setColor(color);
    product = clothing;
}
   
        orderProcessor.createProduct(new User(1, "", "", ""), product);
        System.out.println("Product created successfully.");
    }

    private static void cancelOrder() {
        System.out.println("Cancel Order");
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();

        try {
            orderProcessor.cancelOrder(userId, orderId);
            System.out.println("Order canceled successfully.");
        } catch (OrderNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getAllProducts() {
        orderProcessor.getAllProducts();
    }
    

    private static void getOrderbyUser() {
        System.out.println("Get Order by User");
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();

        try {
            List<Product> products = orderProcessor.getOrderByUser(new User(userId, "", "", ""));
            System.out.println("Products Ordered by User " + userId + ":");
            for (Product product : products) {
                System.out.println(product);
            }
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
