package com.joelle.backend.transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Sort;

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
    private final TransactionService transactionService;


    @Autowired
    public TransactionResolver(TransactionRepository transactionRepository,
                               ClientsRepository clientsRepository,
                               StaffRepository staffRepository,
                               ServiceRepository serviceRepository,
                               TransactionService transactionService) {
        this.transactionRepository = transactionRepository;
        this.clientsRepository = clientsRepository;
        this.staffRepository = staffRepository;
        this.serviceRepository = serviceRepository;
        this.transactionService = transactionService;
    }

@QueryMapping
public List<Transaction> allTransactions(@Argument Long staffId) {
    return transactionRepository.findByStaffId(staffId,Sort.by(Sort.Direction.DESC, "date", "time"));
}


    // @QueryMapping
    // public Transaction getTransactionById(@Argument Long id) {
    //     return transactionRepository.findById(id).orElse(null);
    // }
@MutationMapping
public Transaction createTransaction(
        @Argument String clientName,
        @Argument Long staffId,
        @Argument Long serviceId,
        @Argument Double amountPaid,
        @Argument Double percentageGiven,
        @Argument Long percentageRecipientId,
        @Argument String date,
        @Argument String time
) {
    Transaction transaction = new Transaction(
        clientName,
        staffId,
        serviceId,
        amountPaid,
        percentageGiven,
        percentageRecipientId,
        date,
        time
    );

    try {
        return transactionRepository.save(transaction);
    } catch (Exception e) {
        System.out.println("Error saving transaction: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}
    @QueryMapping
    public Double getTotalTransactionsAmount() {
        return transactionService.getTotalAmount();
    }

}
