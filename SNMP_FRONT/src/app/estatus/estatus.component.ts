
import { Component, Inject, OnInit, QueryList, ViewChild, ViewChildren,  } from '@angular/core';
import { Router } from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
@Component({
selector:'app-estatus',
templateUrl: './estatus.component.html',
styleUrls: ['./estatus.component.css']

})

export class EstatusComponent implements OnInit {
    constructor(
        private router:Router,
        private spinner:NgxSpinnerService,
        ){}
    ngOnInit() {
      this.spinner.show();
    }
    
   
  
}

