import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { CardTestePage } from './card-teste.page';

describe('CardTestePage', () => {
  let component: CardTestePage;
  let fixture: ComponentFixture<CardTestePage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(CardTestePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
