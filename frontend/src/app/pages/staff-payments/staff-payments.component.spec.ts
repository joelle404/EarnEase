import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffPaymentsComponent } from './staff-payments.component';

describe('StaffPaymentsComponent', () => {
  let component: StaffPaymentsComponent;
  let fixture: ComponentFixture<StaffPaymentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StaffPaymentsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StaffPaymentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
