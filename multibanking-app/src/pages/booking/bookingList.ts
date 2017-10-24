import { Component, ViewChild } from "@angular/core";
import { NavController, AlertController, ToastController, NavParams, LoadingController, Navbar } from "ionic-angular";
import { BankAccountService } from "../../services/bankAccountService";
import { BookingService } from "../../services/bookingService";
import { BankAccess } from "../../api/BankAccess";
import { Booking } from "../../api/Booking";
import { LogoService } from '../../services/LogoService';

@Component({
  selector: 'page-bookingList',
  templateUrl: 'bookingList.html'
})
export class BookingListPage {

  bankAccess: BankAccess;
  bankAccountId: string;
  getLogo: Function;
  bookings: Array<Booking>;

  @ViewChild(Navbar) navBar: Navbar;

  constructor(
    public navCtrl: NavController,
    private navparams: NavParams,
    private alertCtrl: AlertController,
    private toastCtrl: ToastController,
    private loadingCtrl: LoadingController,
    private bankAccountService: BankAccountService,
    private bookingService: BookingService,
    private logoService: LogoService
  ) {
    this.bankAccess = navparams.data.bankAccess;
    this.bankAccountId = navparams.data.bankAccountId;
    this.getLogo = logoService.getLogo;
  }

  ngOnInit() {
    this.bookingService.getBookings(this.bankAccess.id, this.bankAccountId).subscribe(
      response => {
        this.bookings = response;
      },
      error => {
        if (error == "SYNC_IN_PROGRESS") {
          this.toastCtrl.create({
            message: 'Account sync in progress',
            showCloseButton: true,
            position: 'top'
          }).present();
        }
      })
  }

  ionViewDidLoad() {
    this.navBar.backButtonClick = (e: UIEvent) => {
      this.navCtrl.parent.viewCtrl.dismiss();
    };
  }

  syncBookingsPromptPin() {
    let alert = this.alertCtrl.create({
      title: 'Pin',
      inputs: [
        {
          name: 'pin',
          placeholder: 'Bank Account Pin',
          type: 'password'
        }
      ],
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel'
        },
        {
          text: 'Submit',
          handler: data => {
            if (data.pin.length > 0) {
              this.syncBookings(data.pin);
            }
          }
        }
      ]
    });
    alert.present();
  }

  syncBookings(pin) {
    if (!pin && !this.bankAccess.storePin) {
      return this.syncBookingsPromptPin();
    }

    let loading = this.loadingCtrl.create({
      content: 'Please wait...'
    });
    loading.present();

    this.bankAccountService.syncBookings(this.bankAccess.id, this.bankAccountId, pin).subscribe(
      response => {
        this.bookings = response;
        loading.dismiss();
      },
      error => {
        if (error == "SYNC_IN_PROGRESS") {
          this.toastCtrl.create({
            message: 'Account sync in progress',
            showCloseButton: true,
            position: 'top'
          }).present();
        }
        else if (error.message == "INVALID_PIN") {
            this.alertCtrl.create({
              message: 'Invalid pin',
              buttons: ['OK']
            }).present();
          }
      })
  }

  itemSelected(booking) {
    console.log(booking)
  }
}
