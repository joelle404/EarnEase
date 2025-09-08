import { Component, OnInit, EnvironmentInjector, inject } from '@angular/core';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { Apollo, gql } from 'apollo-angular';

@Component({
  selector: 'app-monthly-profit',
standalone: true,
  imports: [NgxChartsModule],
  templateUrl: './monthly-profit.component.html',
  styleUrls: ['./monthly-profit.component.css']
})
export class MonthlyProfitComponent implements OnInit {
    private envInjector = inject(EnvironmentInjector);

  staffId = 5; // Replace with the actual staff ID
  chartData: any[] = [];

  // Chart options
  view: [number, number] = [700, 400];
  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showLegend = true;
  showXAxisLabel = true;
  showYAxisLabel = true;
  xAxisLabel = 'Month';
  yAxisLabel = 'Amount';
  autoScale = true;

  constructor(private apollo: Apollo) {}

  ngOnInit(): void {
    this.loadChartData();
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
          name: 'Income',
          series: incomeData.map(i => ({ name: i.month, value: i.profit }))
        },
        {
          name: 'Expenses',
          series: expensesData.map(e => ({ name: e.month, value: e.amount }))
        },
        {
          name: 'Profit',
          series: profitData.map(p => ({ name: p.month, value: p.profit }))
        }
      ];
    } catch (error) {
      console.error('Error loading chart data:', error);
    }
  }
}
