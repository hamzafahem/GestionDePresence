import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListfornvPage } from './listfornv.page';

describe('ListfornvPage', () => {
  let component: ListfornvPage;
  let fixture: ComponentFixture<ListfornvPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ListfornvPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
