import { Component, OnInit } from '@angular/core';
import { Apollo } from 'apollo-angular';
import gql from 'graphql-tag';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-expesnsses',
  standalone: true,
  imports: [NavbarComponent, CommonModule, FormsModule],
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
    }).subscribe((result: any) => {
      this.purchases = [...this.purchases, { ...result.data.createProductPurchase }];
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
      query ALLProductPurchases($staffId: ID){
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

    this.apollo.watchQuery({ query: GET_PURCHASES }).valueChanges.subscribe((res: any) => {
      this.purchases = res.data.allProductPurchases;
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

  getStaffNameById(id: string) {
    const staff = this.staff.find(s => s.id === id);
    return staff ? staff.name : id;
  }
}
