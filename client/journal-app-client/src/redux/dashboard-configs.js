import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { createWidgetConfigDoc, getDashboardWidgetConfigDocs } from '../facades/data';

export const loadDashboardConfigList = createAsyncThunk('dashboardConfigs/loadDashboardConfigList', async (journalId)=> {
    console.log("in loadDashboardConfigList...");
    let resultDashboardConfigs = await getDashboardWidgetConfigDocs(journalId);
    return resultDashboardConfigs.map(dashboardConfig=> ({...dashboardConfig,journal:journalId}));
})

export const insertDashboardConfig = createAsyncThunk('dashboardConfigs/insertDashboardConfig', async (config)=> {
    console.log("in insertDashboardConfig...");
    return await createWidgetConfigDoc(config);
})

const initialState = {
    dashboardConfigs:[],
    status: "empty",
    error: "",
    currentJournal: null
}

export const dashboardConfigsSlice = createSlice({
  name: "dashboardConfigs",
  initialState,
  reducers: {
    setCurrentJournal: (state,action)=> {
        state.currentJournal=action.payload
    },
    reset: (state)=>{
        state = initialState
    }
  },
  extraReducers: (builder)=> {
    builder.addCase(loadDashboardConfigList.pending, (state)=>{
        console.log("in loadDashboardConfigList.pending...");
        state.status = "loading";
    }) 
    builder.addCase(loadDashboardConfigList.fulfilled, (state, action)=>{
        console.log("in loadDashboardConfigList.fulfilled...");
        state.dashboardConfigs = action.payload;
        state.status = "loaded";
        state.error = "";
        console.log("dashboardConfigs state: "+JSON.stringify(state));
    })
    builder.addCase(loadDashboardConfigList.rejected,(state,action)=>{
        console.log("in loadDashboardConfigList.rejected...");
        state.status = "failed";
        state.error=action.error.message
        console.error("ERROR: "+action.error.message);
    })
    builder.addCase(insertDashboardConfig.pending,(state)=>{
        console.log("in insertDashboardConfig.pending...");
        state.status="updating";
    })
    builder.addCase(insertDashboardConfig.fulfilled, (state, action)=> {
        console.log("in insertDashboardConfig.fulfilled...");
        state.dashboardConfigs.push(action.payload);
        state.error = "";
        state.status="loaded";
    })
  }
})

export const {setCurrentJournal,reset} = dashboardConfigsSlice.actions;

export default dashboardConfigsSlice.reducer