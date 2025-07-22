package com.joelle.backend.clients;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ClientsResolver {
        private final ClientsRepository clientsRepository;

    @Autowired
    public ClientsResolver(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    @QueryMapping
    public List<Clients> allClients() {
        return clientsRepository.findAll();
    }

    @QueryMapping
    public Clients getClientById(@Argument Long id) {
        return clientsRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public Clients createClient(@Argument String name, @Argument String phone) {
        System.out.println("Received createClient: " + name + ", " + phone);
        Clients client = new Clients(name, phone);
        try {
            return clientsRepository.save(client);
        } catch (Exception e) {
            System.out.println("Error saving client: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
