import { Component, OnInit } from '@angular/core';
import { Apollo, gql } from 'apollo-angular';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

const GET_RENT = gql`
  query GetRent($staffId: ID!, $month: String!) {
    getRent(staffId: $staffId, month: $month) {
      id
      staffId
      month
      amount
      paidDate
    }
  }
`;

const UPDATE_RENT = gql`
  mutation UpdateRent($id: ID!, $amount: Float!) {
    updateRent(id: $id, amount: $amount) {
      id
      amount
      paidDate
    }
  }
`;

@Component({
  selector: 'app-rent',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './rent.component.html',
  styleUrls: ['./rent.component.css']
})
export class RentComponent implements OnInit {
  staffId: string = this.getLoggedInStaffId() || ''; // example staff
  month: string = new Date().toISOString().slice(0, 7); // yyyy-MM
  rent: any;
  name: String =this.getLoggedInStaffname() || '';
  newAmount: number = 3000; // default rent
  loading = false;
  error: string | null = null;

  constructor(private apollo: Apollo) {}

  ngOnInit(): void {
    this.fetchRent();
  }
  private getLoggedInStaffId(): string | null {
    const staffStr = localStorage.getItem('staff');
    if (staffStr) {
      const staff = JSON.parse(staffStr);
      return staff.id;
    }
    return null;
  }
    private getLoggedInStaffname(): string | null {
    const staffStr = localStorage.getItem('staff');
    if (staffStr) {
      const staff = JSON.parse(staffStr);
      return staff.name;
    }
    return null;
  }
  fetchRent() {
    this.loading = true;
    this.apollo
      .watchQuery({
        query: GET_RENT,
        variables: { staffId: this.staffId, month: this.month }
      })
      .valueChanges.subscribe({
        next: (res: any) => {
          const data = res.data?.getRent;
          if (data) {
            this.rent = data;
            this.newAmount = data.amount;
          } else {
            // No rent record, use default
            this.rent = { amount: 3000, paidDate: null, id: null };
            this.newAmount = 3000;
          }
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Failed to fetch rent';
          this.loading = false;
        }
      });
  }

  updateRent() {
    if (!this.rent || !this.newAmount) return;

    // If rent record exists, update
    if (this.rent.id) {
      this.apollo
        .mutate({
          mutation: UPDATE_RENT,
          variables: { id: this.rent.id, amount: this.newAmount }
        })
        .subscribe({
          next: (res: any) => {
            this.rent.amount = res.data.updateRent.amount;
            this.rent.paidDate = res.data.updateRent.paidDate;
            alert('Rent updated successfully!');
          },
          error: () => alert('Failed to update rent')
        });
    } else {
      // Optionally, call createRent mutation if you implement it in backend
      alert(`Default rent is ${this.newAmount}. Save button can create a new rent record.`);
    }
  }
}
