import { Component, OnInit } from '@angular/core';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { Apollo, gql } from 'apollo-angular';
import { CommonModule, registerLocaleData } from '@angular/common';
import localeHe from '@angular/common/locales/he';
import i18next from 'i18next';

registerLocaleData(localeHe, 'he-IL');

@Component({
  selector: 'app-totalspent',
  standalone: true,
  imports: [NgxChartsModule, CommonModule],
  templateUrl: './totalspent.component.html',
  styleUrls: ['./totalspent.component.css']
})
export class TotalspentComponent implements OnInit {
  staffId = this.getLoggedInStaffId();
  chartData: any[] = [];
  totalSpent: number = 0;

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
      query GetSumPurchasesLastWeek($staffId: ID!) {
        getSumPurchasesLastWeek(staffId: $staffId)
      }
    `;
    const LAST_MONTH_QUERY = gql`
      query GetSumPurchasesLastMonth($staffId: ID!) {
        getSumPurchasesLastMonth(staffId: $staffId)
      }
    `;
    const LAST_YEAR_QUERY = gql`
      query GetSumPurchasesLastYear($staffId: ID!) {
        getSumPurchasesLastYear(staffId: $staffId)
      }
    `;

    try {
      const [weekResult, monthResult, yearResult] = await Promise.all([
        this.apollo.query<{ getSumPurchasesLastWeek: number }>({
          query: LAST_WEEK_QUERY,
          variables: { staffId: this.staffId }
        }).toPromise(),

        this.apollo.query<{ getSumPurchasesLastMonth: number }>({
          query: LAST_MONTH_QUERY,
          variables: { staffId: this.staffId }
        }).toPromise(),

        this.apollo.query<{ getSumPurchasesLastYear: number }>({
          query: LAST_YEAR_QUERY,
          variables: { staffId: this.staffId }
        }).toPromise()
      ]);

      const weekSpent = weekResult?.data?.getSumPurchasesLastWeek ?? 0;
      const monthSpent = monthResult?.data?.getSumPurchasesLastMonth ?? 0;
      const yearSpent = yearResult?.data?.getSumPurchasesLastYear ?? 0;

      this.totalSpent = weekSpent + monthSpent + yearSpent;

      this.chartData = [
        { name: this.getTranslation('charts.lastWeek'), value: weekSpent },
        { name: this.getTranslation('charts.lastMonth'), value: monthSpent },
        { name: this.getTranslation('charts.lastYear'), value: yearSpent }
      ];
    } catch (error) {
      console.error('Error loading spent data:', error);
    }
  }
}
