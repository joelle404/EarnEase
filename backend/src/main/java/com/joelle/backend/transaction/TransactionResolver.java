package com.joelle.backend.transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.joelle.backend.clients.Clients;
import com.joelle.backend.clients.ClientsRepository;
import com.joelle.backend.service.Service;
import com.joelle.backend.service.ServiceRepository;
import com.joelle.backend.staff.Staff;
import com.joelle.backend.staff.StaffRepository;

@Controller
public class TransactionResolver {
    
    private final TransactionRepository transactionRepository;
    private final ClientsRepository clientsRepository;
    private final StaffRepository staffRepository;
    private final ServiceRepository serviceRepository;

    @Autowired
    public TransactionResolver(TransactionRepository transactionRepository,
                               ClientsRepository clientsRepository,
                               StaffRepository staffRepository,
                               ServiceRepository serviceRepository) {
        this.transactionRepository = transactionRepository;
        this.clientsRepository = clientsRepository;
        this.staffRepository = staffRepository;
        this.serviceRepository = serviceRepository;
    }

    @QueryMapping
    public List<Transaction> allTransactions() {
        return transactionRepository.findAll();
    }

    @QueryMapping
    public Transaction getTransactionById(@Argument Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public Transaction createTransaction(@Argument Long clientId,
                                         @Argument Long staffId,
                                         @Argument Long serviceId,
                                         @Argument Double amountPaid,
                                         @Argument Double percentageGiven,
                                         @Argument Long percentageRecipientId,
                                         @Argument LocalDateTime date) {
        Clients client = clientsRepository.findById(clientId).orElse(null);
        Staff staff = staffRepository.findById(staffId).orElse(null);
        Service service = serviceRepository.findById(serviceId).orElse(null);
        Staff recipient = staffRepository.findById(percentageRecipientId).orElse(null);

        if (client == null || staff == null || service == null || recipient == null) {
            System.out.println("Invalid reference ID(s): "
                    + "client=" + clientId + ", staff=" + staffId
                    + ", service=" + serviceId + ", recipient=" + percentageRecipientId);
            return null;
        }
        Transaction transaction = new Transaction(client, staff, null, amountPaid, percentageGiven, recipient, date);
        try {
            return transactionRepository.save(transaction);
        } catch (Exception e) {
            System.out.println("Error saving transaction: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
