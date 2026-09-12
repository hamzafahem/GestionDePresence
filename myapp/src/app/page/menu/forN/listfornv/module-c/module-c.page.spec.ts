import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ModuleCPage } from './module-c.page';

describe('ModuleCPage', () => {
  let component: ModuleCPage;
  let fixture: ComponentFixture<ModuleCPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ModuleCPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
