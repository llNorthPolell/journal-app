import {
    journalRef,journalEntriesRef,       //firestore refs
    getList,createDoc,query, where,doc,     //firestore queries
    uploadFile    //storage
} from '../firebase'


export async function createJournalDoc(journal) {
    let saveJournal = journal;
    let docRef = null;
    let returnId = null;

    saveJournal.img = (journal.img != null) ?
        await uploadFile(journal.img)
        :
        "https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/defaultImg.png?alt=media&token=caa93413-9a70-47df-978e-bc787ec05378";


    console.log(saveJournal);

    try {
        docRef = await createDoc(journalRef, saveJournal)
        returnId = docRef.id;
        console.log("New Journal ID: " + returnId);
        return { ...saveJournal, key: returnId };
    }
    catch (err) {
        console.log(err.message);
    };
}

export function getJournalDocs(userId) {
    const createdJournalsQuery = query(journalRef, where("author", "==", userId));
    return getList(createdJournalsQuery);    
}


export async function createJournalEntryDoc(journalEntry) {
    const journalDocRef = doc(journalRef,journalEntry.journal);
    let saveJournalEntry = journalEntry;
    saveJournalEntry.journal = journalDocRef;

    try {
        let docRef = await createDoc(journalEntriesRef, saveJournalEntry);
        let returnId = docRef.id;
        console.log("New Journal Entry ID: " + returnId);
        return {...saveJournalEntry, key: returnId};
    }
    catch (err) {
        console.log(err.message);
    };
}

export function getJournalEntryDocs(journalId){
    const queryDocRef = doc(journalRef,journalId);
    const journalEntriesQuery = query(journalEntriesRef, where("journal", "==", queryDocRef));
    return getList(journalEntriesQuery);
}