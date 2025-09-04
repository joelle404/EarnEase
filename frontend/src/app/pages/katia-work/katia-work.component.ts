// katia-work.component.ts
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Apollo, gql } from 'apollo-angular';

@Component({
  selector: 'app-katia-work',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './katia-work.component.html',
  styleUrls: ['./katia-work.component.css'],
  encapsulation: ViewEncapsulation.None, 
})
export class KatiaWorkComponent implements OnInit {

  works: any[] = [];

  newWork = {
    clientName: '',
    serviceDate: '',
    grossAmount: 0,
    tamerPercent: 20
  };

  constructor(private apollo: Apollo) {}

  ngOnInit() {
    this.loadWorks();
  }

  // Fetch all Katia works
  loadWorks() {
    this.apollo.watchQuery<any>({
      query: gql`
        query {
          getAllKatiaWork {
            id
            clientName
            serviceDate
            grossAmount
            dimaCut
            tamerPercent
            tamerCut
            katiaNet
          }
        }
      `
    }).valueChanges.subscribe((res: any) => {
      this.works = res.data.getAllKatiaWork;
    });
  }

  // Add a new Katia work
  addWork() {
    this.apollo.mutate({
      mutation: gql`
        mutation($input: KatiaWorkInput!) {
          createKatiaWork(input: $input) {
            id
            clientName
            serviceDate
            grossAmount
            dimaCut
            tamerPercent
            tamerCut
            katiaNet
          }
        }
      `,
      variables: { input: this.newWork }
    }).subscribe(() => {
      this.loadWorks(); // reload table
      // reset form
      this.newWork = { clientName: '', serviceDate: '', grossAmount: 0, tamerPercent: 20 };
    });
  }
}
