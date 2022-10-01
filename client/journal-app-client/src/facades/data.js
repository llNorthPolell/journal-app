import {
    journalRef,journalEntriesRef,dashboardWidgetConfigRef,goalsRef,      //firestore refs
    getList,get,createDoc,query, where,doc,arrayUnion,updateDoc,     //firestore queries
    uploadFile    //storage
} from '../firebase'


export async function createJournalDoc(journal) {
    let saveJournal = {...journal};
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
        console.error(err.message);
    };
}

export function getJournalDocs(userId) {
    const createdJournalsQuery = query(journalRef, where("author", "==", userId));
    return getList(createdJournalsQuery);    
}

export function getJournalDoc(journalId){
    const journalDocRef = doc(journalRef,journalId);
    return get(journalDocRef);
}

export async function updateJournalDoc(journalId, payload){
    const journalDocRef = doc(journalRef,journalId);
    console.log ("updateJournalDoc: journalId: "+journalId +", payload: "+ JSON.stringify(payload));
    await updateDoc(journalDocRef,payload);
    const returnJournal = await getJournalDoc(journalId);
    return returnJournal;
}


export async function createJournalEntryDoc(journalEntry) {
    const journalDocRef = doc(journalRef,journalEntry.journal);
    const saveJournalEntry = {...journalEntry, journal: journalDocRef};

    console.log("Creating " + JSON.stringify(saveJournalEntry));
    try {
        let docRef = await createDoc(journalEntriesRef, saveJournalEntry);
        let returnId = docRef.id;
        console.log("New Journal Entry ID: " + returnId);
        return {...saveJournalEntry, key: returnId};
    }
    catch (err) {
        console.error(err.message);
    };
}

export function getJournalEntryDocs(journalId){
    const queryDocRef = doc(journalRef,journalId);
    const journalEntriesQuery = query(journalEntriesRef, where("journal", "==", queryDocRef));
    return getList(journalEntriesQuery);
}

export function getJournalEntryDoc(journalEntryId){
    const journalEntryDocRef = doc(journalEntriesRef,journalEntryId);
    return get(journalEntryDocRef);
}

export async function updateJournalEntryDoc(journalEntryId, payload){
    const journalEntriesDocRef = doc(journalEntriesRef,journalEntryId);
    const journalDocRef = doc(journalRef,payload.journal);
    let saveJournalEntry = {...payload};
    saveJournalEntry.journal = journalDocRef;
    await updateDoc(journalEntriesDocRef,saveJournalEntry);
    const returnJournalEntry = await getJournalEntryDoc(journalEntryId);
    return returnJournalEntry;
}

export async function createWidgetConfigDoc(config){
    const journalDocRef = doc(journalRef,config.journal);
    let saveConfig = {...config};
    config.journal = journalDocRef;

    try {
        let docRef = await createDoc(dashboardWidgetConfigRef, config);
        let returnId = docRef.id;
        console.log("New Widget Config Entry ID: " + returnId);
        return {...saveConfig, key: returnId};
    }
    catch (err){
        console.error(err.message);
    }
}


export function getDashboardWidgetConfigDocs(journalId){
    const queryDocRef = doc(journalRef, journalId);
    const dashboardWidgetConfigQuery=query(dashboardWidgetConfigRef,where("journal", "==", queryDocRef));
    return getList(dashboardWidgetConfigQuery);
}



export async function createGoalDoc(goal){
    const journalDocRef = doc(journalRef,goal.journal);
    const saveGoal = {...goal, journal: journalDocRef};

    console.log("Creating " + JSON.stringify(saveGoal));
    try {
        let docRef = await createDoc(goalsRef, saveGoal);
        let returnId = docRef.id;
        console.log("New Goal ID: " + returnId);
        return {...saveGoal, key: returnId};
    }
    catch (err) {
        console.error(err.message);
    };
}

export function getGoalDocs(journalId){
    const queryDocRef = doc(journalRef, journalId);
    const goalsQuery=query(goalsRef,where("journal", "==", queryDocRef));
    return getList(goalsQuery);
}

export function getGoalDoc(goalId){
    const goalDocRef = doc(goalsRef,goalId);
    return get(goalDocRef);
}

export async function updateGoalDoc(goalId, payload){
    const goalsDocRef = doc(goalsRef,goalId);
    const journalDocRef = doc(journalRef,payload.journal);
    let saveGoal = {...payload};
    saveGoal.journal = journalDocRef;
    await updateDoc(goalsDocRef,saveGoal);
    const returnGoal = await getGoalDoc(goalId);
    return returnGoal;
}