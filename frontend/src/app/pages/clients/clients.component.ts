import { Component, OnInit } from '@angular/core';
import { Apollo } from 'apollo-angular';
import gql from 'graphql-tag';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { KatiaWorkComponent } from '../katia-work/katia-work.component';
import i18next from 'i18next';

@Component({
  selector: 'app-clients',
  standalone: true,
  imports: [
    NavbarComponent,
    CommonModule,
    FormsModule,
    KatiaWorkComponent
  ],
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css']
})
export class ClientsComponent implements OnInit {
  lastWeekTotal: number = 0;
  lastMonthTotal: number = 0;
  lastYearTotal: number = 0;
deletingRowId: string | null = null;
confirmDeleteRowId: string | null = null;
deleteMode = false;

  searchTerm: string = '';
  page = 1;
  pageSize = 10;
  Math = Math;

  transactions: any[] = [];
  services: any[] = [];
  staff: any[] = [];

  newTransaction = {
    clientName: '',
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
    this.loadStaffServices();
    this.loadStaff();
    this.loadTransactionSums();
  }

toggleDeleteMode() {
  this.deleteMode = !this.deleteMode;
}
  startDeleteTransaction(id: string) {
  // start shaking animation
  this.deletingRowId = id;

  // after animation ends, show X icon for confirmation
  setTimeout(() => {
    this.confirmDeleteRowId = id;
    this.deletingRowId = null;
  }, 500); // 0.5s shaking animation
}

confirmDeleteTransaction(id: string) {
  const DELETE_TRANSACTION = gql`
    mutation DeleteTransaction($id: ID!) {
      deleteTransaction(id: $id)
    }
  `;

  this.apollo.mutate({
    mutation: DELETE_TRANSACTION,
    variables: { id }
  }).subscribe({
    next: (res: any) => {
      if (res.data.deleteTransaction) {
        this.transactions = this.transactions.filter(tx => tx.id !== id);
        this.confirmDeleteRowId = null;
      }
    },
    error: (err) => {
      console.error("Error deleting transaction:", err);
    }
  });
}

get maxPage() {
  return Math.max(1, Math.ceil(this.filteredTransactions.length / this.pageSize));
}
get paginatedTransactions() {
  const start = (this.page - 1) * this.pageSize;
  const end = start + this.pageSize;
  return this.filteredTransactions.slice(start, end);
}

  getTranslation(key: string) {
    return i18next.t(key);
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
    if (!this.searchTerm) return this.transactions;
    return this.transactions.filter(tx =>
      tx.clientName?.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }

  private formatDateTime(): string {
    const date = new Date(this.newTransaction.date);
    return date.toISOString().split("T")[0];
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
      this.transactions = [...this.transactions, { ...result.data.createTransaction }];
    });

    this.newTransaction = {
      clientName: '',
      staffId: this.getLoggedInStaffId() || '',
      serviceId: '',
      amountPaid: 0,
      percentageGiven: 0,
      percentageRecipientId: '',
      date: '',
      time: ''
    };
  }

  loadTransactionSums() {
    const staffId = this.getLoggedInStaffId();
    if (!staffId) return;

    const GET_SUMS = gql`
      query GetTransactionSums($staffId: ID!) {
        getSumLastWeek(staffId: $staffId)
        getSumLastMonth(staffId: $staffId)
        getSumLastYear(staffId: $staffId)
      }
    `;

    this.apollo.watchQuery({
      query: GET_SUMS,
      variables: { staffId }
    }).valueChanges.subscribe((res: any) => {
      this.lastWeekTotal = res.data.getSumLastWeek;
      this.lastMonthTotal = res.data.getSumLastMonth;
      this.lastYearTotal = res.data.getSumLastYear;
    });
  }

  loadTransactions() {
    const staffId = this.getLoggedInStaffId();
    if (!staffId) return;

    const GET_ALL = gql`
      query AllTransactions($staffId: ID!) {
        allTransactions(staffId: $staffId) {
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

    this.apollo.watchQuery({
      query: GET_ALL,
      variables: { staffId }
    }).valueChanges.subscribe((res: any) => {
      this.transactions = res.data.allTransactions;
    });
  }

  getLoggedInStaffName(): string | null {
    const staffStr = localStorage.getItem('staff');
    if (staffStr) {
      const staff = JSON.parse(staffStr);
      return staff.name;
    }
    return null;
  }
prevPage() {
  if (this.page > 1) this.page--;
}

nextPage() {
  if (this.page < this.maxPage) this.page++;
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
deleteTransaction(id: string) {
  const DELETE_TRANSACTION = gql`
    mutation DeleteTransaction($id: ID!) {
      deleteTransaction(id: $id)
    }
  `;

  this.apollo.mutate({
    mutation: DELETE_TRANSACTION,
    variables: { id }
  }).subscribe({
    next: (res: any) => {
      if (res.data.deleteTransaction) {
        // remove transaction from local list
        this.transactions = this.transactions.filter(tx => tx.id !== id);
      }
    },
    error: (err) => {
      console.error("Error deleting transaction:", err);
    }
  });
}

  onServiceChange() {
    const selectedService = this.services.find(s => s.id === this.newTransaction.serviceId);
    if (selectedService) {
      this.newTransaction.amountPaid = selectedService.basePrice;
    }
  }

  getStaffNameById(id: string) {
    const staff = this.staff.find(s => s.id === id);
    return staff ? staff.name : id;
  }

  getServiceNameById(id: string) {
    const service = this.services.find(s => s.id === id);
    return service ? service.name : id;
  }
}
