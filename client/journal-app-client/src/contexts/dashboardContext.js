import React, {useContext, useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import {useData} from '../contexts/dataContext';
import listUtil from '../util/functions/list-util';
import { processDashboardWidgets } from '../dashboard/widgets/widget-refinery';

const DashboardContext = React.createContext();

export function useDashboard() {
    return useContext(DashboardContext);
}

export function DashboardProvider ({children}){
    const {journalId} = useParams();

    const { getJournalEntries, getDashboardConfig,triggerLoadDashboardData, journalListLoaded, dashboardLoaded, currentJournal,createWidgetConfig,journalEntriesList } = useData();

    const [dashboardWidgetContents, setDashboardWidgetContents]=useState([]);
    const [newDashboardWidgets, setNewDashboardWidgets] = useState([]);

    useEffect(()=>{
        if (!dashboardLoaded) callLoadDashboard();
    },[journalListLoaded]);

    useEffect(()=>{
        async function callLoadDashboardWidgets() {
            if (!dashboardLoaded) await callLoadDashboard();
            await loadDashboardWidgets(journalId);
        }

        callLoadDashboardWidgets();
    }, [dashboardLoaded, journalEntriesList]);

    useEffect(()=>{
        loadDashboardWidgets();
    },[newDashboardWidgets])

    async function callLoadDashboard(){
        await triggerLoadDashboardData(journalId);
    }

    async function loadDashboardWidgets(){
        const journalEntriesList = getJournalEntries(journalId);
        const dashboardConfigs = getDashboardConfig();
        const tempDashboardContents = processDashboardWidgets(newDashboardWidgets,journalEntriesList);
        const savedDashboardContents = processDashboardWidgets(dashboardConfigs,journalEntriesList);


        console.log("Combine " + JSON.stringify(savedDashboardContents)+" + "+JSON.stringify(tempDashboardContents));
        listUtil(dashboardWidgetContents, setDashboardWidgetContents, { type: "SET", payload:[...savedDashboardContents,...tempDashboardContents]});

    }

    async function filterJournalEntries(searchInput){
        let searchResults = [];
        //if (!isNaN(Date.parse(searchInput))){

        //} 
        const journalEntriesList = getJournalEntries(journalId);

        journalEntriesList.map(
            journalEntry => {
                journalEntry.journalBodyItems.map(
                    journalBodyItem=>{
                        if (journalBodyItem.topic.includes(searchInput) || 
                            journalBodyItem.description.includes(searchInput))
                            searchResults.push({...journalBodyItem, key: journalEntry.key,dateOfEntry:journalEntry.dateOfEntry});
                        
            })}  
        );

        searchResults.sort((a,b)=>{return a.dateOfEntry<b.dateOfEntry});

        return searchResults;
    }

    async function getJournalEntry(key){
        const journalEntriesList = await getJournalEntries(journalId);
        return journalEntriesList.find(journalEntry=> journalEntry.key===key);
    }

    function getOpenDashboardPosition(){
        let maxPosition = 0;
        dashboardWidgetContents.forEach(dashboardWidgetContent=>{
            maxPosition = (dashboardWidgetContent.position > maxPosition)? dashboardWidgetContent.position : maxPosition;
        });
        return maxPosition+1;
    }


    function addNewWidgetConfig(config){
        setNewDashboardWidgets([...newDashboardWidgets,config]);
    }

    async function saveDashboard(){
        newDashboardWidgets.forEach(newDashboardWidget=>{
            createWidgetConfig(newDashboardWidget);
        });
        setNewDashboardWidgets([]);
    }

    function discardChangeDashboard(){
        setNewDashboardWidgets([]);
    }

    const values = {
        getJournalEntries, filterJournalEntries, getJournalEntry, dashboardWidgetContents, currentJournal, getOpenDashboardPosition,addNewWidgetConfig,saveDashboard,discardChangeDashboard
    }

    
    return (
        <DashboardContext.Provider value={values}>
            {children}
        </DashboardContext.Provider>
    )

}

