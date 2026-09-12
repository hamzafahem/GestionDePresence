import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ModuleNPage } from './module-n.page';

describe('ModuleNPage', () => {
  let component: ModuleNPage;
  let fixture: ComponentFixture<ModuleNPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ModuleNPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
