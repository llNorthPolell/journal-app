import React, {useContext, useState, useEffect} from 'react';
import {useAuth} from '../contexts/authContext';
import listUtil from '../util/functions/list-util';
import {getJournalDocs, createJournalDoc, createJournalEntryDoc, getJournalEntryDocs} from '../facades/data';


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

    useEffect(async ()=>{
        if (userId==null){
            listUtil(journalList, setJournalList, { type: "TRUNCATE"});
            return;
        } 
        const journalDocs = await getJournalDocs(userId);
        listUtil(journalList, setJournalList, { type: "SET", payload:journalDocs});
    },[userId])



    async function createJournal(journal){
        const newJournal = await createJournalDoc(journal);
        listUtil(journalList, setJournalList, { type: "INSERT", payload: newJournal });
    }

    
    async function createJournalEntry(journalEntry){
        const newJournalEntry = await createJournalEntryDoc(journalEntry);
        return newJournalEntry;
    }

    async function getJournalEntries(journalId){
        if (userId==null) return;
        return await getJournalEntryDocs(journalId);
    }

    function getJournalDoc(journalId){
        if (userId==null) return;
        console.log("Finding " + journalId + " in " + journalList);
        return journalList.find(journal=>{return journal.key===journalId})
    }

    const values = {
        journalList, userId, createJournal, getJournalDoc, createJournalEntry,getJournalEntries
    }

    
    return (
        <DataContext.Provider value={values}>
            {children}
        </DataContext.Provider>
    )

}

