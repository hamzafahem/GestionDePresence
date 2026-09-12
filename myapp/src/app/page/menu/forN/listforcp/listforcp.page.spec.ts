import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListforcpPage } from './listforcp.page';

describe('ListforcpPage', () => {
  let component: ListforcpPage;
  let fixture: ComponentFixture<ListforcpPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ListforcpPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
