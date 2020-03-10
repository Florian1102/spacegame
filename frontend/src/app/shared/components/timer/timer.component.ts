import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-timer',
  templateUrl: './timer.component.html',
  styleUrls: ['./timer.component.css']
})
export class TimerComponent implements OnInit {

  @Input() necessaryTime: any;
  currentTime: any;
  deltaTime: any;
  days: number = 0;
  hours: number = 0;
  minutes: number = 0;
  seconds: number = 0;
  isRunning: boolean = false;

  constructor() { }

  ngOnInit() {
    this.setCurrentDeltaTime();
    this.convertTime();

    setInterval(() => {
      if (this.necessaryTime !== null){

        this.setCurrentDeltaTime();
        this.convertTime();
        this.isRunning = true;

        if (this.deltaTime <= 0 && this.isRunning) {
          window.location.reload();
          this.isRunning = false;
        };
      }
    }, 1000);
  }

  convertTime(){
    if (this.deltaTime <= 0){
      this.days = 0;
      this.hours = 0;
      this.minutes = 0;
      this.seconds = 0;
    } else {
      this.days = Math.floor(this.deltaTime / (1000 * 60 * 60 * 24)) || 0;
      this.hours = Math.floor((this.deltaTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)) || 0;
      this.minutes = Math.floor((this.deltaTime % (1000 * 60 * 60)) / (1000 * 60)) || 0;
      this.seconds = Math.floor((this.deltaTime % (1000 * 60)) / 1000) || 0;
    }
  }

  setCurrentDeltaTime(){
    this.currentTime = new Date().getTime();
    this.deltaTime = new Date(this.necessaryTime).getTime() + 1000 - this.currentTime;
  }

}
