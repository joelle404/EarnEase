import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Apollo, gql } from 'apollo-angular';
import i18next from 'i18next';

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
    tamerPercent: 0
  };

  constructor(private apollo: Apollo) {}

  ngOnInit() {
    this.loadWorks();
  }

  deleteMode = false;

  toggleDeleteMode() {
    this.deleteMode = !this.deleteMode;
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
        }
      }
    `,
    variables: { input: this.newWork },
    refetchQueries: [
      {
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
      }
    ]
  }).subscribe(() => {
    // reset form only
    this.newWork = { clientName: '', serviceDate: '', grossAmount: 0, tamerPercent: 0 };
  });
}

// Delete a Katia work
deleteWork(id: string) {
  const DELETE_WORK = gql`
    mutation DeleteKatiaWork($id: ID!) {
      deleteKatiaWork(id: $id)
    }
  `;

  this.apollo.mutate({
    mutation: DELETE_WORK,
    variables: { id },
    refetchQueries: [
      {
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
      }
    ]
  }).subscribe();
}

  getTranslation(key: string) {
    return i18next.t(key);
  }
}
