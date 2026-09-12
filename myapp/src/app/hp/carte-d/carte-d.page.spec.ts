import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CarteDPage } from './carte-d.page';

describe('CarteDPage', () => {
  let component: CarteDPage;
  let fixture: ComponentFixture<CarteDPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(CarteDPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
