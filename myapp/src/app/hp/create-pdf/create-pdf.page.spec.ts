import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { CreatePDFPage } from './create-pdf.page';

describe('CreatePDFPage', () => {
  let component: CreatePDFPage;
  let fixture: ComponentFixture<CreatePDFPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(CreatePDFPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
