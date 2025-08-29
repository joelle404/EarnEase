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
  searchTerm: string = '';
  page = 1;
pageSize = 10; // rows per page
  Math = Math;
  transactions: any[] = [];
  services: any[] = []; // services for the logged-in staff
  staff: any[] = [];
  newTransaction = {
    clientName: '',
    staffId: '',   // auto-filled from localStorage
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
    this.loadStaff();
  }

  private getLoggedInStaffId(): string | null {
    const staffStr = localStorage.getItem('staff');
    if (staffStr) {
      const staff = JSON.parse(staffStr);
      return staff.id;
    }
    return null;
  }
get filteredTransactions() {
  if (!this.searchTerm) {
    return this.transactions;
  }
  return this.transactions.filter(tx =>
    tx.clientName?.toLowerCase().includes(this.searchTerm.toLowerCase())
  );
}
  private formatDateTime(): string {
    return `${this.newTransaction.date}T${this.newTransaction.time}:00`;
  }

  addTransaction() {
    const CREATE_TRANSACTION = gql`
      mutation CreateTransaction(
        $clientName: String!,
        $staffId: ID!,
        $serviceId: ID!,
        $amountPaid: Float!,
        $percentageGiven: Float!,
        $percentageRecipientId: ID,
        $date: String!,
        $time: String!
      ) {
        createTransaction(
          clientName: $clientName,
          staffId: $staffId,
          serviceId: $serviceId,
          amountPaid: $amountPaid,
          percentageGiven: $percentageGiven,
          percentageRecipientId: $percentageRecipientId,
          date: $date,
          time: $time
        ) {
          id
          clientName
          staffId
          serviceId
          amountPaid
          percentageGiven
          percentageRecipientId
          date
          time
        }
      }
    `;

    // Attach staffId automatically
    this.newTransaction.staffId = this.getLoggedInStaffId() || '';
const recipientId = this.newTransaction.percentageRecipientId || null;

this.apollo.mutate({
  mutation: CREATE_TRANSACTION,
  variables: {
    clientName: this.newTransaction.clientName,
    staffId: this.newTransaction.staffId,
    serviceId: this.newTransaction.serviceId,
    amountPaid: this.newTransaction.amountPaid,
    percentageGiven: this.newTransaction.percentageGiven,
    percentageRecipientId: recipientId,
    date: this.formatDateTime(),
    time: this.newTransaction.time
  }
}).subscribe((result: any) => {
  // Make a **copy** before pushing, because Apollo objects are sometimes frozen
  this.transactions = [...this.transactions, { ...result.data.createTransaction }];
});

    this.newTransaction = {
      clientName: '',
      staffId: this.getLoggedInStaffId() || '', // keep logged-in staff
      serviceId: '',
      amountPaid: 0,
      percentageGiven: 0,
      percentageRecipientId: '',
      date: '',
      time: ''
    };

  }

  loadTransactions() {
    const GET_ALL = gql`
      {
        allTransactions {
          id
          clientName
          staffId
          serviceId
          amountPaid
          percentageGiven
          percentageRecipientId
          date
          time
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

  loadStaff() {
    const GET_STAFF = gql`
      query {
        allStaff {
          id
          name
        }
      }
    `;

    this.apollo.watchQuery({ query: GET_STAFF }).valueChanges.subscribe((res: any) => {
      this.staff = res.data.allStaff;
    });
  }

  onServiceChange() {
    const selectedService = this.services.find(
      s => s.id === this.newTransaction.serviceId
    );
    if (selectedService) {
      this.newTransaction.amountPaid = selectedService.basePrice;
    }
  }

  // Optional: helper to display names instead of IDs
  getStaffNameById(id: string) {
    const staff = this.staff.find(s => s.id === id);
    return staff ? staff.name : id;
  }

  getServiceNameById(id: string) {
    const service = this.services.find(s => s.id === id);
    return service ? service.name : id;
  }
}
