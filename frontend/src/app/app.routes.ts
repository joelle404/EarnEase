import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { StaffComponent } from './pages/staff/staff.component';
import { StaffPaymentsComponent } from './pages/staff-payments/staff-payments.component';
import { ServicesComponent } from './pages/services/services.component';
import { SettingsComponent } from './pages/settings/settings.component';

export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'staff', component: StaffComponent },
  { path: 'staff-payments', component: StaffPaymentsComponent },
  { path: 'services', component: ServicesComponent },
  { path: 'settings', component: SettingsComponent },
];
