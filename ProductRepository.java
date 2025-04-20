package com.unicesumar.repository;

import com.unicesumar.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProductRepository implements EntityRepository<Product> {
    private final Connection connection;

    public ProductRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Product entity) {
        String query = "INSERT INTO products VALUES (?, ?, ?)";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, entity.getUuid().toString());
            stmt.setString(2, entity.getName());
            stmt.setDouble(3, entity.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Product> findById(UUID id) {
        String query = "SELECT * FROM products WHERE uuid = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Product product = new Product(
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("name"),
                        resultSet.getDouble("price")
                );
                return Optional.of(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();

    }

    @Override
    public List<Product> findAll() {
        String query = "SELECT * FROM products";
        List<Product> products = new LinkedList<>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Product product = new Product(
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("name"),
                        resultSet.getDouble("price")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;

    }

    @Override
    public void deleteById(UUID id) {
        String query = "DELETE FROM products WHERE uuid = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Product> findByIdS(String id) {
        List<Product> produtos = new ArrayList<>();
        String[] uuidStrings = id.split(",");
        List<UUID> uuidList = new ArrayList<>();

        for (String str : uuidStrings) {
            try {
                uuidList.add(UUID.fromString(str.trim()));
            } catch (IllegalArgumentException e) {
                System.out.println("UUID inválido ignorado: " + str.trim());
            }
        }

        if (uuidList.isEmpty()) {
            return produtos;
        }

        String placeholders = String.join(",", java.util.Collections.nCopies(uuidList.size(), "?"));
        String query = "SELECT * FROM products WHERE uuid IN (" + placeholders + ")";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            for (int i = 0; i < uuidList.size(); i++) {
                stmt.setString(i + 1, uuidList.get(i).toString());
            }

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Product product = new Product(
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("name"),
                        resultSet.getDouble("price")
                );
                produtos.add(product);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar produtos: " + e.getMessage());
        }

        return produtos;
    }


}
