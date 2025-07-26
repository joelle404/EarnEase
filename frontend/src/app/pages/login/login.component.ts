import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email = '';
  password = '';
  errorMessage = '';

  constructor(private router: Router) {}

  login() {
    if (this.email === 'admin@example.com' && this.password === 'admin123') {
      this.router.navigate(['/dashboard']);
    } else {
      this.errorMessage = 'Invalid credentials';
    }
  }
}
