import React, {useContext, useState, useEffect} from 'react';
import {useAuth} from '../contexts/authContext';
import {
    journalRef,journalEntriesRef,       //firestore refs
    getList,createDoc,query, where,     //firestore queries
    uploadFile    //storage
} from '../firebase'
import listUtil from '../util/functions/list-util';

const DataContext = React.createContext();

export function useData() {
    return useContext(DataContext);
}

export function DataProvider ({children}){
    const { user } = useAuth();
    const [journalList, setJournalList] = useState([]);
    const [userId, setUserId] = useState();

    useEffect(() => {
        setUserId((user != null)?user.uid:null);
    }, [user]);

    useEffect(()=>{
        if (userId!=null){
            const createdJournalsQuery = query(journalRef, where("author", "==", userId));
            getList(createdJournalsQuery).then((journalDocs)=>{
                listUtil(journalList, setJournalList, { type: "SET", payload:journalDocs});
            });      
        } 
    },[userId])



    async function createJournal(journal){
        let saveJournal = journal;
        let docRef=null;
        let returnId=null;

        saveJournal.img = (journal.img != null)? 
            await uploadFile(journal.img)
            :
            "https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/defaultImg.png?alt=media&token=caa93413-9a70-47df-978e-bc787ec05378";
        

        console.log(saveJournal);

        try{
            docRef = await createDoc(journalRef,saveJournal)
            returnId = docRef.id;
            console.log("New Journal ID: " +returnId);
            listUtil(journalList, setJournalList, { type: "INSERT", payload:{...saveJournal,key: returnId}});
        }
        catch(err){
            console.log(err.message);
        };
    }

    
    async function createJournalEntry(journalEntry){
        try{
            let docRef = await createDoc(journalEntriesRef,journalEntry);
            let returnId = docRef.id;
            console.log("New Journal Entry ID: " +returnId);
        }
        catch(err){
            console.log(err.message);
        };
    }


    const values = {
        journalList, createJournal, userId
    }

    
    return (
        <DataContext.Provider value={values}>
            {children}
        </DataContext.Provider>
    )

}

