import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ContactdocPage } from './contactdoc.page';

describe('ContactdocPage', () => {
  let component: ContactdocPage;
  let fixture: ComponentFixture<ContactdocPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ContactdocPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
