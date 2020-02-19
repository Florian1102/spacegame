import { Component, OnInit, Input } from '@angular/core';
import { User } from 'src/app/core/models/user.model';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/core/services/user.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  userId: number;
  user: User;
  name: string;

  form: FormGroup;

  constructor(private route: ActivatedRoute, private userService: UserService, private fb: FormBuilder, private authService: AuthService) { }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => this.userId = +paramMap.get('userId')); 
    this.findUser();
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
    });
  }

  findUser() {
    this.userService.findUserById(this.userId).subscribe(foundUser => this.user = foundUser);
  }

  renameUser(){
    this.name = this.form.value.name;
    if (this.form.valid) {
      this.userService.renameUser(this.userId, this.name).subscribe(() => {
      this.authService.updateUser(this.userId);
      this.user.name = this.name;
      this.form = this.fb.group({
        name: [''],
      });
      });
    } else {
        alert("Eingabe ungültig");
    }
  }
}
