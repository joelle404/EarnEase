import { Component } from '@angular/core';
import { Apollo } from 'apollo-angular';
import gql from 'graphql-tag';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-clients',
  standalone: true, // ✅ Needed for standalone component
  imports: [
    NavbarComponent,
    CommonModule,
    FormsModule,
  ],
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css'] // ✅ Must be plural
})
export class ClientsComponent {
  transactions: any[] = [];

  newTransaction = {
    clientId: '',
    staffId: '',
    serviceId: '',
    amountPaid: 0,
    percentageGiven: 0,
    percentageRecipientId: '',
    date: '',
    time: ''
  };

  constructor(private apollo: Apollo) {}

  ngOnInit() {
    this.loadTransactions();
  }

  private formatDateTime(): string {
    return `${this.newTransaction.date}T${this.newTransaction.time}:00`;
  }

  addTransaction() {
    const CREATE_TRANSACTION = gql`
      mutation CreateTransaction(
        $clientId: ID!,
        $staffId: ID!,
        $serviceId: ID!,
        $amountPaid: Float!,
        $percentageGiven: Float!,
        $percentageRecipientId: ID!,
        $date: String!
      ) {
        createTransaction(
          clientId: $clientId,
          staffId: $staffId,
          serviceId: $serviceId,
          amountPaid: $amountPaid,
          percentageGiven: $percentageGiven,
          percentageRecipientId: $percentageRecipientId,
          date: $date
        ) {
          id
          client { name }
          staff { name }
          service { name }
          amountPaid
          percentageGiven
          percentageRecipient { name }
          date
        }
      }
    `;

    this.apollo.mutate({
      mutation: CREATE_TRANSACTION,
      variables: {
        ...this.newTransaction,
        date: this.formatDateTime()
      }
    }).subscribe((result: any) => {
      this.transactions.push(result.data.createTransaction);
    });
  }

  loadTransactions() {
    const GET_ALL = gql`
      {
        allTransactions {
          id
          client { name }
          service { name }
          amountPaid
          percentageGiven
          percentageRecipient { name }
          date
        }
      }
    `;

    this.apollo.watchQuery({ query: GET_ALL }).valueChanges.subscribe((res: any) => {
      this.transactions = res.data.allTransactions;
    });
  }
}
