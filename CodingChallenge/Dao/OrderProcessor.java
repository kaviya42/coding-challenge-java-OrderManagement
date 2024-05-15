package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Connect.DataConnect;
import Exception.OrderNotFoundException;
import Exception.UserNotFoundException;
import Models.Product;
import Models.User;

public class OrderProcessor implements IOrderManagementRepository {

    private Connection connection;

    public OrderProcessor() {
        connection = DataConnect.getConnect();
    }

 
    public void createOrder(User user, List<Product> products) throws UserNotFoundException {
        try {
            
            if (!userExists(user.getUserId())) {
                createUser(user);
            }

            for (Product product : products) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO Orders (userId, productId) VALUES (?, ?)");
                statement.setInt(1, user.getUserId());
                statement.setInt(2, product.getProductId());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new UserNotFoundException("Error creating order for user: " + ex.getMessage());
        }
    }

   
    public void cancelOrder(int userId, int orderId) throws OrderNotFoundException {
        try {
            if (!orderExists(userId, orderId)) {
                throw new OrderNotFoundException("Order with ID " + orderId + " for user ID " + userId + " not found.");
            }

            PreparedStatement statement = connection.prepareStatement("DELETE FROM Orders WHERE userId = ? AND orderId = ?");
            statement.setInt(1, userId);
            statement.setInt(2, orderId);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new OrderNotFoundException("Error canceling order: " + ex.getMessage());
        }
    }

    
    public void createProduct(User user, Product product) {
        try {
           
            if (!userExists(user.getUserId())) {
                createUser(user);
            }

            
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Product (productId, productName, description, price, quantityInStock, type) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setInt(1, product.getProductId());
            statement.setString(2, product.getProductName());
            statement.setString(3, product.getDescription());
            statement.setDouble(4, product.getPrice());
            statement.setInt(5, product.getQuantityInStock());
            statement.setString(6, product.getType());
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

 
    public void createUser(User user) {
        try {
            String query = "INSERT INTO user (user_id, username, password, role) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query); 
            preparedStatement.setInt(1, user.getUserId());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public void getAllProducts() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Product");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setProductId(resultSet.getInt("productId"));
                System.out.println("productId: " + product.getProductId());
                product.setProductName(resultSet.getString("productName"));
                System.out.println("productName: " + product.getProductName());
                product.setDescription(resultSet.getString("description"));
                System.out.println("description: " + product.getDescription());
                product.setPrice(resultSet.getDouble("price"));
                System.out.println("price: " + product.getPrice());
                product.setQuantityInStock(resultSet.getInt("quantityInStock"));
                System.out.println("quantityInStock: " + product.getQuantityInStock());
                product.setType(resultSet.getString("type"));
                System.out.println("type: " + product.getType());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    

 
    public List<Product> getOrderByUser(User user) throws UserNotFoundException {
        List<Product> products = new ArrayList<>();
        try {
            if (!userExists(user.getUserId())) {
                throw new UserNotFoundException("User with ID " + user.getUserId() + " not found.");
            }

            PreparedStatement statement = connection.prepareStatement("SELECT p.* FROM Product p JOIN Orders o ON p.productId = o.productId WHERE o.userId = ?");
            statement.setInt(1, user.getUserId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();

                  int productId = resultSet.getInt("productId");
                     System.out.println("Product ID: " + productId);
                product.setProductId(productId);
                String productName = resultSet.getString("productName");
                System.out.println("Product Name: " + productName);
                product.setProductName(productName);
                String description = resultSet.getString("description");
                System.out.println("Description: " + description);
                product.setDescription(description);
                double price = resultSet.getDouble("price");
                System.out.println("Price: " + price);
                product.setPrice(price);
               int quantityInStock = resultSet.getInt("quantityInStock");
                System.out.println("Quantity in Stock: " + quantityInStock);
                product.setQuantityInStock(quantityInStock);
               String type = resultSet.getString("type");
               System.out.println("Type: " + type);
                product.setType(type);

               products.add(product);

            }
        } catch (SQLException ex) {
            throw new UserNotFoundException("Error calculating tax " + ex.getMessage());
        }
        return products;
    }

    private boolean userExists(int userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM User WHERE userId = ?");
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    private boolean orderExists(int userId, int orderId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Orders WHERE userId = ? AND orderId = ?");
        statement.setInt(1, userId);
        statement.setInt(2, orderId);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }
}
