interface Macro {
  id: string;
  uid: string;
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
  viewportHeight: number;
  viewportWidth: number;
  deviceScaleFactor: number;
  selectedAreaLeft: number;
  selectedAreaRight: number;
  selectedAreaTop: number;
  selectedAreaBottom: number;
  events: MacroEvent[];
  createdAt: FirebaseFirestore.FieldValue;
  updatedAt: FirebaseFirestore.FieldValue;
  executedAt: FirebaseFirestore.FieldValue;
}
