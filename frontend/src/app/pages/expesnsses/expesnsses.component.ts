import { Component, OnInit } from '@angular/core';
import { Apollo } from 'apollo-angular';
import gql from 'graphql-tag';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RentComponent } from '../rent/rent.component';

@Component({
  selector: 'app-expesnsses',
  standalone: true,
  imports: [NavbarComponent, CommonModule, FormsModule,RentComponent],
  templateUrl: './expesnsses.component.html',
  styleUrls: ['./expesnsses.component.css']
})
export class ExpesnssesComponent implements OnInit {
  searchTerm: string = '';
  page = 1;
  pageSize = 10;
  Math = Math;

  purchases: any[] = [];
  staff: any[] = [];

  totalLastWeek: number = 0;
  totalLastMonth: number = 0;
  totalLastYear: number = 0;

  newPurchase = {
    staffId: '',
    productName: '',
    amountSpent: 0,
    date: ''
  };

  constructor(private apollo: Apollo) {}

  ngOnInit() {
    this.loadPurchases();
    this.loadStaff();
    this.loadTotals();
    this.newPurchase.staffId = this.getLoggedInStaffId() || '';
  }

  private getLoggedInStaffId(): string | null {
    const staffStr = localStorage.getItem('staff');
    if (staffStr) {
      const staff = JSON.parse(staffStr);
      return staff.id;
    }
    return null;
  }

  get filteredPurchases() {
    if (!this.searchTerm) {
      return this.purchases;
    }
    return this.purchases.filter(p =>
      p.productName?.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }
getLoggedInStaffName(): string | null {
  const staffStr = localStorage.getItem('staff');
  if (staffStr) {
    const staff = JSON.parse(staffStr);
    return staff.name;
  }
  return null;
}

  addPurchase() {
    const CREATE_PURCHASE = gql`
      mutation CreateProductPurchase(
        $staffId: ID!,
        $productName: String!,
        $amountSpent: Float!,
        $date: String!
      ) {
        createProductPurchase(
          staffId: $staffId,
          productName: $productName,
          amountSpent: $amountSpent,
          date: $date
        ) {
          id
          staff {
            id
            name
          }
          productName
          amountSpent
          date
        }
      }
    `;

    this.newPurchase.staffId = this.getLoggedInStaffId() || '';

    this.apollo.mutate({
      mutation: CREATE_PURCHASE,
      variables: {
        staffId: this.newPurchase.staffId,
        productName: this.newPurchase.productName,
        amountSpent: this.newPurchase.amountSpent,
        date: this.formatDateTime()
      }
    }).subscribe({
      next: (result: any) => {
        const newPurchase = result.data?.createProductPurchase;
        if (newPurchase) {
          this.purchases = [...this.purchases, newPurchase];
          this.loadTotals(); // refresh totals after adding
        }
      },
      error: err => console.error('Failed to add purchase', err)
    });

    this.newPurchase = {
      staffId: this.getLoggedInStaffId() || '',
      productName: '',
      amountSpent: 0,
      date: ''
    };
  }

  private formatDateTime(): string {
    const date = new Date(this.newPurchase.date);
    return date.toISOString().split("T")[0];
  }

  loadPurchases() {
    const staffId = this.getLoggedInStaffId();
    if (!staffId) return;

    const GET_PURCHASES = gql`
      query ALLProductPurchases($staffId: ID) {
        allProductPurchases(staffId: $staffId) {
          id
          productName
          amountSpent
          date
          staff {
            id
            name
          }
        }
      }
    `;

    this.apollo.watchQuery<{ allProductPurchases: any[] }>({
      query: GET_PURCHASES,
      variables: { staffId }
    }).valueChanges.subscribe({
      next: result => {
        this.purchases = result.data?.allProductPurchases ?? [];
      },
      error: err => {
        console.error('Failed to load purchases', err);
        this.purchases = [];
      }
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

    this.apollo.watchQuery({ query: GET_STAFF }).valueChanges.subscribe({
      next: (res: any) => {
        this.staff = res.data?.allStaff ?? [];
      },
      error: err => {
        console.error('Failed to load staff', err);
        this.staff = [];
      }
    });
  }

  private loadTotals() {
    const staffId = this.getLoggedInStaffId();
    if (!staffId) return;

    const GET_TOTAL_LAST_WEEK = gql`
      query GetSumLastWeek($staffId: ID!) {
        getSumPurchasesLastWeek(staffId: $staffId)
      }
    `;
    const GET_TOTAL_LAST_MONTH = gql`
      query GetSumLastMonth($staffId: ID!) {
        getSumPurchasesLastMonth(staffId: $staffId)
      }
    `;
    const GET_TOTAL_LAST_YEAR = gql`
      query GetSumLastYear($staffId: ID!) {
        getSumPurchasesLastYear(staffId: $staffId)
      }
    `;

    this.apollo.query({ query: GET_TOTAL_LAST_WEEK, variables: { staffId } }).subscribe((res: any) => {
      this.totalLastWeek = res.data?.getSumPurchasesLastWeek ?? 0;
    });

    this.apollo.query({ query: GET_TOTAL_LAST_MONTH, variables: { staffId } }).subscribe((res: any) => {
      this.totalLastMonth = res.data?.getSumPurchasesLastMonth ?? 0;
    });

    this.apollo.query({ query: GET_TOTAL_LAST_YEAR, variables: { staffId } }).subscribe((res: any) => {
      this.totalLastYear = res.data?.getSumPurchasesLastYear ?? 0;
    });
  }

  getStaffNameById(id: string) {
    const staff = this.staff.find(s => s.id === id);
    return staff ? staff.name : id;
  }
}
