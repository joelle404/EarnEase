import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TotalincomeComponent } from './totalincome.component';

describe('TotalincomeComponent', () => {
  let component: TotalincomeComponent;
  let fixture: ComponentFixture<TotalincomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TotalincomeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TotalincomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
