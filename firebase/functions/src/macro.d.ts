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
  isFailure: boolean;
  userAgent: string;
  acceptLanguage: string;
  height: number;
  width: number;
  deviceScaleFactor: number;
  events: MacroEvent[];
}
