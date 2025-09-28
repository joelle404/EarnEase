import { Component, OnInit } from '@angular/core';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { Apollo, gql } from 'apollo-angular';
import { CommonModule } from '@angular/common';
import i18next from 'i18next';

@Component({
  selector: 'app-totalincome',
  standalone: true,
  imports: [NgxChartsModule, CommonModule],
  templateUrl: './totalincome.component.html',
  styleUrls: ['./totalincome.component.css']
})
export class TotalincomeComponent implements OnInit {
  staffId = this.getLoggedInStaffId();
  chartData: any[] = [];
  totalIncome: number = 0;

  view: [number, number] = [400, 400];
  gradient = false;
  showLegend = true;
  showLabels = true;
  explodeSlices = false;
  doughnut = true;

  constructor(private apollo: Apollo) {}

  ngOnInit(): void {
    this.loadChartData();
  }

  private getLoggedInStaffId(): string | null {
    const staffStr = localStorage.getItem('staff');
    if (staffStr) {
      const staff = JSON.parse(staffStr);
      return staff.id;
    }
    return null;
  }

  getTranslation(key: string): string {
    return i18next.t(key);
  }

  async loadChartData() {
    const LAST_WEEK_QUERY = gql`
      query GetSumLastWeek($staffId: ID!) {
        getSumLastWeek(staffId: $staffId)
      }
    `;
    const LAST_MONTH_QUERY = gql`
      query GetSumLastMonth($staffId: ID!) {
        getSumLastMonth(staffId: $staffId)
      }
    `;
    const LAST_YEAR_QUERY = gql`
      query GetSumLastYear($staffId: ID!) {
        getSumLastYear(staffId: $staffId)
      }
    `;

    try {
      const [weekResult, monthResult, yearResult] = await Promise.all([
        this.apollo.query<{ getSumLastWeek: number }>({
          query: LAST_WEEK_QUERY,
          variables: { staffId: this.staffId }
        }).toPromise(),

        this.apollo.query<{ getSumLastMonth: number }>({
          query: LAST_MONTH_QUERY,
          variables: { staffId: this.staffId }
        }).toPromise(),

        this.apollo.query<{ getSumLastYear: number }>({
          query: LAST_YEAR_QUERY,
          variables: { staffId: this.staffId }
        }).toPromise()
      ]);

      const weekIncome = weekResult?.data?.getSumLastWeek ?? 0;
      const monthIncome = monthResult?.data?.getSumLastMonth ?? 0;
      const yearIncome = yearResult?.data?.getSumLastYear ?? 0;

      this.totalIncome = weekIncome + monthIncome + yearIncome;

      this.chartData = [
        { name: this.getTranslation('charts.lastWeek'), value: weekIncome },
        { name: this.getTranslation('charts.lastMonth'), value: monthIncome },
        { name: this.getTranslation('charts.lastYear'), value: yearIncome }
      ];
    } catch (error) {
      console.error('Error loading chart data:', error);
    }
  }
}
