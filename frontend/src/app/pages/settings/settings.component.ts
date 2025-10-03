import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { FormsModule } from '@angular/forms';
import i18next from 'i18next';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [NavbarComponent, FormsModule],
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  currentLang: string = 'en';

  constructor(private router: Router) {}

  ngOnInit() {
    // استرجاع اللغة من localStorage أو الافتراضية (en)
    const savedLang = localStorage.getItem('lang') || 'en';
    this.currentLang = savedLang;
    i18next.changeLanguage(savedLang);
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/']);
  }

  changeLanguage(lang: string) {
    i18next.changeLanguage(lang);
    this.currentLang = lang;
    localStorage.setItem('lang', lang); // حفظ اللغة في LocalStorage
  }

  getTranslation(key: string) {
    return i18next.t(key);
  }
}
