import { Component } from '@angular/core';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { MonthlyProfitComponent } from '../monthly-profit/monthly-profit.component';
import { CommonModule } from '@angular/common';
import { TotalspentComponent } from '../../components/totalspent/totalspent.component';
import { TotalincomeComponent } from '../../components/totalincome/totalincome.component';
import { TotalpureprofitComponent } from '../../components/totalpureprofit/totalpureprofit.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,        // always include
    NavbarComponent,
    MonthlyProfitComponent,
    TotalspentComponent,
    TotalincomeComponent,
    TotalpureprofitComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {}
