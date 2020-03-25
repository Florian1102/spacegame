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
  days: number;
  hours: number;
  minutes: number;
  seconds: number;
  isRunning: boolean = false;

  constructor() { 
    this.setCurrentDeltaTime();

  }

  ngOnInit() {
    
    setInterval(() => {
      if (this.necessaryTime !== null){
        
        this.setCurrentDeltaTime();
        this.isRunning = true;
        
        if (this.deltaTime <= 0 && this.isRunning) {
          this.isRunning = false;
          window.location.reload();
        };
      }
    }, 1000);
    
  }
  
  setCurrentDeltaTime(){
    this.currentTime = new Date().getTime();
    this.deltaTime = new Date(this.necessaryTime).getTime() - this.currentTime;
    this.convertTime();
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


}
