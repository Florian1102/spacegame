import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { UserService } from 'src/app/core/services/user.service';
import { Router } from '@angular/router';
import { User } from 'src/app/core/models/user.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: FormGroup;
  user: User;

  constructor(private fb: FormBuilder,
    private userService: UserService,
    private router: Router) { }

  ngOnInit() {
    this.form = this.fb.group({
      name: ['']
    });
  }

  handleSubmit() {

    this.userService.findUserByName(this.form.value.name).subscribe(foundUser => {
      this.user = foundUser;
      this.router.navigate(['profile']);
    })
  }

}
