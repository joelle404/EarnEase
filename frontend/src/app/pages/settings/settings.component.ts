import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common'; // <-- Add this
import { Apollo, gql } from 'apollo-angular';
import i18next from 'i18next';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [NavbarComponent, FormsModule, CommonModule], // <-- Add CommonModule here
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  currentLang: string = 'en';
  passwordData = { currentPassword: '', newPassword: '', confirmPassword: '' };
  passwordMessage: string = '';

  constructor(private router: Router, private apollo: Apollo) {}

  ngOnInit() {
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
    localStorage.setItem('lang', lang);
  }

  getTranslation(key: string) {
    return i18next.t(key);
  }

  changePassword() {
    if (this.passwordData.newPassword !== this.passwordData.confirmPassword) {
      this.passwordMessage = this.getTranslation('settings.passwordMismatch');
      return;
    }

    const staff = JSON.parse(localStorage.getItem('staff') || '{}');

    const CHANGE_PASSWORD = gql`
      mutation ChangePassword($staffId: ID!, $currentPassword: String!, $newPassword: String!) {
        changePassword(staffId: $staffId, currentPassword: $currentPassword, newPassword: $newPassword)
      }
    `;

    this.apollo.mutate({
      mutation: CHANGE_PASSWORD,
      variables: {
        staffId: staff.id,
        currentPassword: this.passwordData.currentPassword,
        newPassword: this.passwordData.newPassword
      }
    }).subscribe({
      next: (res: any) => {
        if (res.data.changePassword) {
          this.passwordMessage = this.getTranslation('settings.passwordChanged');
          this.passwordData = { currentPassword: '', newPassword: '', confirmPassword: '' };
        } else {
          this.passwordMessage = this.getTranslation('settings.wrongCurrentPassword');
        }
      },
      error: () => {
        this.passwordMessage = this.getTranslation('settings.errorChangingPassword');
      }
    });
  }
}
