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

    const { getJournalEntries, getDashboardConfig,triggerLoadDashboardData, journalListLoaded, dashboardLoaded, currentJournal,createWidgetConfig } = useData();

    const [dashboardWidgetContents, setDashboardWidgetContents]=useState([]);
    const [newDashboardWidgets, setNewDashboardWidgets] = useState([]);

    useEffect(()=>{
        if (!dashboardLoaded) callLoadDashboard();
    },[journalListLoaded]);

    useEffect(()=>{
        if (!dashboardLoaded) callLoadDashboard();
        loadDashboardWidgets(journalId);
    }, [dashboardLoaded])

    async function callLoadDashboard(){
        await triggerLoadDashboardData(journalId);
    }

    async function loadDashboardWidgets(){
        const journalEntriesList = await getJournalEntries(journalId);
        const dashboardConfigs = await getDashboardConfig(journalId);
        listUtil(dashboardWidgetContents, setDashboardWidgetContents, { type: "SET", payload:processDashboardWidgets(dashboardConfigs,journalEntriesList)});
    }

    async function filterJournalEntries(searchInput){
        let searchResults = [];
        //if (!isNaN(Date.parse(searchInput))){

        //} 
        const journalEntriesList = await getJournalEntries(journalId);

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


    const values = {
        getJournalEntries, filterJournalEntries, getJournalEntry, dashboardWidgetContents, currentJournal, getOpenDashboardPosition,createWidgetConfig
    }

    
    return (
        <DashboardContext.Provider value={values}>
            {children}
        </DashboardContext.Provider>
    )

}

