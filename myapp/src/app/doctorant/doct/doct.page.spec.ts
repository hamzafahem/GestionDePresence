
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DoctPage } from '../doct/doct.page';
import { IonFabList } from '@ionic/angular';
// dert IonF blast page module****
describe('DoctPage', () => {
  let component: DoctPage;
  let fixture: ComponentFixture<DoctPage>;

  beforeEach(async(() => {
    fixture = TestBed.createComponent(DoctPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
