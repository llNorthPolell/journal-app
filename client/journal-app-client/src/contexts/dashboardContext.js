import React, {useContext, useEffect, useState} from 'react';
import useJournalEntryList from '../facades/hooks/useJournalEntryList';
import useDashboardConfigList from '../facades/hooks/useDashboardConfigList';
import listUtil from '../util/functions/list-util';
import { processDashboardWidgets } from '../dashboard/widgets/widget-refinery';

const DashboardContext = React.createContext();

export function useDashboard() {
    return useContext(DashboardContext);
}

export function DashboardProvider ({children}){
    const [journalEntriesList] = useJournalEntryList(["getAll"]);
    const [dashboardConfigList,createWidgetConfig]= useDashboardConfigList(["getAll","insert"]);

    const [dashboardWidgetContents, setDashboardWidgetContents]=useState([]);
    const [newDashboardWidgets, setNewDashboardWidgets] = useState([]);

    useEffect(()=>{
        function loadDashboardWidgets(){
            const dashboardConfigs = [...dashboardConfigList];
            const tempDashboardContents = processDashboardWidgets(newDashboardWidgets,journalEntriesList);
            const savedDashboardContents = processDashboardWidgets(dashboardConfigs,journalEntriesList);
    
            console.log("Combine " + JSON.stringify(savedDashboardContents)+" + "+JSON.stringify(tempDashboardContents));
            listUtil(dashboardWidgetContents, setDashboardWidgetContents, { type: "SET", payload:[...savedDashboardContents,...tempDashboardContents]});
        }
        loadDashboardWidgets();
    },[newDashboardWidgets,dashboardConfigList])

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
        dashboardWidgetContents, getOpenDashboardPosition,addNewWidgetConfig,saveDashboard,discardChangeDashboard
    }

    
    return (
        <DashboardContext.Provider value={values}>
            {children}
        </DashboardContext.Provider>
    )

}

