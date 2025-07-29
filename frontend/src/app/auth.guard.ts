import { inject, Injectable } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const token = localStorage.getItem('token');

  const router = inject(Router);

  if (!token) {
    router.navigate(['/']); // redirect to login
    return false;
  }

  return true;
};
