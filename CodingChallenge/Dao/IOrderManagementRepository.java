package Dao;

import java.util.List;

import Exception.OrderNotFoundException;
import Exception.UserNotFoundException;
import Models.Product;
import Models.User;

public interface IOrderManagementRepository {

    void createOrder(User user, List<Product> products) throws UserNotFoundException;
    void cancelOrder(int userId, int orderId) throws OrderNotFoundException;
    void createProduct(User user, Product product);
    void createUser(User user);
    void getAllProducts();
    List<Product> getOrderByUser(User user) throws UserNotFoundException;
}
