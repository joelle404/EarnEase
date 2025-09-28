import { Component, OnInit } from '@angular/core';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { Apollo, gql } from 'apollo-angular';
import i18next from 'i18next';

@Component({
  selector: 'app-monthly-profit',
  standalone: true,
  imports: [NgxChartsModule],
  templateUrl: './monthly-profit.component.html',
  styleUrls: ['./monthly-profit.component.css']
})
export class MonthlyProfitComponent implements OnInit {
  staffId = 5; // Replace with dynamic staff ID
  chartData: any[] = [];

  // Chart options
  view: [number, number] = [600, 400];
  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showLegend = true;
  showXAxisLabel = true;
  showYAxisLabel = true;
  xAxisLabel = this.getTranslation('charts.month');
  yAxisLabel = this.getTranslation('charts.amount');
  autoScale = true;

  constructor(private apollo: Apollo) {}

  ngOnInit(): void {
    this.loadChartData();
  }

  getTranslation(key: string): string {
    return i18next.t(key);
  }

  async loadChartData() {
    const MONTHLY_INCOME_QUERY = gql`
      query GetMonthlyIncome($staffId: ID!) {
        getMonthlyIncome(staffId: $staffId) {
          month
          profit
        }
      }
    `;

    const MONTHLY_EXPENSES_QUERY = gql`
      query GetMonthlyExpenses($staffId: ID!) {
        getMonthlyExpenses(staffId: $staffId) {
          month
          amount
        }
      }
    `;

    try {
      const [incomeResult, expensesResult] = await Promise.all([
        this.apollo.query<{ getMonthlyIncome: { month: string; profit: number }[] }>({
          query: MONTHLY_INCOME_QUERY,
          variables: { staffId: this.staffId }
        }).toPromise(),

        this.apollo.query<{ getMonthlyExpenses: { month: string; amount: number }[] }>({
          query: MONTHLY_EXPENSES_QUERY,
          variables: { staffId: this.staffId }
        }).toPromise()
      ]);

      const incomeData = incomeResult?.data?.getMonthlyIncome ?? [];
      const expensesData = expensesResult?.data?.getMonthlyExpenses ?? [];

      const profitData = incomeData.map(i => {
        const matchingExpense = expensesData.find(e => e.month === i.month);
        const expenseAmount = matchingExpense?.amount ?? 0;
        return { month: i.month, profit: i.profit - expenseAmount };
      });

      this.chartData = [
        {
          name: this.getTranslation('charts.income'),
          series: incomeData.map(i => ({ name: i.month, value: i.profit }))
        },
        {
          name: this.getTranslation('charts.expenses'),
          series: expensesData.map(e => ({ name: e.month, value: e.amount }))
        },
        {
          name: this.getTranslation('charts.profit'),
          series: profitData.map(p => ({ name: p.month, value: p.profit }))
        }
      ];
    } catch (error) {
      console.error('Error loading monthly profit data:', error);
    }
  }
}
