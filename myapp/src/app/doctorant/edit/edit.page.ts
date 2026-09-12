
// import { Component, OnInit } from '@angular/core';
// import { FormBuilder, FormGroup } from '@angular/forms';
// import { ActivatedRoute, Router } from '@angular/router';

// import { AdminService } from '../../shared/admin.service';


// @Component({
//   selector: 'app-edit',
//   templateUrl: './edit.page.html',
//   styleUrls: ['./edit.page.scss'],
// })
// export class EditPage implements OnInit {
//   // updatedocfor ?: FormGroup;
//   updatedocfor: FormGroup<any> | undefined
//   ID:any;
//   constructor(
//     private adminservice: AdminService,
//   private router:Router,
//   private fb :FormBuilder ,
//   private actroute:ActivatedRoute
//   ) {
//     this.ID =this.actroute.snapshot.paramMap.get('ID')
//     this.adminservice.getdoc(this.ID).valueChanges().Subscribe((res: any)=>{
//       this.updatedocfor.setValue(res)
//     })
//    }
//   id(id: any) {
//     throw new Error('Method not implemented.');
//   }

//   ngOnInit() {
//     this.updatedocform =this.fb.group({
//         $key: [''],
//         name: [''],
//         email:[''],
//         mobile:[''],
//         details:[''],

//     })
//     console.log(this.updatedocform.value )
//   }

//   updateform(){

//      this.adminservice.Updatedoc(this.id,this.docform.value).then(()=>{
//       this.router.navigate(['/doct'])
//      }).catch((error: any) => console.log(error))
//     }
//   }

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { AdminService } from '../../shared/admin.service';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.page.html',
  styleUrls: ['./edit.page.scss'],
})
export class EditPage implements OnInit {
  updatedocform?: FormGroup <any>; // Correction : le nom de la variable doit correspondre à celui utilisé dans ngOnInit()
  ID: any;

  constructor(
    private adminservice: AdminService,
    private router: Router,
    private fb: FormBuilder,
    private actroute: ActivatedRoute
  ) {
    this.ID = this.actroute.snapshot.paramMap.get('ID');
    /*this.adminservice.getdoc(this.ID).valueChanges().subscribe((res: any) => {
      this.updatedocform.patchValue(res); // Correction : utiliser patchValue au lieu de setValue
    });*/
  }

  ngOnInit() {
    this.updatedocform = this.fb.group({
      $key: [''],
      name: [''],
      email: [''],
      mobile: [''],
      details: [''],
    });

    console.log(this.updatedocform.value);
  }

  updateform() {
    /*this.adminservice.updatepers(this.ID, this.updatedocform.value) // Correction : utiliser updatedocform au lieu de docform
      .then(() => {
        this.router.navigate(['/doct']);
      })
      .catch((error: any) => console.log(error));*/
  }
}
