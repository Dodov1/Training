import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrainingCreatorComponent } from './training-creator.component';

describe('TrainingCreatorComponent', () => {
  let component: TrainingCreatorComponent;
  let fixture: ComponentFixture<TrainingCreatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TrainingCreatorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TrainingCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
