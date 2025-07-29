import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpesnssesComponent } from './expesnsses.component';

describe('ExpesnssesComponent', () => {
  let component: ExpesnssesComponent;
  let fixture: ComponentFixture<ExpesnssesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExpesnssesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExpesnssesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
