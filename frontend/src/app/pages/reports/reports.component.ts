import { Component, OnInit } from '@angular/core';
import { Apollo, gql } from 'apollo-angular';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { CommonModule } from '@angular/common';
import { RentComponent } from '../rent/rent.component';
import i18next from 'i18next';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [NavbarComponent, CommonModule, RentComponent],
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css']
})
export class ReportsComponent implements OnInit {
  staffId =  this.getLoggedInStaffId(); // testing
  period: 'week' | 'month' | 'year' = 'month';

  givenData: any[] = [];
  receivedData: any[] = [];
  loading = false;

  // Totals
  profit = 0;
  income = 0;
  expenses = 0;

  constructor(private apollo: Apollo) {}

  ngOnInit(): void {
    this.loadReports();
  }
    private getLoggedInStaffId(): string | null {
    const staffStr = localStorage.getItem('staff');
    if (staffStr) {
      const staff = JSON.parse(staffStr);
      return staff.id;
    }
    return null;
  }

  getLoggedInStaffName(): string | null {
    const staffStr = localStorage.getItem('staff');
    if (staffStr) {
      const staff = JSON.parse(staffStr);
      return staff.name;
    }
    return null;
  }
  loadReports() {
    this.loading = true;

    let givenQuery = '';
    let receivedQuery = '';
    let profitQuery = '';
    let incomeQuery = '';
    let expensesQuery = '';

    if (this.period === 'week') {
      givenQuery = 'getGivenLastWeek';
      receivedQuery = 'getReceivedLastWeek';
      profitQuery = 'getPureProfitLastWeek';
      incomeQuery = 'getSumLastWeek';
      expensesQuery = 'getSumPurchasesLastWeek';
    } else if (this.period === 'month') {
      givenQuery = 'getGivenLastMonth';
      receivedQuery = 'getReceivedLastMonth';
      profitQuery = 'getPureProfitLastMonth';
      incomeQuery = 'getSumLastMonth';
      expensesQuery = 'getSumPurchasesLastMonth';
    } else {
      givenQuery = 'getGivenLastYear';
      receivedQuery = 'getReceivedLastYear';
      profitQuery = 'getPureProfitLastYear';
      incomeQuery = 'getSumLastYear';
      expensesQuery = 'getSumPurchasesLastYear';
    }

    const QUERY = gql`
      query ($staffId: ID!) {
        given: ${givenQuery}(staffId: $staffId) {
          staffName
          value
        }
        received: ${receivedQuery}(staffId: $staffId) {
          staffName
          value
        }
        profit: ${profitQuery}(staffId: $staffId)
        income: ${incomeQuery}(staffId: $staffId)
        expenses: ${expensesQuery}(staffId: $staffId)
      }
    `;

    this.apollo.watchQuery<any>({
      query: QUERY,
      variables: { staffId: this.staffId }
    }).valueChanges.subscribe(({ data, loading }) => {
      this.loading = loading;
      if (data) {
        this.givenData = data.given || [];
        this.receivedData = data.received || [];
        this.profit = data.profit || 0;
        this.income = data.income || 0;
        this.expenses = data.expenses || 0;
      }
    });
  }

  getTranslation(key: string) {
    return i18next.t(key);
  }
  changePeriod(period: 'week' | 'month' | 'year') {
    this.period = period;
    this.loadReports();
  }
}
