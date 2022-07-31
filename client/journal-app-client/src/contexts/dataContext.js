import React, {useContext, useState, useEffect} from 'react';
import {useAuth} from '../contexts/authContext';
import listUtil from '../util/functions/list-util';
import {
    createJournalDoc, updateJournalDoc, getJournalDocs,         // journals
    createJournalEntryDoc, getJournalEntryDocs,                 // journal entries
    getDashboardWidgetConfigDocs, createWidgetConfigDoc         // widget configs
} from '../facades/data';


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
        async function loadJournalList(){
            if (userId==null){
                listUtil(journalList, setJournalList, { type: "TRUNCATE"});
                return;
            } 
            const journalDocs = await getJournalDocs(userId);
            listUtil(journalList, setJournalList, { type: "SET", payload:journalDocs});
        }
        loadJournalList();
    },[userId])



    async function createJournal(journal){
        const newJournal = await createJournalDoc(journal);
        listUtil(journalList, setJournalList, { type: "INSERT", payload: newJournal });
        return newJournal;
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

    async function updateJournal(journalId, payload){
        await updateJournalDoc(journalId, payload);

        const updatedJournal = {...getJournalDoc(journalId)};

        payload.topics.map(payloadTopic=>{
            if (!updatedJournal.topics.some(topic=>topic === payloadTopic))
                updatedJournal.topics.push(payloadTopic);
        });

        updatedJournal.last_updated = payload.last_updated;
        setJournalList(journalList.map(journal=>
            journal.key === updatedJournal.key ? 
                updatedJournal : journal   
        ));
    }

    async function createWidgetConfig(config){
        const newWidgetConfig = await createWidgetConfigDoc(config);
        return newWidgetConfig;
    }

    async function getDashboardConfig(journalId){
        if (userId==null) return;
        return await getDashboardWidgetConfigDocs(journalId);
    }


    const values = {
        journalList, userId, createJournal, getJournalDoc, createJournalEntry,getJournalEntries,updateJournal, createWidgetConfig, getDashboardConfig
    }

    
    return (
        <DataContext.Provider value={values}>
            {children}
        </DataContext.Provider>
    )

}

