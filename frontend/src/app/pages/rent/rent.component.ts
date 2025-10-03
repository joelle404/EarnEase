import { Component, OnInit } from '@angular/core';
import { Apollo, gql } from 'apollo-angular';
import { CommonModule } from '@angular/common';
import i18next from 'i18next';

const GET_ALL_RENTS = gql`
  query GetAllRents($staffId: ID!) {
    getAllRents(staffId: $staffId) {
      id
      month
      amount
      paidDate
    }
  }
`;


@Component({
  selector: 'app-rent',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './rent.component.html',
  styleUrls: ['./rent.component.css']
})
export class RentComponent implements OnInit {
  staffId: string = this.getLoggedInStaffId() || '';
  rents: any[] = [];
  loading = false;
  error: string | null = null;

  constructor(private apollo: Apollo) {}

  ngOnInit(): void {
    this.fetchAllRents();
  }

  private getLoggedInStaffId(): string | null {
    const staffStr = localStorage.getItem('staff');
    if (staffStr) {
      const staff = JSON.parse(staffStr);
      return staff.id;
    }
    return null;
  }

  getTranslation(key: string) {
    return i18next.t(key);
  }

fetchAllRents() {
  this.loading = true;
  this.apollo.watchQuery<any>({
    query: GET_ALL_RENTS,
    variables: { staffId: Number(this.staffId) } // <-- important
  }).valueChanges.subscribe({
    next: ({ data }) => {
      console.log('GraphQL data:', data);
      this.rents = data?.getAllRents || [];
      this.loading = false;
    },
    error: (err) => {
      console.error('GraphQL error:', err);
      this.error = 'Failed to load rent history';
      this.loading = false;
    }
  });
}

}
