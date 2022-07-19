// for defaulting date field to today
const today = new Date();
const day = ((today.getDate() > 9) ? '' : '0') + today.getDate();
const month = ((today.getMonth() > 9) ? '' : '0') + (today.getMonth() + 1);
const year = today.getFullYear();

export const DefaultJournalEntry = {
    journalCollection: "",
    summary: "",
    dateOfEntry: year + "-" + month + "-" + day,
    overview: "",
    journalBodyItems: []
}
