import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TotalpureprofitComponent } from './totalpureprofit.component';

describe('TotalpureprofitComponent', () => {
  let component: TotalpureprofitComponent;
  let fixture: ComponentFixture<TotalpureprofitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TotalpureprofitComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TotalpureprofitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
