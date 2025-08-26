import { Component, OnInit } from '@angular/core';
import { Apollo } from 'apollo-angular';
import gql from 'graphql-tag';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-clients',
  standalone: true,
  imports: [
    NavbarComponent,
    CommonModule,
    FormsModule,
  ],
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css']
})
export class ClientsComponent implements OnInit {
  transactions: any[] = [];
  services: any[] = []; // ✅ services for the logged-in staff

  newTransaction = {
    clientId: '',
    staffId: '',   // will be set from localStorage
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
    this.loadStaffServices();
  }

  private getLoggedInStaffId(): string | null {
    const staffStr = localStorage.getItem('staff');
    if (staffStr) {
      const staff = JSON.parse(staffStr);
      return staff.id;
    }
    return null;
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

    // ✅ Attach staffId automatically
    this.newTransaction.staffId = this.getLoggedInStaffId() || '';

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
          staff { name }
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

  loadStaffServices() {
    const staffId = this.getLoggedInStaffId();
    if (!staffId) return;

    const GET_SERVICES_BY_STAFF = gql`
      query GetServicesByStaffId($staffId: ID!) {
        getServicesByStaffId(staffId: $staffId) {
          id
          name
          basePrice
        }
      }
    `;

    this.apollo.watchQuery({
      query: GET_SERVICES_BY_STAFF,
      variables: { staffId }
    }).valueChanges.subscribe((res: any) => {
      this.services = res.data.getServicesByStaffId;
    });
  }
}