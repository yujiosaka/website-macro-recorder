interface MacroHistory {
  screenshotUrl: string;
  isEntirePageUpdated: boolean;
  isSelectedAreaUpdated: boolean;
  isFailure: boolean;
  executedAt: FirebaseFirestore.Timestamp;
}
