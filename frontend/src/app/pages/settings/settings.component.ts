import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import i18next from 'i18next';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [NavbarComponent],
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']  // ✅ FIXED here
})
export class SettingsComponent {
  constructor(private router: Router) {}

  logout() {
    localStorage.clear();
    this.router.navigate(['/']);
  }

  changeLanguage(lang: string) {
    i18next.changeLanguage(lang);
  }
}
