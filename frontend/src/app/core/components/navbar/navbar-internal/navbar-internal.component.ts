import { Component, OnInit, Input } from '@angular/core';
import { User } from 'src/app/core/models/user.model';

@Component({
  selector: 'app-navbar-internal',
  templateUrl: './navbar-internal.component.html',
  styleUrls: ['./navbar-internal.component.css']
})
export class NavbarInternalComponent implements OnInit {

  @Input() user: User;
  
  constructor() { }

  ngOnInit() {
  }

}
