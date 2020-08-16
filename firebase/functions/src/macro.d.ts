interface Macro {
  id: String;
  uid: String;
  name: string;
  url: string;
  screenshotUrl: string;
  scheduleFrequency: number;
  scheduleHour: number;
  scheduleMinute: number;
  notifySuccess: boolean;
  notifyFailure: boolean;
  checkEntirePage: boolean;
  checkSelectedArea: boolean;
  isFailure: boolean;
  userAgent: string;
  acceptLanguage: string;
  height: number;
  width: number;
  deviceScaleFactor: number;
  selectedAreaLeft: number;
  selectedAreaRight: number;
  selectedAreaTop: number;
  selectedAreaBottom: number;
  events: MacroEvent[];
}
