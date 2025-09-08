import { Component, OnInit } from '@angular/core';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { Apollo, gql } from 'apollo-angular';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-totalpureprofit',
  standalone: true,
  imports: [NgxChartsModule, CommonModule],
  templateUrl: './totalpureprofit.component.html',
  styleUrls: ['./totalpureprofit.component.css']
})
export class TotalpureprofitComponent implements OnInit {
  staffId = this.getLoggedInStaffId(); // Replace with dynamic staff ID
  chartData: any[] = [];
  totalPureProfit: number = 0;

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

  async loadChartData() {
    const LAST_WEEK_QUERY = gql`
      query GetPureProfitLastWeek($staffId: ID!) {
        getPureProfitLastWeek(staffId: $staffId)
      }
    `;
    const LAST_MONTH_QUERY = gql`
      query GetPureProfitLastMonth($staffId: ID!) {
        getPureProfitLastMonth(staffId: $staffId)
      }
    `;
    const LAST_YEAR_QUERY = gql`
      query GetPureProfitLastYear($staffId: ID!) {
        getPureProfitLastYear(staffId: $staffId)
      }
    `;

    try {
      const [weekResult, monthResult, yearResult] = await Promise.all([
        this.apollo.query<{ getPureProfitLastWeek: number }>({
          query: LAST_WEEK_QUERY,
          variables: { staffId: this.staffId }
        }).toPromise(),

        this.apollo.query<{ getPureProfitLastMonth: number }>({
          query: LAST_MONTH_QUERY,
          variables: { staffId: this.staffId }
        }).toPromise(),

        this.apollo.query<{ getPureProfitLastYear: number }>({
          query: LAST_YEAR_QUERY,
          variables: { staffId: this.staffId }
        }).toPromise()
      ]);

      const weekProfit = weekResult?.data?.getPureProfitLastWeek ?? 0;
      const monthProfit = monthResult?.data?.getPureProfitLastMonth ?? 0;
      const yearProfit = yearResult?.data?.getPureProfitLastYear ?? 0;

      this.totalPureProfit = weekProfit + monthProfit + yearProfit;

      this.chartData = [
        { name: 'Last Week', value: weekProfit },
        { name: 'Last Month', value: monthProfit },
        { name: 'Last Year', value: yearProfit }
      ];
    } catch (error) {
      console.error('Error loading pure profit data:', error);
    }
  }
}
