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
    const { auth } = useAuth();
    const [journalListLoaded, setJournalListLoaded] = useState(false);
    const [journalList, setJournalList] = useState([]);
    const [userId, setUserId] = useState();

    const [journalDoc, setJournalDoc] = useState();
    const [dashboardLoaded, setDashboardLoaded] = useState(false);
    const [journalEntriesList, setJournalEntriesList] = useState([]);
    const [dashboardConfigList, setDashboardConfigList] = useState([]);

    useEffect(()=>{
        const unsubscribe = auth.onAuthStateChanged(authUser => {
            if (authUser){
                setUserId(authUser.uid);
                loadJournalList(authUser.uid);
            }
        });
        return unsubscribe;
    },[]);

    async function loadJournalList(userId){
        const journalDocs = await getJournalDocs(userId);
        listUtil(journalList, setJournalList, { type: "SET", payload:journalDocs});
        setJournalListLoaded(true);
    }

    async function createJournal(journal){
        const newJournal = await createJournalDoc(journal);
        listUtil(journalList, setJournalList, { type: "INSERT", payload: newJournal });
        return newJournal;
    }

    
    async function createJournalEntry(journalEntry){
        const newJournalEntry = await createJournalEntryDoc(journalEntry);
        setJournalEntriesList(await getJournalEntries(journalDoc.key));
        return newJournalEntry;
    }

    async function getJournalEntries(journalId){
        return await getJournalEntryDocs(journalId);
    }

    async function getJournalDoc(journalId){
        if (!journalListLoaded) await loadJournalList(userId);
        console.log("Finding " + journalId + " in " + journalList);
        return journalList.find(journal=>{return journal.key===journalId})
    }

    async function updateJournal(journalId, payload){
        await updateJournalDoc(journalId, payload);

        const updatedJournal = {...await getJournalDoc(journalId)};

        payload.topics.map(payloadTopic=>{
            if (!updatedJournal.topics.some(topic=>topic === payloadTopic))
                updatedJournal.topics.push(payloadTopic);
        });

        updatedJournal.last_updated = payload.last_updated;
        setJournalList(journalList.map(journal=>
            journal.key === updatedJournal.key ? 
                updatedJournal : journal   
        ));

        if (journalDoc!= null && updatedJournal.key === journalDoc.key) setJournalDoc(updatedJournal);
    }

    async function createWidgetConfig(config){
        const newWidgetConfig = await createWidgetConfigDoc(config);
        return newWidgetConfig;
    }

    async function getDashboardConfig(journalId){
        return await getDashboardWidgetConfigDocs(journalId);
    }



    async function loadDashboardData (journalId){
        if (dashboardLoaded && journalId === journalDoc.key) return false;

        const retreivedJournalDoc = await getJournalDoc(journalId);
        
        if (retreivedJournalDoc==null) return false;
        console.log("Load Dashboard!" + journalId);
        setJournalDoc(retreivedJournalDoc);
        let resultJournalEntryDocs = await getJournalEntries(journalId);
        let resultDashboardConfigDocs = await getDashboardConfig(journalId);
        resultJournalEntryDocs.sort((a,b)=>{return a.dateOfEntry>b.dateOfEntry});
        resultDashboardConfigDocs.sort((a,b)=>{return a.position<b.position});

        listUtil(journalEntriesList,setJournalEntriesList,{type:"SET",payload:resultJournalEntryDocs});
        listUtil(dashboardConfigList,setDashboardConfigList,{type:"SET",payload:resultDashboardConfigDocs});
        setDashboardLoaded(true);
        return true;
    }

    async function clearDashboardData(){
        listUtil(journalEntriesList,setJournalEntriesList,{type:"TRUNCATE"});
        listUtil(dashboardConfigList,setDashboardConfigList,{type:"TRUNCATE"});
        setJournalDoc(null);
    }

    const values = {
        journalList, userId, 
        createJournal, getJournalDoc, updateJournal,
        createJournalEntry,getJournalEntries, 
        createWidgetConfig, getDashboardConfig, loadDashboardData, clearDashboardData,
        journalListLoaded, dashboardLoaded
    }

    
    return (
        <DataContext.Provider value={values}>
            {children}
        </DataContext.Provider>
    )

}

