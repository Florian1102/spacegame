import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { User } from 'src/app/core/models/user.model';
import { UserService } from 'src/app/core/services/user.service';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';


@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  form: FormGroup;

  constructor(private fb: FormBuilder,
              private userService: UserService,
              private authService: AuthService,
              private router: Router) {
  }

  ngOnInit() {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  handleSubmit() {
    if (this.form.valid) {
      let user: User = {
        name: this.form.value.name
      }
      this.userService.createUser(user).subscribe(newUserCreated => {
        this.authService.loginUser(newUserCreated),
        this.router.navigate(['user/' + newUserCreated.id + '/overview'])
      })
    } else {
        alert("Eingabe ungültig");
    }
  }

}
