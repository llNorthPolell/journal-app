import {
    journalRef,journalEntriesRef,       //firestore refs
    getList,createDoc,query, where,     //firestore queries
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
    if (userId==null) return;
    const createdJournalsQuery = query(journalRef, where("author", "==", userId));
    return getList(createdJournalsQuery);    
}


export async function createJournalEntryDoc(journalEntry) {
    try {
        let docRef = await createDoc(journalEntriesRef, journalEntry);
        let returnId = docRef.id;
        console.log("New Journal Entry ID: " + returnId);
        return {...journalEntry, key: returnId};
    }
    catch (err) {
        console.log(err.message);
    };
}

