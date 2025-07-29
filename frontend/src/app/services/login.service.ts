import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private GRAPHQL_URL = 'http://localhost:8080/graphql';

  constructor(private http: HttpClient) {}

  login(email: string, password: string) {
    const query = {
      query: `
        mutation Login($email: String!, $password: String!) {
          login(email: $email, password: $password) {
            token
            staff {
              id
              name
              role
              email
            }
          }
        }
      `,
      variables: {
        email,
        password
      }
    };

    return this.http.post<any>(this.GRAPHQL_URL, query).pipe(
      map(res => res.data.login)
    );
  }
}
