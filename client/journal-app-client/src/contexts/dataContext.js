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

    const [currentJournal, setCurrentJournal] = useState();
    const [dashboardLoaded, setDashboardLoaded] = useState(false);
    const [journalEntriesList, setJournalEntriesList] = useState([]);
    const [dashboardConfigList, setDashboardConfigList] = useState([]);

    useEffect(()=>{
        const unsubscribe = auth.onAuthStateChanged(authUser => {
            if (authUser){
                setUserId(authUser.uid);
            }
        });
        return unsubscribe;
    },[]);

    useEffect(()=>{
        if (userId) loadJournalList(userId);
    },[userId]);

    useEffect(()=>{
        async function loadDashboardData(){
            if (!currentJournal) return;
            await loadJournalEntries();
            await loadDashboardConfigList();

            setDashboardLoaded(true);
        }
        loadDashboardData();
    }, [currentJournal])

    async function loadJournalEntries(){
        if (!currentJournal) return;
        let resultJournalEntryDocs = await getJournalEntryDocs(currentJournal.key);
        resultJournalEntryDocs.sort((a,b)=>{return a.dateOfEntry>b.dateOfEntry});
        listUtil(journalEntriesList,setJournalEntriesList,{type:"SET",payload:resultJournalEntryDocs});
    }


    async function loadDashboardConfigList(){
        if (!currentJournal) return;
        let resultDashboardConfigDocs = await getDashboardConfig(currentJournal.key);
        resultDashboardConfigDocs.sort((a,b)=>{return a.position>b.position});
        listUtil(dashboardConfigList,setDashboardConfigList,{type:"SET",payload:resultDashboardConfigDocs});
    }


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
        setDashboardLoaded(false);
        return newJournalEntry;
    }

    function getJournalEntries(journalId){
        return journalEntriesList;
    }

    async function getJournalDoc(journalId){
        console.log("Finding " + journalId + " in " + JSON.stringify(journalList));
        return journalList.find(journal=>{return journal.key===journalId})
    }

    async function updateJournal(journalId, payload){
        let savePayload = {...payload};
        let saveSchemas = [...currentJournal.schemas];

        payload.schemas.forEach(payloadSchema=>{
            if (!saveSchemas.some(saveSchema => saveSchema.topic === payloadSchema.topic))
                saveSchemas.push(payloadSchema);
            else {
                saveSchemas.map(schema=>{
                    if(schema.topic===payloadSchema.topic){
                        const newRecords=payloadSchema.records.filter(record=> !schema.records.includes(record))
                        schema.records.push(...newRecords);
                    }
                });

            }
        });

        savePayload.schemas = saveSchemas;

        console.log("Updated Journal Schema: " + JSON.stringify(savePayload));

        await updateJournalDoc(journalId, savePayload);
        await loadJournalList(userId);
        const updatedJournalDoc = await getJournalDoc(journalId);
        setCurrentJournal(updatedJournalDoc);
    }

    async function createWidgetConfig(config){
        const newWidgetConfig = await createWidgetConfigDoc(config);
        setDashboardLoaded(false);
        await loadDashboardConfigList();
        setDashboardLoaded(true);
        return newWidgetConfig;
    }

    async function getDashboardConfig(journalId){
        return await getDashboardWidgetConfigDocs(journalId);
    }


    async function triggerLoadDashboardData (journalId){
        if (dashboardLoaded && journalId === currentJournal.key) return dashboardLoaded;

        const retreivedJournalDoc = await getJournalDoc(journalId);
        
        if (retreivedJournalDoc==null) return false;

        setCurrentJournal(retreivedJournalDoc);
        
        return dashboardLoaded;
    }

    async function clearDashboardData(){
        listUtil(journalEntriesList,setJournalEntriesList,{type:"TRUNCATE"});
        listUtil(dashboardConfigList,setDashboardConfigList,{type:"TRUNCATE"});
        setCurrentJournal(null);
        setDashboardLoaded(false);
    }

    const values = {
        journalList, userId, 
        createJournal, getJournalDoc, updateJournal,                //journal
        createJournalEntry,getJournalEntries, 
        createWidgetConfig, getDashboardConfig, triggerLoadDashboardData, clearDashboardData,   // Dashboard data
        journalListLoaded, dashboardLoaded, currentJournal  // triggers
    }

    
    return (
        <DataContext.Provider value={values}>
            {children}
        </DataContext.Provider>
    )

}

