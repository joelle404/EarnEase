import { ApplicationConfig, inject, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { MatTabsModule } from '@angular/material/tabs';
import { provideAnimations } from '@angular/platform-browser/animations';

import { provideApollo } from 'apollo-angular';
import { InMemoryCache } from '@apollo/client/core';
import { HttpLink } from 'apollo-angular/http';
import { setContext } from '@apollo/client/link/context';

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    importProvidersFrom(HttpClientModule, MatTabsModule),
    provideRouter(routes),
    provideAnimations(),

    provideApollo(() => {
      const httpLink = inject(HttpLink);

      // Auth link to attach JWT token to headers
      const authLink = setContext((operation, context) => {
        const token = localStorage.getItem('token');
        return {
          headers: {
            ...context['headers'],
            Authorization: token ? `Bearer ${token}` : ''
          }
        };
      });

      return {
        cache: new InMemoryCache(),
        link: authLink.concat(
          httpLink.create({ uri: 'http://localhost:8080/graphql' })
        )
      };
    })
  ],
};
