package com.unicesumar;

import com.unicesumar.entities.Product;
import com.unicesumar.entities.User;
import com.unicesumar.repository.ProductRepository;
import com.unicesumar.repository.UserRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProductRepository listaDeProdutos = null;
        Connection conn = null;
        UserRepository listaDeUsuarios = null;
        
        // Parâmetros de conexão
        String url = "jdbc:sqlite:database.sqlite";

        // Tentativa de conexão
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                listaDeProdutos = new ProductRepository(conn);
                listaDeUsuarios = new UserRepository(conn);
            } else {
                System.out.println("Falha na conexão.");
                System.exit(1);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            System.exit(1);
        }

        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n---MENU---");
            System.out.println("1 - Cadastrar Produto");
            System.out.println("2 - Listas Produtos");
            System.out.println("3 - Buscar Produtos");
            System.out.println("4 - Cadastrar Usuário");
            System.out.println("5 - Listar Usuários");
            System.out.println("6 - Buscar usuário");
            System.out.println("7 - Sair");
            System.out.println("Escolha uma opção: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Cadastrar Produto");
                    listaDeProdutos.save(new Product("Teste", 10));
                    listaDeProdutos.save(new Product("Computador", 3000));

                    break;
                case 2:
                    System.out.println("Listar Produtos");
                    List<Product> products = listaDeProdutos.findAll();
                    products.forEach(System.out::println);
                    break;
                case 3:
                    System.out.println("Bucas Produtos (ID)");
                    scanner.nextLine();

                    break;
                case 4:
                    System.out.println("Cadastrar Usuário");
                    scanner.nextLine();
                    System.out.print("Digite o nome: ");
                    String password = scanner.nextLine();

                    System.out.print("Digite o e-mail: ");
                    String name = scanner.nextLine();

                    System.out.print("Digite a senha: ");
                    String email = scanner.nextLine();

                    User newUser = new User(password, name, email);
                    listaDeUsuarios.save(newUser);
                    break;
                case 5:
                    System.out.println("Listar Usuários");
                    List<User> users = listaDeUsuarios.findAll();
                    users.forEach(user -> System.out.println("Email: " + user.getEmail()));
                    break;
                case 6:
                    System.out.println("Buscar Usuario");
                    scanner.nextLine();
                    System.out.print("Digite o email do usuário: ");
                    String emailBusca = scanner.nextLine();
                    Optional<User> user = listaDeUsuarios.findByEmail(emailBusca);
                    if (user.isPresent()) {
                        User userBusca = user.get();
                        System.out.println("Usuário encontrado:");
                        System.out.println("Nome : " + userBusca.getName());
                    } else {
                        System.out.println("Usuário com email \"" + emailBusca + "\" não encontrado.");
                    }
                    break;
                case 7:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente");
            }

        } while (option != 7);

        scanner.close();
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
